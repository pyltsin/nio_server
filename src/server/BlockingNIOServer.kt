package server

import handlers.BlockingChannelHandler
import handlers.ExecutorServiceHandler
import handlers.PrintingHandler
import handlers.TransmogrifyChannelHandler
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.util.concurrent.Executors

fun main() {
    val ssc: ServerSocketChannel = ServerSocketChannel.open()
    ssc.bind(InetSocketAddress(8080))
    val handler =
        ExecutorServiceHandler(
            PrintingHandler(
                BlockingChannelHandler(
                    TransmogrifyChannelHandler()
                )
            ),
            Executors.newFixedThreadPool(10)
        )

    while (true) {
        val s = ssc.accept()
        handler.handle(s)
    }
}