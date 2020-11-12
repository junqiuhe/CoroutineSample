package com.sample.coroutine.suspend.practice2;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

/**
 * suspend fun main
 */
public class RunSuspend implements Continuation<Unit> {

    private Object result;

    @NotNull
    @Override
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }

    @Override
    public void resumeWith(@NotNull Object o) {
        synchronized (this) {
            this.result = o;
            notifyAll();
        }
    }

    public void await() throws InterruptedException {
        synchronized (this) {
            while (true) {
                if (result != null) {
                    return;
                }
                wait();
            }
        }
    }
}
