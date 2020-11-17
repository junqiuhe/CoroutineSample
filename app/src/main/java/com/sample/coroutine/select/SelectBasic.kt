package com.sample.coroutine.select

import com.sample.coroutine.User
import com.sample.coroutine.utils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onReceiveOrNull
import kotlinx.coroutines.selects.select

fun CoroutineScope.getUserFromService() = async {
    delay(2000)
    User("zhang", "san")
}

fun CoroutineScope.getUserFromLocal() = async {
    User("li", "si")
}

data class Response(val user: User, val isServer: Boolean)


/**
 * 想要确认挂起函数是否支持 select，只需要查看其是否存在对应的 SelectClauseN 即可。
 */
fun main() {
//    selectAwait()
    selectJob()
}

/**
 * 复用多个 Await.
 */
fun selectAwait() = runBlocking {
    val serverDeferred = getUserFromService()
    val localDeferred = getUserFromLocal()

    //复用多个 Await.
    val response = select<Response> {

        serverDeferred.onAwait {
            Response(it, true)
        }

        localDeferred.onAwait.invoke {
            Response(it, false)
        }
    }
    log(response)

    response.isServer.takeUnless { it }?.let {
        val userFromServer = serverDeferred.await()
        //cache user

        log(userFromServer)

    }
}


/**
 * 复用多个 Channel.
 */
fun selectChannel() = runBlocking {

    val channels = List(10) { Channel<Int>() }

    val result = select<Int> {
        channels.forEach { channel ->
            channel.onReceive {
                it
            }
        }
    }

    log("result $result")
}

/**
 * 复用多个 Job
 */
fun selectJob() = runBlocking {
    select<Unit> {
        List(5) { index ->
            GlobalScope.async {
                delay((index + 1).toLong() * 100)
                index
            }
        }.forEach { job ->
            job.onAwait {
                log("onSelect --> $it")
            }
        }
    }
}