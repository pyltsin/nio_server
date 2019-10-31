import java.net.Socket

fun main() {
    val socket = Array<Socket>(3000) {
        Socket("localhost", 8080)
    }

    Thread.sleep(100_000)

}