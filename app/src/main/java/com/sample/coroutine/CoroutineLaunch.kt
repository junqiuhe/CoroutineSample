package com.sample.coroutine

import com.sample.coroutine.utils.log
import kotlinx.coroutines.delay
import kotlin.coroutines.*


/**
 * suspend fun main的本质.
 */

fun main() {

    /**
     * 协程启动的方式一
     */
    suspend {
        1
    }.createCoroutine(object : Continuation<Int> {

        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            log("result --> ${result.getOrThrow()}")
        }
    }).resume(Unit)

    /**
     * 协程启动方式二
     */
    suspend {
    }.startCoroutine(object : Continuation<Unit> {

        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<Unit>) {
        }
    })

    callLaunchCoroutine()
}

fun callLaunchCoroutine() {
    launchCoroutine(ProducerScope<Int>()){
        log("In Coroutine...")
        produce(1024)
        delay(2000)
        produce(1024)
    }
}

class ProducerScope<T> {
    suspend fun produce(value: T) {
    }
}

/**
 * 创建协程接收一个 Receiver, 它的作用可以为协程体提供一个作用域
 * 即是在协程体内我们可以直接使用作用域内提供的函数或者状态等.
 */
fun <R, T> launchCoroutine(receiver: R, block: suspend R.() -> T) {
    block.startCoroutine(receiver, object : Continuation<T> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<T>) {
            log("Coroutine End: ${result.getOrNull()}")
        }
    })
}


