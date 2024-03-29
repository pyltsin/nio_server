package server

import handlers.AcceptHandler
import handlers.Handler
import handlers.ReadHandler
import handlers.WriteHandler
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*

fun main() {

    val ssc: ServerSocketChannel = ServerSocketChannel.open()
    ssc.bind(InetSocketAddress(8080))
    ssc.configureBlocking(false)

    var selector: Selector = Selector.open()

    ssc.register(selector, SelectionKey.OP_ACCEPT)



    val pendingData: MutableMap<SocketChannel, Queue<ByteBuffer>> = HashMap()

    val acceptHandler: Handler<SelectionKey> = AcceptHandler(pendingData)
    val readHandler: Handler<SelectionKey> = ReadHandler(pendingData)
    val writeHandler: Handler<SelectionKey> = WriteHandler(pendingData)

    while (true) {
        selector.select()
        val keys = selector.selectedKeys()
        val iterator = keys.iterator()
        while (iterator.hasNext()) {
            val key: SelectionKey = iterator.next()
            iterator.remove()

            if (key.isValid()) {
                if (key.isAcceptable) {
                    acceptHandler.handle(key)
                }else if (key.isReadable) {
                    readHandler.handle(key)
                }else if (key.isWritable) {
                    writeHandler.handle(key)
                }
            }
        }

    }
}