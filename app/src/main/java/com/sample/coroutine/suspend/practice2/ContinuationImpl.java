package com.sample.coroutine.suspend.practice2;

import com.sample.coroutine.utils.LogKt;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlinx.coroutines.DelayKt;

public class ContinuationImpl implements Continuation<Object> {

    private int label = 0;

    private final Continuation<Unit> completion;

    public ContinuationImpl(Continuation<Unit> completion) {
        this.completion = completion;
    }

    @NotNull
    @Override
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }

    @Override
    public void resumeWith(@NotNull Object o) {
        try {
            Object result = null;
            switch (label) {
                case 0: {
                    LogKt.log(1);
                    result = SuspendFunctionKt.returnSuspended(this);
                    label++;
                    if (isSuspend(result)) return;
                }
                case 1: {
                    LogKt.log(o);
                    LogKt.log(2);
                    result = DelayKt.delay(2000, this);
                    label++;
                    if (isSuspend(result)) return;
                }
                case 2: {
                    LogKt.log(3);
                    result = SuspendFunctionKt.returnImmediately(this);
                    label++;
                    if (isSuspend(result)) return;
                }
                case 3: {
                    LogKt.log(result);
                    LogKt.log(4);
                }
            }
            completion.resumeWith(Unit.INSTANCE);
        } catch (Exception e) {
            completion.resumeWith(e);
        }
    }

    private boolean isSuspend(Object result) {
        return IntrinsicsKt.getCOROUTINE_SUSPENDED() == result;
    }
}

