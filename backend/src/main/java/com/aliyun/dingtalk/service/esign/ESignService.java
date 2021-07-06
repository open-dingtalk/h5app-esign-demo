package com.aliyun.dingtalk.service.esign;

import com.aliyun.dingtalkesign_2_0.models.GetAuthUrlResponseBody;

public interface ESignService {

    Boolean initData(String authCorpId, String noticeUrl);

    String getESignConsole(String authCorpId);

    GetAuthUrlResponseBody getAuthUrl(String authCorpId);

}
