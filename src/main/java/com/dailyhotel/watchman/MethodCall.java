package com.dailyhotel.watchman;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tywin on 11/11/2016.
 */
@Accessors(chain = true)
@EqualsAndHashCode()
@ToString(callSuper = true, includeFieldNames = true)
public class MethodCall implements Serializable {
    @Getter
    @Setter
    private String signature;

    @Getter
    @Setter
    @Singular
    private List<Object> arguments;

    @Getter
    @Setter
    private Long ttlInSecond;

    @Getter
    @Setter
    private Integer thredshold;

    @Getter
    @Setter
    private Integer count;
}
