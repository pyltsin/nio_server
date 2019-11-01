package handlers

import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque

class AcceptHandler(val pendingData: MutableMap<SocketChannel, Queue<ByteBuffer>>) : Handler<SelectionKey> {
    override fun handle(selectionKey: SelectionKey) {
        val ssc: ServerSocketChannel = selectionKey.channel() as ServerSocketChannel
        val sc = ssc.accept()
        with(sc) {
            println("connected tp $this")
            pendingData[this] = ConcurrentLinkedDeque()
            configureBlocking(false)
            register(selectionKey.selector(), SelectionKey.OP_READ)
        }
    }
}
