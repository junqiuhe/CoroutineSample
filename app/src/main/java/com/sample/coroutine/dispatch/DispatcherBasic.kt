package com.sample.coroutine.dispatch

import com.sample.coroutine.User
import com.sample.coroutine.utils.log
import kotlinx.coroutines.*
import java.lang.Thread.sleep
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 调度器是基于拦截器实现的
 */
suspend fun main() {
    GlobalScope.launch(context = DispatchHandler()) {
        log(1)

        delay(1000)
        log(2)

        val user = getUser()
        log("user --> $user")

    }.join()
}

/**
 * suspendCoroutine 并不是启动协程，而是获取当前协程的 Continuation 实例.
 */
suspend fun getUser(): User = suspendCoroutine { continuation ->
    thread {
        sleep(5000)
        User("li", "si").let {
            continuation.resume(it)
        }
    }
}