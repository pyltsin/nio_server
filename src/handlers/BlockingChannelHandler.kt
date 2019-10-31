package handlers

import java.nio.channels.SocketChannel

class BlockingChannelHandler(other: Handler<SocketChannel>) : DecoratedHandler<SocketChannel>(other) {
    override fun handle(sc: SocketChannel) {
        while (sc.isConnected) {
            super.handle(sc)
        }
    }
}
