package handlers

import util.tranmogrify
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel
import java.util.*

class ReadHandler(private val pendingData: MutableMap<SocketChannel, Queue<ByteBuffer>>) : Handler<SelectionKey> {
    override fun handle(selectionKey: SelectionKey) {
        var sc: SocketChannel = selectionKey.channel() as SocketChannel
        var buf: ByteBuffer = ByteBuffer.allocateDirect(80)

        val read = sc.read(buf)

        if (read == -1) {
            pendingData.remove(sc)
            sc.close()
            return
        }

        if (read > 0) {
            tranmogrify(buf)
            pendingData[sc]?.add(buf)
            selectionKey.interestOps(SelectionKey.OP_WRITE)
        }
    }
}
