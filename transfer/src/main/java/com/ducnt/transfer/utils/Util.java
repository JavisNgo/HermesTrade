package com.ducnt.transfer.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class Util {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final int COUNTER_LENGTH = 5;

    public Long getEpochTimeStamp() {
        return System.currentTimeMillis() / 1000L;
    }

    public String generateExternalRef(Long timestamp) {
        int currentCounter = counter.getAndIncrement();

        Long epochTimeStamp = getEpochTimeStamp();

        if(timestamp < epochTimeStamp) {
            counter.set(1);
            currentCounter = 1;
        }

        if(currentCounter < Math.pow(10, COUNTER_LENGTH) - 1) {
            counter.set(1);
            currentCounter = 1;
        }

        String formattedCounter = String.format("%0" + COUNTER_LENGTH + "d", currentCounter);

        return getEpochTimeStamp() + formattedCounter;
    }

}
