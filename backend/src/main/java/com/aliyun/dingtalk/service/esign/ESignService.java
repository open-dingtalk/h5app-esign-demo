package com.aliyun.dingtalk.service.esign;

import com.aliyun.dingtalk.model.ESignCallBackVO;
import com.aliyun.dingtalkesign_2_0.models.CreateProcessResponseBody;
import com.aliyun.dingtalkesign_2_0.models.GetAuthUrlResponseBody;
import com.aliyun.dingtalkesign_2_0.models.GetExecuteUrlResponseBody;
import com.aliyun.dingtalkesign_2_0.models.GetFileInfoResponseBody;
import com.aliyun.dingtalkesign_2_0.models.GetFlowDocsResponseBody;
import com.aliyun.dingtalkesign_2_0.models.GetIsvStatusResponseBody;
import com.aliyun.dingtalkesign_2_0.models.ProcessStartResponseBody;

import java.util.Map;

public interface ESignService {

    Boolean initData(String authCorpId, String noticeUrl);

    String getESignConsole(String authCorpId);

    GetAuthUrlResponseBody getAuthUrl(String authCorpId, String redirectUrl);

    GetFileInfoResponseBody getUploadFile(String fileId, String authCorpId);

    ProcessStartResponseBody processStartUrl(String authCorpId, String userId, String fileName, String fileId);

    Map<String, String> callback(ESignCallBackVO eSignCallBackVO);

    Boolean deAuthorize(String authCorpId);

    GetIsvStatusResponseBody getESignCorpStatus(String authCorpId);

    GetExecuteUrlResponseBody getProcessExecuteUrls(String authCorpId, String taskId, String account);

    GetFlowDocsResponseBody getProcessFiles(String authCorpId, String taskId);

    CreateProcessResponseBody processStart(String authCorpId, String userId, String fileName, String fileId);

    Map<String, String> uploadFile(String authCorpId);
}
