package com.aliyun.dingtalk.model;

import lombok.Data;

@Data
public class ESignCallBackVO {

    private String action;

    private String taskId;

    private BizData bizData;

    public class BizData {

        private String status;

        private Long finishTime;
    }
}
