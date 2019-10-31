package server

import handlers.TransmogrifyChannelHandler
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

fun main() {

    val ssc: ServerSocketChannel = ServerSocketChannel.open()
    ssc.bind(InetSocketAddress(8080))
    ssc.configureBlocking(false)

    val handler = TransmogrifyChannelHandler()

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