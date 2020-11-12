package com.sample.coroutine

import com.sample.coroutine.utils.log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import java.lang.RuntimeException

/**
 * https://www.bennyhuo.com/2019/04/23/coroutine-exceptions/
 *
 * 设置异常捕获器
 */
suspend fun main() {
//    threadUncaught()

    coroutineUncaught()
}


/**
 * 为线程设置未处理异常捕获器.
 */
fun threadUncaught() {
    Thread.setDefaultUncaughtExceptionHandler { t, e ->
        log("threadName: ${t.name} , exception msg: ${e.message}")
    }
    throw RuntimeException("runtime exception")
}

/**
 * 为指定的协程设置未处理异常捕获器.
 */
suspend fun coroutineUncaught() {
    val coroutineUncaughtExceptionHandler = CoroutineExceptionHandler { _, e ->
        log("coroutine exception msg: ${e.message}")
    }

    log(1)
    GlobalScope.launch(context = coroutineUncaughtExceptionHandler) {
        throw RuntimeException("coroutine runtime exception")
    }.join()
    log(2)
}

// 协程作用域 GlobalScope、coroutineScope、supervisorScope
// Cancel 操作.