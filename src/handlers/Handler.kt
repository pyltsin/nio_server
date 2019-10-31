package handlers

interface Handler<S> {
    fun handle(s: S)

}