package com.hezhaohui.agent.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    private Integer code;
    private Object data;
    private String msg;

    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
}
