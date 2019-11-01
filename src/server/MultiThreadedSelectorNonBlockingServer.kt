package server

import handlers.*
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors

fun main() {

    val ssc: ServerSocketChannel = ServerSocketChannel.open()
    ssc.bind(InetSocketAddress(8080))
    ssc.configureBlocking(false)

    var selector: Selector = Selector.open()

    ssc.register(selector, SelectionKey.OP_ACCEPT)

    val newFixedThreadPool = Executors.newFixedThreadPool(10)

    val pendingData: MutableMap<SocketChannel, Queue<ByteBuffer>> = ConcurrentHashMap()
    val selectorActions = ConcurrentLinkedQueue<Runnable>()

    val acceptHandler: Handler<SelectionKey> = AcceptHandler(pendingData)
    val readHandler: Handler<SelectionKey> = PoolReadHandler(newFixedThreadPool, selectorActions, pendingData)
    val writeHandler: Handler<SelectionKey> = WriteHandler(pendingData)

    while (true) {
        selector.select()
        processSelectionActions(selectorActions)
        val keys = selector.selectedKeys()
        val iterator = keys.iterator()
        while (iterator.hasNext()) {
            val key: SelectionKey = iterator.next()
            iterator.remove()

            if (key.isValid()) {
                if (key.isAcceptable) {
                    acceptHandler.handle(key)
                } else if (key.isReadable) {
                    readHandler.handle(key)
                } else if (key.isWritable) {
                    writeHandler.handle(key)
                }
            }
        }

    }
}

fun processSelectionActions(selectorActions: ConcurrentLinkedQueue<Runnable>) {

    var action: Runnable?

    action = selectorActions.poll()
    while (action != null) {
        action.run()
        action = selectorActions.poll()
    }
}
