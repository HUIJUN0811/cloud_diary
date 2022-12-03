package com.msbjy.note.vo;

public class ResultInfo<T> {
    private Integer code;
    private  String msg;
    private  T result;


    public ResultInfo() {
    }

    public ResultInfo(Integer code, String msg, T result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    /**
     * 获取
     * @return code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置
     * @param code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取
     * @return result
     */
    public T getResult() {
        return result;
    }

    /**
     * 设置
     * @param result
     */
    public void setResult(T result) {
        this.result = result;
    }

    public String toString() {
        return "ResultInfo{code = " + code + ", msg = " + msg + ", result = " + result + "}";
    }
}
