package com.sample.coroutine

import com.sample.coroutine.utils.log
import kotlinx.coroutines.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

suspend fun main() {
    GlobalScope.launch(context = MyContinuationInterceptor()) {
        log(1)
        val job = async {
            log(2)
            delay(1000)
            log(3)
            "hello"
        }
        log(4)
        val result = job.await()
        log("5, result = $result")
    }.join()
    log(6)
}

/**
 * 简易实现拦截器.
 */
class MyContinuationInterceptor : ContinuationInterceptor {

    override val key: CoroutineContext.Key<*>
        get() = ContinuationInterceptor

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        MyContinuation(continuation)
}

class MyContinuation<T>(private val originContinuation: Continuation<T>) : Continuation<T> {

    override val context: CoroutineContext
        get() = originContinuation.context

    override fun resumeWith(result: Result<T>) {
        log("<MyContinuation> $result")
        originContinuation.resumeWith(result)
    }
}

