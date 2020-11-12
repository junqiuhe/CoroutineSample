package com.sample.coroutine

import com.sample.coroutine.utils.log
import kotlinx.coroutines.delay
import kotlin.coroutines.*


/**
 * suspend fun main的本质.
 */

fun main() {

    runSuspend {
        log(1)
        delay(5000)
    }
    log(2)

    /**
     * 协程启动的方式一
     */
    suspend {
    }.createCoroutine(object : Continuation<Unit> {

        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<Unit>) {
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
}

fun runSuspend(block: suspend () -> Unit) {
    val runSuspend = RunSuspend()
    block.createCoroutine(runSuspend).resume(Unit)
    runSuspend.await()
}

class RunSuspend : Continuation<Unit> {

    private val lock: Object = Object()

    private var result: Result<Unit>? = null

    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) = synchronized(lock) {
        this.result = result

        log("执行完毕.....")

        lock.notifyAll()
    }

    fun await(): Unit = synchronized(lock) {
        while (true) {
            when (val result = this.result) {
                null -> lock.wait()
                else -> {
                    result.getOrThrow() // throw up failure
                    return
                }
            }
        }
    }
}