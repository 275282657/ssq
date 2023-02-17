package com.ssq.vo;

import lombok.Data;

import java.util.List;

@Data
public class ResultBean {

    private String state;
    private String message;
    private String Tflag;
    private List<SsqVo> result;
}
