package handlers

import util.tranmogrify
import java.net.Socket

class TransmogrifyHandler : Handler<Socket> {
    override fun handle(s: Socket) {
        s.use {
            val inStream = s.getInputStream()
            inStream.use {
                val out = s.getOutputStream()
                out.use {
                    var data: Int
                    data = inStream.read()
                    while (data != -1) {
                        out.write(tranmogrify(data))
                        data = inStream.read()
                    }

                }
            }
        }

    }
}