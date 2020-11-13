package com.sample.coroutine.suspend.practice3

import com.sample.coroutine.utils.log

fun main() {

    val fibonacci = sequence<Int> {
        yield(1)
        var cur = 1
        var next = 1
        while (true) {
            yield(next)
            val temp = cur + next
            cur = next
            next = temp
        }
    }


//    fibonacci.take(5).forEach(::log)

    val iterator = fibonacci.take(5).iterator()
    while (iterator.hasNext()) {
        val value = iterator.next()
        log(value)
    }
}