package com.aliyun.dingtalk.service.esign.impl;

import com.aliyun.dingtalk.config.AppConfig;
import com.aliyun.dingtalk.exception.InvokeDingTalkException;
import com.aliyun.dingtalk.service.esign.ESignService;
import com.aliyun.dingtalk.util.AccessTokenUtil;
import com.aliyun.dingtalkesign_2_0.Client;
import com.aliyun.dingtalkesign_2_0.models.CreateDevelopersHeaders;
import com.aliyun.dingtalkesign_2_0.models.CreateDevelopersRequest;
import com.aliyun.dingtalkesign_2_0.models.CreateDevelopersResponse;
import com.aliyun.dingtalkesign_2_0.models.CreateDevelopersResponseBody;
import com.aliyun.dingtalkesign_2_0.models.GetAuthUrlHeaders;
import com.aliyun.dingtalkesign_2_0.models.GetAuthUrlRequest;
import com.aliyun.dingtalkesign_2_0.models.GetAuthUrlResponse;
import com.aliyun.dingtalkesign_2_0.models.GetAuthUrlResponseBody;
import com.aliyun.dingtalkesign_2_0.models.GetCorpConsoleHeaders;
import com.aliyun.dingtalkesign_2_0.models.GetCorpConsoleResponse;
import com.aliyun.dingtalkesign_2_0.models.GetCorpConsoleResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ESignServiceImpl implements ESignService {

    @Autowired
    private AppConfig appConfig;

    private Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }

    @Override
    public Boolean initData(String authCorpId, String noticeUrl) {
        try {

            Client client = createClient();
            CreateDevelopersHeaders createDevelopersHeaders = new CreateDevelopersHeaders();
            createDevelopersHeaders.xAcsDingtalkAccessToken = AccessTokenUtil.getCorpAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret(), authCorpId);
            CreateDevelopersRequest createDevelopersRequest = new CreateDevelopersRequest()
                    .setNoticeUrl(noticeUrl);
            CreateDevelopersResponse developersWithOptions = client.createDevelopersWithOptions(createDevelopersRequest, createDevelopersHeaders, new RuntimeOptions());
            CreateDevelopersResponseBody body = developersWithOptions.getBody();
            return body.data;

        } catch (TeaException e) {
            e.printStackTrace();
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }

    /**
     * 使用 Token 初始化账号Client
     *
     * @return Client
     * @throws Exception
     */
    private static Client createESignClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }


    /**
     * 获取e签宝企业控制台
     *
     * @return
     */
    @Override
    public String getESignConsole(String authCorpId) {
        try {
            Client client = createESignClient();
            GetCorpConsoleHeaders getCorpConsoleHeaders = new GetCorpConsoleHeaders();
            getCorpConsoleHeaders.xAcsDingtalkAccessToken = AccessTokenUtil.getCorpAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret(), authCorpId);
            GetCorpConsoleResponse corpConsoleWithOptions = client.getCorpConsoleWithOptions(getCorpConsoleHeaders, new RuntimeOptions());
            GetCorpConsoleResponseBody body = corpConsoleWithOptions.getBody();
            return body.getOrgConsoleUrl();
        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getESignConsole fail！");
        }
    }

    /**
     * 获取授权的页面地址
     * @return
     */
    public GetAuthUrlResponseBody getAuthUrl(String authCorpId) {

        try {
            Client client = createESignClient();
            GetAuthUrlHeaders getAuthUrlHeaders = new GetAuthUrlHeaders();
            getAuthUrlHeaders.xAcsDingtalkAccessToken = AccessTokenUtil.getCorpAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret(), authCorpId);;
            GetAuthUrlRequest getAuthUrlRequest = new GetAuthUrlRequest()
                    .setRedirectUrl("http://wanzq.vaiwan.com/index.html");
            GetAuthUrlResponse authUrlResponse = client.getAuthUrlWithOptions(getAuthUrlRequest, getAuthUrlHeaders, new RuntimeOptions());
            return authUrlResponse.getBody();
        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getESignAuthUrl fail！");
        }
    }


}
