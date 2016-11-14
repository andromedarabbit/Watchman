package com.dailyhotel.watchman.exception;

import com.dailyhotel.watchman.MethodCall;

/**
 * Created by Keaton on 4/26/16.
 */
public class DuplicateDectectedException extends RuntimeException {

    private final MethodCall record;

    public DuplicateDectectedException(MethodCall record) {
        super("Duplicate method call detected!");
        this.record = record;
    }

    public final MethodCall getMethodCall() {
        return record;
    }
}

