package com.sample.coroutine.dispatch

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

object DefaultDispatcher : Dispatcher {

    private val threadGroup = ThreadGroup("DefaultDispatcher")

    private val threadIndex = AtomicInteger(0)

    private val executor: ExecutorService

    init {
        Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors()) { runnable ->
            val threadName = "${threadGroup.name}-worker-${threadIndex.getAndIncrement()}"
            Thread(threadGroup, runnable, threadName)

        }.let {
            executor = it
        }
    }

    override fun dispatch(block: () -> Unit) {
        executor.execute {
            block.invoke()
        }
    }
}