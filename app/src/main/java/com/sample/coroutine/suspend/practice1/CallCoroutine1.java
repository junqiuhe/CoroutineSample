package com.sample.coroutine.suspend.practice1;

import com.sample.coroutine.utils.LogKt;

import org.jetbrains.annotations.NotNull;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.*;

public class CallCoroutine1 {

    public static void main(String[] args) {

        Object obj = SuspendTestKt.hello(new Continuation<Integer>() {
            @NotNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NotNull Object o) {
                if (o instanceof Integer) {
                    handleResult((Integer) o);
                } else {
                    Throwable throwable = (Throwable) o;
                    throwable.printStackTrace();
                }
            }
        });

        if (IntrinsicsKt.getCOROUTINE_SUSPENDED() == obj) {
            LogKt.log("suspend....");
        } else {
            handleResult((Integer) obj);
        }
    }

    private static void handleResult(Integer result) {
        LogKt.log("result : " + result);
    }
}
