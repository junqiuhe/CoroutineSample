package com.sample.coroutine

import com.sample.coroutine.utils.log
import kotlinx.coroutines.*


/**
 * 协程启动模式
 *
 * Default  --> 饿汉式： 一旦被启动，就会进入调度状态.
 *
 * Lazy  --> 不会立即进入调度状态，须调用 job.start() / job.join()
 *
 * Atomic  --> 与 协程的 cancel 有关，它会被立即启动，只是在启动时，无视协程的 cancel 状态，因此它一定会执行。
 *
 * Undispatched --> 会立即在当前线程执行，直到遇到协程体中的第一个挂起点.
 */
fun main() {

    runBlocking {
        log(1)

        val job = GlobalScope.launch {
            log(2)
            delay(1000)
            log(3)
        }
        log(4)
        job.join()
        log(5)
    }
}