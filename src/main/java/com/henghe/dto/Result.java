package com.henghe.dto;

import java.util.List;

public class Result<T>{
    //200-success 500-false
    private int code;
    //错误打印信息
    private String msg = null;
    //当前页数，总条数，总页数
    private Integer page = null;
    private Integer totalNum= null;
    private Integer totalPage= null;
    //请求数据
    private List<T> datas = null;

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, List<T> datas) {
        this.code = code;
        this.datas = datas;
    }

    public Result(int code, Integer page, Integer totalNum, Integer totalPage, List<T> datas) {
        this.code = code;
        this.page = page;
        this.totalNum = totalNum;
        this.totalPage = totalPage;
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", page=" + page +
                ", totalNum=" + totalNum +
                ", totalPage=" + totalPage +
                ", datas=" + datas +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
