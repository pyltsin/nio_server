package handlers

import util.tranmogrify
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

class TransmogrifyChannelHandler : Handler<SocketChannel> {
    override fun handle(sc: SocketChannel) {
        val buf = ByteBuffer.allocateDirect(80)

        var read = sc.read(buf)

        if (read == -1) {
            sc.close()
            return
        }

        if (read > 0) {
            tranmogrify(buf)
            while (buf.hasRemaining()) {
                sc.write(buf)
            }
        }
    }
}