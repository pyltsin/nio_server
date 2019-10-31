package server

import handlers.AcceptHandler
import handlers.Handler
import handlers.ReadHandler
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import kotlin.collections.ArrayList

fun main() {

    val ssc: ServerSocketChannel = ServerSocketChannel.open()
    ssc.bind(InetSocketAddress(8080))
    ssc.configureBlocking(false)

    var selector: Selector = Selector.open()

    ssc.register(selector, SelectionKey.OP_ACCEPT)



    val pendingData: MutableMap<SocketChannel, Queue<ByteBuffer>> = HashMap()

    val acceptHandler: Handler<SelectionKey> = AcceptHandler(pendingData)
    val readHandler: Handler<SelectionKey> = ReadHandler(pendingData)

    val sockets: MutableList<SocketChannel> = ArrayList()


    while (true) {
        val s: SocketChannel? = ssc.accept()
        if (s != null) {
            sockets.add(s)
            println("Connected to $s")
            s.configureBlocking(false)

        }

        for (socket in sockets) {
            if (socket.isConnected) {
                handler.handle(socket)
            }
        }

        sockets.removeIf { t -> !t.isConnected }
    }
}