package com.sample.coroutine.suspend.practice3

import com.sample.coroutine.User
import com.sample.coroutine.utils.log
import java.lang.RuntimeException
import java.lang.Thread.sleep
import kotlin.concurrent.thread
import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.*

internal interface AsyncScope<T> {
    suspend fun await(block: () -> T): T
}

internal class AsyncHandler<T>(
    block: suspend AsyncScope<T>.() -> Unit
) : AsyncScope<T>, Continuation<Any?> {

    init {
        val start = block.createCoroutine(this, this)
        start.resume(Unit)
    }

    override suspend fun await(block: () -> T): T = suspendCoroutine {
        thread {
            try {
                it.resume(block.invoke())
            } catch (e: Exception) {
                it.resumeWithException(e)
            }
        }
    }

    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<Any?>) {
        log("执行完毕....")
    }
}

internal fun <T> async(block: suspend AsyncScope<T>.() -> Unit) = AsyncHandler(block)

fun main() {
//    Looper.prepare()
//    val handlerDispatcher = DispatcherContext(object : Dispatcher {
//        val handler = Handler()
//        override fun dispatch(block: () -> Unit) {
//            handler.post(block)
//        }
//    })
//
//    async(handlerDispatcher) {
//        val user = await { githubApi.getUserCallback("bennyhuo") }
//        log(user)
//    }
//
//    Looper.loop()

    async<User> {
        try {
            val user = await {
                sleep(4000)
                User("Zhang", "San")
                throw RuntimeException("hahahahah....")
            }
            log(user)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
