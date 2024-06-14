package com.gaoh.redissontask.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaoh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResult<T> {
    private int code;
    private boolean status;
    private String msg;
    private T resultData;

    private long timestamp;

    public static RestResult<String> ok() {
        return ok("success");
    }

    public static RestResult<String> ok(String msg) {
        return new RestResult<>(200, true, msg, null, System.currentTimeMillis());
    }


    public static RestResult<String> error(String err) {
        return new RestResult<>(500, false, err, null, System.currentTimeMillis());
    }

    public static RestResult<String> error() {
        return error("error");
    }
}
