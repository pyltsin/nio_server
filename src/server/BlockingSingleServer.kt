package server

import handlers.PrintingHandler
import handlers.TransmogrifyHandler
import java.net.ServerSocket

fun main() {
    val ss = ServerSocket(8080)
    val handler = PrintingHandler(
        TransmogrifyHandler()
    )

    while (true) {
        val s = ss.accept()
        handler.handle(s)
    }
}
