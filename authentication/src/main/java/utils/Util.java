package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {
    public Long getEpochTimeStamp() {
        return System.currentTimeMillis() / 1000L;
    }
}
