package handlers

import util.tranmogrify
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ExecutorService

class PoolReadHandler(private val pool: ExecutorService,
                      private val selectorActions: ConcurrentLinkedQueue<Runnable>,
                      private val pendingData: MutableMap<SocketChannel, Queue<ByteBuffer>>) : Handler<SelectionKey> {
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
            pool.submit {
                tranmogrify(buf)
                pendingData[sc]?.add(buf)
                selectorActions.add(Runnable { selectionKey.interestOps(SelectionKey.OP_WRITE) })
                selectionKey.selector().wakeup()
            }
        }
    }
}
