package com.sample.coroutine.suspend.practice2;

import kotlin.Unit;

public class CallCoroutine2 {

    public static void main(String[] args) throws InterruptedException {
        RunSuspend completion = new RunSuspend();
        ContinuationImpl coroutine = new ContinuationImpl(completion);
        coroutine.resumeWith(Unit.INSTANCE);
        completion.await();
    }
}
