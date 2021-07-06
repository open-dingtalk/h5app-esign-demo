package com.aliyun.dingtalk.controller;

import com.aliyun.dingtalk.model.ServiceResult;
import com.aliyun.dingtalk.service.esign.ESignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ESignController {

    @Autowired
    private ESignService eSignService;

    @PostMapping("/esign")
    public ServiceResult initData(@RequestParam String authCorpId, @RequestParam String noticeUrl) {


        return ServiceResult.getSuccessResult(eSignService.initData(authCorpId, noticeUrl));
    }

    @GetMapping("/esign/console")
    public ServiceResult getESignConsole(@RequestParam String authCorpId) {
        return ServiceResult.getSuccessResult(eSignService.getESignConsole(authCorpId));
    }

    @GetMapping("/esign/auth-url")
    public ServiceResult getESignAuthUrl(@RequestParam String authCorpId) {
        return ServiceResult.getSuccessResult(eSignService.getAuthUrl(authCorpId));
    }
}
