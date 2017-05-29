package com.team12.navaait.rest.model;

/**
 * Created by Sam on 5/29/2017.
 */

public class RestError {

    private Integer code;
    private String strMessage;

    public RestError(String strMessage) {
        this.strMessage = strMessage;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }
}
