package handlers

import java.util.concurrent.ExecutorService
import java.util.concurrent.FutureTask

class ExecutorServiceHandler<S>(
    other: Handler<S>,
    private val pool: ExecutorService
) :
    DecoratedHandler<S>(other) {

    override fun handle(s: S) {
        pool.submit(
            FutureTask {
                super.handle(s)
            }
        )
    }
}