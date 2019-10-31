package handlers

class ThreadedHandler<S>(other: Handler<S>) : DecoratedHandler<S>(other) {

    override fun handle(s: S) {
        Thread {
            other.handle(s)
        }.start()
    }
}