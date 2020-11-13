package com.sample.coroutine.suspend.practice3

import com.sample.coroutine.utils.log
import kotlin.coroutines.*


/**
 * 利用协程仿写 python 序列生成器 ---> 仔细琢磨
 */

internal interface Generator<T> {
    fun iterator(): Iterator<T>
}

internal class GeneratorImpl<T>(
    private val initValue: T,
    private val block: suspend GeneratorScope<T>.(T) -> Unit
) : Generator<T> {
    override fun iterator(): Iterator<T> = GeneratorIterator(initValue, block)
}

/**
 * 作用域...
 */
internal interface GeneratorScope<T> {
    suspend fun yield(value: T)
}

/**
 * 迭代器的状态.
 */
sealed class State {
    class NotReady(val continuation: Continuation<Unit>) : State()
    class Ready<T>(val continuation: Continuation<Unit>, val value: T) : State()
    object Done : State()
}

internal class GeneratorIterator<T>(
    private val initValue: T,
    private val block: suspend GeneratorScope<T>.(T) -> Unit
) : Iterator<T>, Continuation<Any?>, GeneratorScope<T> {

    private var state: State

    init {
        val startCoroutineBlock: suspend GeneratorScope<T>.() -> Unit = {
            block.invoke(this, initValue)
        }
        val start = startCoroutineBlock.createCoroutine(this, this)
        state = State.NotReady(start)
    }

    override fun hasNext(): Boolean {
        resume()
        return state != State.Done
    }

    private fun resume() {
        when (val currentState = state) {
            is State.NotReady -> currentState.continuation.resume(Unit)
        }
    }

    override fun next(): T {
        return when (val currentState = state) {
            is State.NotReady -> {
                resume()
                return next()
            }
            is State.Ready<*> -> {
                state = State.NotReady(currentState.continuation)
                (currentState as State.Ready<T>).value
            }
            State.Done -> throw IndexOutOfBoundsException("No value left.")
        }
    }

    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<Any?>) {
        log("执行完毕...")
        state = State.Done
        result.getOrThrow()
    }

    override suspend fun yield(value: T) = suspendCoroutine<Unit> { continuation ->
        state = when (state) {
            is State.NotReady -> State.Ready(continuation, value)
            is State.Ready<*> -> throw IllegalStateException("Cannot yield a value while ready.")
            State.Done -> throw IllegalStateException("Cannot yield a value while done.")
        }
    }
}

internal fun <T> generator(block: suspend GeneratorScope<T>.(T) -> Unit): (T) -> Generator<T> {
    return { startValue ->
        GeneratorImpl(startValue, block)
    }
}

fun main() {
    val num = generator<Int> { initValue ->
        println("initValue : $initValue")

        yield(1)
        yield(2)
        yield(3)
        yield(4)
        yield(5)
    }

    val iterator = num(10).iterator()
    while (iterator.hasNext()) {
        val value = iterator.next()
        println(value)
    }
}