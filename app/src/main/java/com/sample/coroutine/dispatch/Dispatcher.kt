package com.sample.coroutine.dispatch

import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

interface Dispatcher {
    fun dispatch(block: () -> Unit)
}

/**
 * 调度器是基于拦截器的基础上实现的.
 */
class DispatchHandler(
    private val dispatcher: Dispatcher = DefaultDispatcher
) : ContinuationInterceptor {

    override val key: CoroutineContext.Key<*>
        get() = ContinuationInterceptor

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        DispatchContinuation(continuation, dispatcher)
}

class DispatchContinuation<T>(
    private val delegate: Continuation<T>,
    private val dispatcher: Dispatcher
) : Continuation<T> {
    override val context: CoroutineContext
        get() = delegate.context

    override fun resumeWith(result: Result<T>) {
        dispatcher.dispatch {
            delegate.resumeWith(result)
        }
    }
}