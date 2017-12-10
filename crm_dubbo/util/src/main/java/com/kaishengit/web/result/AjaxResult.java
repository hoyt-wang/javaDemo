package com.kaishengit.web.result;

/**
 * ajax请求返回的结果，一般在控制器中使用
 * Created by hoyt on 2017/11/8.
 */

public class AjaxResult {

    public static final String STATE_SUCCESS = "success";
    public static final String STATE_ERROR = "error";

    public static AjaxResult success() {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setState(STATE_SUCCESS);
        return ajaxResult;
    }

    /*public static AjaxResult success(Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setState(AjaxResult.STATE_SUCCESS);
        ajaxResult.setData(data);
        return ajaxResult;
    }*/
    public static AjaxResult successWithData(Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setState(STATE_SUCCESS);
        ajaxResult.setData(data);
        return ajaxResult;
    }

    public static AjaxResult error(String message) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setState(STATE_ERROR);
        ajaxResult.setMessage(message);
        return ajaxResult;
    }
    private String state;
    private String messgae;
    private Object data;

    public String getState() {
        return state;
    }

    public AjaxResult(){}

    public AjaxResult(String state) {
        this.state = state;
    }

    public AjaxResult(String state,Object data) {
        this.state = state;
        this.data = data;
    }

    public AjaxResult(String state,String message) {
        this.state = state;
        this.messgae = message;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessgae() {
        return messgae;
    }

    public void setMessgae(String messgae) {
        this.messgae = messgae;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.messgae = message;
    }
}
