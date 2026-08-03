package com.shuzijun.lc;

public interface CancellationToken {

    CancellationToken NONE = new CancellationToken() {
        @Override
        public boolean isCancellationRequested() {
            return false;
        }
    };

    boolean isCancellationRequested();
}
