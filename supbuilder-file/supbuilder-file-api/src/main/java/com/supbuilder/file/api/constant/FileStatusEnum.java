package com.supbuilder.file.api.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileStatusEnum {


    /**
     * 处理失败
     */
    FAIL("0", "fail"),

    /**
     * 正在处理
     */
    ING("2", "ing"),

    /**
     * 处理成功
     */
    SUCCESS("1", "success");

    /**
     * 类型
     */
    private final String type;

    /**
     * 描述
     */
    private final String description;
}
