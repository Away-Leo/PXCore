package com.pingxundata.pxcore.metadata.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回结果类
 */
public class RequestResult {

    /**
     * 返回状态
     */
    private boolean success=false;

    /**
     * 返回码
     */
    private String code="";

    /**
     * 返回信息
     */
    private String message="网络请求出错";

    /**
     * 返回集合
     */
    private List<?> resultList=new ArrayList<>();

    /**
     * 总记录数
     */
    private int totalElements=0;

    /**
     * 总页数
     */
    private int totalPages=0;

    /**
     * 当前页数
     */
    private int number=0;

    /**
     * 每页数据量
     */
    private int size=0;

    /**
     * 实体对象返回值
     */
    private Object entityResult;

    public Object getEntityResult() {
        return entityResult;
    }

    public void setEntityResult(Object entityResult) {
        this.entityResult = entityResult;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getResultList() {
        return resultList;
    }

    public void setResultList(List<?> resultList) {
        this.resultList = resultList;
    }
}
