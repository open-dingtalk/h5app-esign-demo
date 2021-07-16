package com.aliyun.dingtalk.util;

import com.aliyun.dingtalk.constant.UrlConstant;
import com.aliyun.dingtalk.exception.InvokeDingTalkException;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiServiceGetCorpTokenRequest;
import com.dingtalk.api.request.OapiServiceGetSuiteTokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiServiceGetCorpTokenResponse;
import com.dingtalk.api.response.OapiServiceGetSuiteTokenResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * 获取access_token工具类
 */
@Slf4j
public class AccessTokenUtil {


    /**
     * 企业内部应用token
     * @param appKey
     * @param appSecret
     * @return
     * @throws RuntimeException
     */
    public static String getAccessToken(String appKey, String appSecret) throws RuntimeException {
        try {

            DefaultDingTalkClient client = new DefaultDingTalkClient(UrlConstant.GET_TOKEN_URL);
            OapiGettokenRequest request = new OapiGettokenRequest();

            request.setAppkey(appKey);
            request.setAppsecret(appSecret);
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            if (response.isSuccess()) {
                String accessToken = response.getAccessToken();
                return accessToken;
            } else {
                throw new InvokeDingTalkException(response.getErrorCode(), response.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            throw new InvokeDingTalkException(e.getErrCode(), e.getErrMsg());
        }

    }

    /**
     * 第三方企业token
     * @param suiteKey
     * @param suiteSecret
     * @return
     * @throws RuntimeException
     */
    public static String getSuiteAccessToken(String suiteKey, String suiteSecret) throws InvokeDingTalkException {
        try {
            String suiteTicket = getSuiteTicket();
            DingTalkClient client = new DefaultDingTalkClient(UrlConstant.SUITE_ACCESS_TOKEN_URL);
            OapiServiceGetSuiteTokenRequest req = new OapiServiceGetSuiteTokenRequest();
            req.setSuiteKey(suiteKey);
            req.setSuiteSecret(suiteSecret);
            req.setSuiteTicket(suiteTicket);
            OapiServiceGetSuiteTokenResponse response = client.execute(req);
            if (response.isSuccess()) {
                String accessToken = response.getSuiteAccessToken();
                return accessToken;
            } else {
                throw new InvokeDingTalkException(response.getErrorCode(), response.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            throw new InvokeDingTalkException(e.getErrCode(), e.getErrMsg());
        }

    }

    /**
     * 获取suiteTicket
     * @return
     */
    private static String getSuiteTicket() {

        Resource classPathResource = new ClassPathResource("application.properties");

        Properties properties = new Properties();

        try {
            properties.load(classPathResource.getInputStream());
            String property = properties.getProperty("dingtalk.suite_ticket");
            if (StringUtils.isNotBlank(property)) {
                return properties.getProperty("dingtalk.suite_ticket");
            } else {
                throw new RuntimeException("suiteTicket为空，需要重新在第三方企业应用->开发管理重新推送");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("getSuiteTicket fail！");
        }
    }



    /**
     * 授权企业的access_token
     * @param suiteKey
     * @param suiteSecret
     * @param authCorpId
     * @return
     * @throws RuntimeException
     */
    public static String getCorpAccessToken(String suiteKey, String suiteSecret, String authCorpId) throws RuntimeException {
        try {
            String suiteTicket = getSuiteTicket();
            DefaultDingTalkClient client = new DefaultDingTalkClient(UrlConstant.GET_CORP_ACCESS_TOKEN_URL);
            OapiServiceGetCorpTokenRequest req = new OapiServiceGetCorpTokenRequest();
            req.setAuthCorpid(authCorpId);
            OapiServiceGetCorpTokenResponse response = client.execute(req, suiteKey, suiteSecret, suiteTicket);
            if (response.isSuccess()) {
                return response.getAccessToken();

            } else {
                throw new InvokeDingTalkException(response.getErrorCode(), response.getErrmsg());
            }
        } catch (ApiException e) {
            e.printStackTrace();
            throw new InvokeDingTalkException(e.getErrCode(), e.getErrMsg());
        }

    }

}
