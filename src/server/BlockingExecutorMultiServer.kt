package server

import handlers.ExecutorServiceHandler
import handlers.PrintingHandler
import handlers.ThreadedHandler
import handlers.TransmogrifyHandler
import java.net.ServerSocket
import java.util.concurrent.Executors

fun main() {
    val ss = ServerSocket(8080)
    val handler =
        ExecutorServiceHandler(
            PrintingHandler(
                TransmogrifyHandler()
            ),
            Executors.newFixedThreadPool(10)
        )

    while (true) {
        val s = ss.accept()
        handler.handle(s)
    }
}