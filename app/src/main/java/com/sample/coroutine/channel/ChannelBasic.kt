package com.sample.coroutine.channel

import com.sample.coroutine.utils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select


/**
通道 --> 提供了一种便捷的方法使单个值在多个协程间进行相互传输

类比 BlockingQueue

 */

/**
 * https://www.bennyhuo.com/2019/09/16/coroutine-channel/#more
 */
suspend fun main() {
//    channelBasic()

//    channelClose()

    broadCastChannel()
}

private suspend fun channelBasic() {
    /**
     * Channel 是生产者消费者模式在协程上的实现.
     */
    val channel = Channel<Int>()

    val producer = GlobalScope.launch {
        var i = 0
        while (true) {
            i++
            log("producer: before : $i")
            channel.send(i)
            log("producer: after : $i")
            delay(1000)
        }
    }

    val consumer = GlobalScope.launch {
        while (true) {
            val result = channel.receive()
            log("consumer: result : $result")
            delay(2000)
        }
    }

    producer.join()
    consumer.join()

    //还处于试验阶段...
//    val receiveChannel = GlobalScope.produce {
//        send(1)
//    }
//
//    val sendChannel = GlobalScope.actor<Int> {
//        val result = receive()
//        log("result $result")
//    }
}

/**

Channel 的 close 是一个协作机制。

调用了 close. isClosedForSend = true，对于接受方来讲，它需要等待所有数据接受完时， isClosedForReceive = true

Channel 的关闭 建议由主导方进行关闭。

 */
private suspend fun channelClose() {
    val channel = Channel<Int>(3)

    val producer = GlobalScope.launch {
        (1..5).forEach {
            channel.send(it)
            log("send $it")
        }
        channel.close()
        log("close channel. closedForSend: ${channel.isClosedForSend} , closedForReceive : ${channel.isClosedForReceive}")
    }

    val consumer = GlobalScope.launch {
        /**
         * 迭代 Channel
         */
        for (ele in channel) {
            log("receive $ele")
            delay(1000)
        }
        log("after consume. closedForSend: ${channel.isClosedForSend} , closedForReceive : ${channel.isClosedForReceive}")
    }

    producer.join()
    consumer.join()
}

/**

 BroadcastChannel

有多个端时，他们不是互斥的

 */
suspend fun broadCastChannel() {
//    val broadcastChannel = Channel<Int>().broadcast()

    val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)

    /**
     * 一个协程发送.
     */
    val producer = GlobalScope.launch {
        (0..5).forEach {
            broadcastChannel.send(it)
            log("send $it")
        }
        broadcastChannel.close()
    }

    /**
     * 三个消费者协程订阅。。
     */
    List(3) { index ->
        GlobalScope.launch {
            val receiveChannel = broadcastChannel.openSubscription()
            for (ele in receiveChannel) {
                log("[$index] receive: $ele")
                delay(1000)
            }
        }
    }.joinAll()

    producer.join()
}