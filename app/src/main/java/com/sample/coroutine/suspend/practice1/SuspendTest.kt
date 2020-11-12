package com.sample.coroutine.suspend.practice1

import com.sample.coroutine.utils.log
import java.lang.Thread.sleep
import kotlin.concurrent.thread
import kotlin.coroutines.intrinsics.*
import kotlin.coroutines.resume

/**
 * fun hello(continuation: Continuation<Int>): Any
 */
suspend fun hello() = suspendCoroutineUninterceptedOrReturn<Int> { continuation ->
    log(1)
    thread {
        sleep(2000)
        log(2)
        continuation.resume(1024)
    }
    log(3)
    COROUTINE_SUSPENDED
}

/**
 * 本质见 CallCoroutine1
 */
suspend fun main() {
    log(hello())

    CallCoroutine1.main(arrayOf())
}