package com.aliyun.dingtalk.service.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalk.config.AppConfig;
import com.aliyun.dingtalk.constant.UrlConstant;
import com.aliyun.dingtalk.exception.InvokeDingTalkException;
import com.aliyun.dingtalk.service.handler.EventHandler;
import com.aliyun.dingtalk.util.AccessTokenUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiServiceActivateSuiteRequest;
import com.dingtalk.api.request.OapiServiceGetPermanentCodeRequest;
import com.dingtalk.api.response.OapiServiceActivateSuiteResponse;
import com.dingtalk.api.response.OapiServiceGetPermanentCodeResponse;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 企业授权
 */
@Service
public class TmpAuthCodeEventHandler implements EventHandler {

    @Autowired
    private AppConfig appConfig;

    /**
     * 授权企业应用
     *
     * @param eventJson
     */
    @Override
    public void handler(JSONObject eventJson) {

        String suiteAccessToken = AccessTokenUtil.getSuiteAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret());


        OapiServiceGetPermanentCodeResponse permanentCode = getPermanentCode(eventJson, suiteAccessToken);

        activateSuite(permanentCode, suiteAccessToken);

    }

    /**
     * 激活应用
     * @param permanentCode
     * @param suiteAccessToken
     */
    private void activateSuite(OapiServiceGetPermanentCodeResponse permanentCode, String suiteAccessToken) {

        try {
            DingTalkClient client = new DefaultDingTalkClient(UrlConstant.ACTIVATE_SUITE_URL.replace("SUITE_ACCESS_TOKEN", suiteAccessToken));
            OapiServiceActivateSuiteRequest req = new OapiServiceActivateSuiteRequest();
            req.setSuiteKey(appConfig.getSuiteKey());
            req.setAuthCorpid(permanentCode.getAuthCorpInfo().getCorpid());
            req.setPermanentCode(permanentCode.getPermanentCode());
            OapiServiceActivateSuiteResponse rsp = client.execute(req);
            if (!rsp.isSuccess()) {
                throw new InvokeDingTalkException(rsp.getErrorCode(), rsp.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            throw new InvokeDingTalkException(e.getErrCode(), e.getErrMsg());
        }
    }

    /**
     * 获取授权企业的永久授权码
     * @param eventJson
     * @param suiteAccessToken
     * @return
     * @throws ApiException
     */
    private OapiServiceGetPermanentCodeResponse getPermanentCode(JSONObject eventJson, String suiteAccessToken) {
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.GET_PERMANENT_CODE_URL.replace("SUITE_ACCESS_TOKEN", suiteAccessToken));
        OapiServiceGetPermanentCodeRequest req = new OapiServiceGetPermanentCodeRequest();
        req.setTmpAuthCode(eventJson.getString("AuthCode"));
        try {
            OapiServiceGetPermanentCodeResponse rsp = client.execute(req);
            if (rsp.isSuccess()) {
                return rsp;
            } else {
                throw new InvokeDingTalkException(rsp.getErrorCode(), rsp.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            throw new InvokeDingTalkException(e.getErrCode(), e.getErrMsg());
        }

    }

}
