package com.sample.coroutine

/**
 * 伴生对象
 */
class CompanionObject {

    companion object {
        fun create(): CompanionObject = CompanionObject()
    }

    /**
     * 可对伴生对象指定名称.
     */
//    companion object Factory {
//        fun create(): CompanionObject = CompanionObject()
//    }
}

fun main() {

    /**
     * 仅能通过该方式获取伴生对象的成员
     */
    CompanionObject.create()


    /**
     * 子身所用的名称（CompanionObject）可用作对该类伴生对象的应用
     */
    val x = CompanionObject
    val y = CompanionObject.Companion
    println(x == y)  //true

    val z: Factory<MyClass> = MyClass
    println(z)
}

/**
 * 注意，伴生对象的成员看起来像其它语言的静态成员，在运行时他们仍然是真实对象的实例成员，而且，还可以实现接口.
 */
interface Factory<T> {
    fun create(): T
}
class MyClass {
    companion object : Factory<MyClass> {
        override fun create(): MyClass = MyClass()
    }
}