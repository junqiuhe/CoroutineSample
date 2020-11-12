package com.sample.coroutine.suspend

import com.sample.coroutine.User
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * 理解挂起函数
 */
suspend fun main() {
}

/**
 * 该挂起函数的类型为 suspend () -> User
 *
 * 转换成普通的函数为:
 * fun getUser(continuation: Continuation<User>): Any {
 * }
 *
 * 挂起函数的返回值，变成了 Continuation 中的元素。 Any则有两种值.  真正的值（不是真正的挂起） / COROUTINE_SUSPENDED （真正的挂起）
 *
 */
suspend fun getUser(): User {
    return User("zhang", "San")
}

/**
 * suspendCoroutine、suspendCancellableCoroutine 底层都是调用了 suspendCoroutineUninterceptedOrReturn
 *
 * 以下三个函数都是为了获取挂起函数中隐式的 Continuation，目的是可以将回调转换成挂起函数
 * suspendCoroutine
 * suspendCancellableCoroutine
 * suspendCoroutineUninterceptedOrReturn
 */
suspend fun getUserCoroutine() = suspendCoroutine<User> {
    it.resume(User("zhang", "San"))
}
suspend fun getUserCoroutine1() = suspendCancellableCoroutine<User> {
    it.resume(User("zhang", "San"))
}