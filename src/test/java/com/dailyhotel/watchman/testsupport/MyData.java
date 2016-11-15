package com.dailyhotel.watchman.testsupport;

import com.dailyhotel.watchman.DuplicateDetection;
import org.springframework.stereotype.Component;

/**
 * Created by tywin on 14/11/2016.
 */
@Component
public class MyData {
    private String name = "This is my name!";

    @DuplicateDetection
    public String getName() {
        return name;
    }

    @DuplicateDetection
    public String setName(String name) {
        return this.name = name;
    }
}
