package handlers

class PrintingHandler<S>(other: Handler<S>): DecoratedHandler<S>(other) {

    override fun handle(s: S) {
        println("Connected to $s")
        try {
            other.handle(s)
        }finally {
            println("Disconnected from $s")
        }
    }
}