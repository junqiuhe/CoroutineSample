package com.sample.coroutine.suspend.practice2

import com.sample.coroutine.utils.log
import kotlinx.coroutines.delay
import java.lang.Thread.sleep
import kotlin.concurrent.thread
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.resume

/**
 * 真正的挂起
 */
suspend fun returnSuspended() = suspendCoroutineUninterceptedOrReturn<String> { continuation ->
    thread {
        sleep(3000)
        continuation.resume("return suspend")
    }
    COROUTINE_SUSPENDED
}

/**
 * 非真正的挂起.
 */
suspend fun returnImmediately() = suspendCoroutineUninterceptedOrReturn<String> { _ ->
    "return immediately"
}

/**
 * 本质见 CallCoroutine2
 */
suspend fun main() {
//    log(1)
//    log(returnSuspended())
//    log(2)
//    delay(3000)
//    log(3)
//    log(returnImmediately())
//    log(4)

    CallCoroutine2.main(arrayOf())
}