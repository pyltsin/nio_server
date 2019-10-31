package handlers

abstract class DecoratedHandler<S>
internal constructor(val other: Handler<S>) : Handler<S> {
    override fun handle(s: S) {
        other.handle(s)
    }
}