package com.sample.coroutine

import com.sample.coroutine.utils.log
import kotlinx.coroutines.*
import java.lang.Thread.sleep
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 调度器是基于拦截器实现的
 */
suspend fun main() {

    val user = GlobalScope.async(context = Dispatchers.Default) {
        log(1)

        delay(1000)
        log(2)

        val user = getUser()
        log(3)

        user

    }.await()

    log("4, result: $user")
}

/**
 * suspendCoroutine 并不是启动协程，而是获取当前协程的 Continuation 实例.
 */
suspend fun getUser(): User = suspendCoroutine {
    val user = getUserFromApi()
    it.resume(user)
}

private fun getUserFromApi(): User {
    sleep(2000)
    return User("zhang", "san")
}