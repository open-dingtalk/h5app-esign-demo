package com.aliyun.dingtalk.controller;

import com.aliyun.dingtalk.model.ESignCallBackVO;
import com.aliyun.dingtalk.model.ServiceResult;
import com.aliyun.dingtalk.service.esign.ESignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ESignController {

    @Autowired
    private ESignService eSignService;

    /**
     * 服务商数据初始化
     * noticeUrl为e签宝回调服务端接口，不支持接口修改该参数，只能联系e签宝手动重置
     *
     * @param authCorpId
     * @param noticeUrl
     * @return
     */
    @PostMapping("/esign/corp")
    public ServiceResult initData(@RequestParam String authCorpId, @RequestParam(required = false) String noticeUrl) {

        return ServiceResult.getSuccessResult(eSignService.initData(authCorpId, noticeUrl));
    }

    /**
     * 获取企业控制台地址,只支持PC端
     *
     * @param authCorpId
     * @return
     */
    @GetMapping("/esign/corp/console")
    public ServiceResult getESignConsole(@RequestParam String authCorpId) {
        return ServiceResult.getSuccessResult(eSignService.getESignConsole(authCorpId));
    }

    /**
     * 获取企业授权url，可以在pc浏览器或者钉钉移动端打开e签宝微应用进行授权操作
     *
     * @param authCorpId
     * @param redirectUrl
     * @return
     */
    @GetMapping("/esign/corp/auth")
    public ServiceResult getESignAuthUrl(@RequestParam String authCorpId, @RequestParam(required = false) String redirectUrl) {
        return ServiceResult.getSuccessResult(eSignService.getAuthUrl(authCorpId, redirectUrl));
    }

    /**
     * 取消企业授权
     *
     * @param authCorpId
     * @return
     */
    @PutMapping("/esign/corp/auth")
    public ServiceResult deAuthorize(@RequestParam String authCorpId) {
        return ServiceResult.getSuccessResult(eSignService.deAuthorize(authCorpId));
    }

    /**
     * 获取文件详情
     *
     * @param fileId
     * @param authCorpId
     * @return
     */
    @GetMapping("/esign/corp/upload-file")
    public ServiceResult getESignUploadFile(@RequestParam String fileId, @RequestParam String authCorpId) {
        return ServiceResult.getSuccessResult(eSignService.getUploadFile(fileId, authCorpId));
    }

    /**
     * 获取创建签署流程url，可以在pc浏览器或者钉钉移动端打开进入e签宝微应用发起签署流程
     *
     * @param map
     * @return
     */
    @PostMapping("/esign/corp/process/url")
    public ServiceResult processStartUrl(@RequestBody Map<String, String> map) {
        String authCorpId = map.get("authCorpId");
        String userId = map.get("userId");
        String fileName = map.get("fileName");
        String fileId = map.get("fileId");
        return ServiceResult.getSuccessResult(eSignService.processStartUrl(authCorpId, userId, fileName, fileId));
    }

    /**
     * 创建签署流程
     *
     * @param map
     * @return
     */
    @PostMapping("/esign/corp/process")
    public ServiceResult processStart(@RequestBody Map<String, String> map) {
        String authCorpId = map.get("authCorpId");
        String userId = map.get("userId");
        String fileName = map.get("fileName");
        String fileId = map.get("fileId");
        return ServiceResult.getSuccessResult(eSignService.processStart(authCorpId, userId, fileName, fileId));
    }


    @PostMapping("/esign/corp/callback")
    public ServiceResult<Map<String, String>> callback(@RequestBody ESignCallBackVO eSignCallBackVO) {

        return ServiceResult.getSuccessResult(eSignService.callback(eSignCallBackVO));
    }

    /**
     * 获取企业e签宝微应用状态
     *
     * @param authCorpId
     * @return
     */
    @GetMapping("/esign/corp/status")
    public ServiceResult getESignStatus(@RequestParam String authCorpId) {

        return ServiceResult.getSuccessResult(eSignService.getESignCorpStatus(authCorpId));
    }

    /**
     * 获取签署人签署地址，在pc浏览器或者钉钉移动端打开e签宝微应用签署印章
     *
     * @param authCorpId
     * @param taskId
     * @param account
     * @return
     */
    @GetMapping("/esign/corp/process/execute-urls")
    public ServiceResult getProcessExecuteUrls(@RequestParam String authCorpId, @RequestParam String taskId, @RequestParam String account) {

        return ServiceResult.getSuccessResult(eSignService.getProcessExecuteUrls(authCorpId, taskId, account));
    }

    /**
     * 获取签署流程所有合同列表，通过返回的fileUrl下载签署过后的合同文件
     *
     * @param authCorpId
     * @param taskId
     * @return
     */
    @GetMapping("/esign/corp/process/{taskId}/docs")
    public ServiceResult getProcessFiles(@RequestParam String authCorpId, @PathVariable String taskId) {

        return ServiceResult.getSuccessResult(eSignService.getProcessFiles(authCorpId, taskId));
    }

    /**
     * 上传文件
     * @param authCorpId
     * @return
     */
    @PostMapping("/esign/corp/file")
    public ServiceResult uploadFile(@RequestParam String authCorpId) {

        return ServiceResult.getSuccessResult(eSignService.uploadFile(authCorpId));
    }

}
