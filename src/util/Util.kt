package util

import java.nio.ByteBuffer

fun tranmogrify(data: Int): Int {
    return if (Character.isLetter(data)) {
        (data).toChar().toUpperCase().toInt()
    } else {
        data
    }
}

fun tranmogrify(buf: ByteBuffer) {
    buf.flip()
    for (i in 0 until buf.limit()) {
        buf.put(i, tranmogrify(buf.get(i).toInt()).toByte())

    }
}
