package com.aliyun.dingtalk.service.esign.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.dingtalk.config.AppConfig;
import com.aliyun.dingtalk.exception.InvokeDingTalkException;
import com.aliyun.dingtalk.model.ESignCallBackVO;
import com.aliyun.dingtalk.service.esign.ESignService;
import com.aliyun.dingtalk.util.AccessTokenUtil;
import com.aliyun.dingtalkesign_2_0.Client;
import com.aliyun.dingtalkesign_2_0.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
     * 获取e签宝企业控制台
     *
     * @return
     */
    @Override
    public String getESignConsole(String authCorpId) {
        try {
            Client client = createClient();
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
     *
     * @return
     */
    public GetAuthUrlResponseBody getAuthUrl(String authCorpId, String redirectUrl) {

        try {
            Client client = createClient();
            GetAuthUrlHeaders getAuthUrlHeaders = new GetAuthUrlHeaders();
            getAuthUrlHeaders.xAcsDingtalkAccessToken = AccessTokenUtil.getCorpAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret(), authCorpId);

            GetAuthUrlRequest getAuthUrlRequest = new GetAuthUrlRequest()
                    .setRedirectUrl(redirectUrl);
            GetAuthUrlResponse authUrlResponse = client.getAuthUrlWithOptions(getAuthUrlRequest, getAuthUrlHeaders, new RuntimeOptions());

            return authUrlResponse.getBody();
        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getESignAuthUrl fail！");
        }
    }

    /**
     * 获取授权的页面地址
     * demo中请求数据都是固定的，发起人和参与方为同一人、抄送人列表、参与方列表、文件列表等都可以从前端页面传入
     *
     * @return
     */
    public ProcessStartResponseBody processStartUrl(String authCorpId, String userId, String fileName, String fileId) {

        try {
            Client client = createClient();

            String corpAccessToken = AccessTokenUtil.getCorpAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret(), authCorpId);

            preProcess(client, corpAccessToken, userId);


            ProcessStartHeaders processStartHeaders = new ProcessStartHeaders();
            processStartHeaders.xAcsDingtalkAccessToken = corpAccessToken;

            // 抄送人列表
            ProcessStartRequest.ProcessStartRequestCcs ccs0 = new ProcessStartRequest.ProcessStartRequestCcs()
                    .setAccountType("DING_USER")
                    .setUserId(userId);

            // 参与方列表
//            ProcessStartRequest.ProcessStartRequestParticipants participants0 = new ProcessStartRequest.ProcessStartRequestParticipants()
//                    .setAccountType("DING_USER")
//                    .setSignRequirements("2")
//                    .setUserId(userId);

            ProcessStartRequest.ProcessStartRequestParticipants participants1 = new ProcessStartRequest.ProcessStartRequestParticipants()
                    .setAccountType("OUTER_USER")
                    .setSignRequirements("2")
                    .setAccount("15638940333")
                    .setAccountName("万志强");

            // 文件列表
            ProcessStartRequest.ProcessStartRequestFiles files0 = new ProcessStartRequest.ProcessStartRequestFiles()
                    .setFileId(fileId)
                    .setFileName(fileName);
            ProcessStartRequest processStartRequest = new ProcessStartRequest()
                    .setAutoStart("false")
                    .setInitiatorUserId(userId)
                    .setTaskName("xxx发起的流程")
                    .setFiles(Arrays.asList(files0))
                    .setParticipants(Arrays.asList(participants1))
                    .setCcs(Arrays.asList(ccs0));
            ProcessStartResponse processStartResponse = client.processStartWithOptions(processStartRequest, processStartHeaders, new RuntimeOptions());
            return processStartResponse.getBody();
        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getESignAuthUrl fail！");
        }
    }

    /**
     * e签宝回调接口
     *
     * @param eSignCallBackVO
     * @return
     */
    @Override
    public Map<String, String> callback(ESignCallBackVO eSignCallBackVO) {
        log.info("callback request: {}", JSON.toJSON(eSignCallBackVO));
        return new HashMap<>();
    }

    @Override
    public Boolean deAuthorize(String authCorpId) {

        try {
            Client client = createClient();
            CancelCorpAuthHeaders cancelCorpAuthHeaders = new CancelCorpAuthHeaders();
            cancelCorpAuthHeaders.xAcsDingtalkAccessToken = AccessTokenUtil.getCorpAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret(), authCorpId);
            CancelCorpAuthRequest cancelCorpAuthRequest = new CancelCorpAuthRequest();
            CancelCorpAuthResponse cancelCorpAuthResponse = client.cancelCorpAuthWithOptions(cancelCorpAuthRequest, cancelCorpAuthHeaders, new RuntimeOptions());
            CancelCorpAuthResponseBody body = cancelCorpAuthResponse.getBody();
            return body.getResult();

        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("checkCorpInfo fail！");
        }

    }

    @Override
    public GetIsvStatusResponseBody getESignCorpStatus(String authCorpId) {
        try {

            Client client = createClient();

            GetIsvStatusHeaders getIsvStatusHeaders = new GetIsvStatusHeaders();
            getIsvStatusHeaders.xAcsDingtalkAccessToken = AccessTokenUtil.getCorpAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret(), authCorpId);

            GetIsvStatusResponse isvStatusWithOptions = client.getIsvStatusWithOptions(getIsvStatusHeaders, new RuntimeOptions());

            GetIsvStatusResponseBody body = isvStatusWithOptions.getBody();

            return body;


        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getESignCorpStatus fail！");
        }
    }

    @Override
    public GetExecuteUrlResponseBody getProcessExecuteUrls(String authCorpId, String taskId, String account) {


        try {

            Client client = createClient();

            GetExecuteUrlHeaders getExecuteUrlHeaders = new GetExecuteUrlHeaders();
            getExecuteUrlHeaders.xAcsDingtalkAccessToken = AccessTokenUtil.getCorpAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret(), authCorpId);
            GetExecuteUrlRequest getExecuteUrlRequest = new GetExecuteUrlRequest()
                    .setTaskId(taskId)
                    .setSignContainer(2)
                    .setAccount(account);

            GetExecuteUrlResponse executeUrlWithOptions = client.getExecuteUrlWithOptions(getExecuteUrlRequest, getExecuteUrlHeaders, new RuntimeOptions());

            GetExecuteUrlResponseBody body = executeUrlWithOptions.getBody();

            return body;


        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getESignCorpStatus fail！");
        }
    }

    @Override
    public GetFlowDocsResponseBody getProcessFiles(String authCorpId, String taskId) {

        try {

            Client client = createClient();

            GetFlowDocsHeaders getFlowDocsHeaders = new GetFlowDocsHeaders();
            getFlowDocsHeaders.xAcsDingtalkAccessToken = AccessTokenUtil.getCorpAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret(), authCorpId);
            GetFlowDocsResponse flowDocsWithOptions = client.getFlowDocsWithOptions(taskId, getFlowDocsHeaders, new RuntimeOptions());
            GetFlowDocsResponseBody body = flowDocsWithOptions.getBody();
            return body;

        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("checkCorpInfo fail！");
        }
    }

    @Override
    public CreateProcessResponseBody processStart(String authCorpId, String userId, String fileName, String fileId) {
        try {
            Client client = createClient();

            String corpAccessToken = AccessTokenUtil.getCorpAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret(), authCorpId);

            preProcess(client, corpAccessToken, userId);

            CreateProcessHeaders processStartHeaders = new CreateProcessHeaders();
            processStartHeaders.xAcsDingtalkAccessToken = corpAccessToken;

            // 抄送人列表
            CreateProcessRequest.CreateProcessRequestCcs ccs0 = new CreateProcessRequest.CreateProcessRequestCcs()
                    .setAccountType("DING_USER")
                    .setUserId(userId);

            // 参与方列表
            CreateProcessRequest.CreateProcessRequestParticipants participants0 = new CreateProcessRequest.CreateProcessRequestParticipants()
                    .setAccountType("DING_USER")
                    .setSignRequirements("2")
                    .setUserId(userId);

            // 文件列表
            CreateProcessRequest.CreateProcessRequestFiles files0 = new CreateProcessRequest.CreateProcessRequestFiles()
                    .setFileId(fileId)
                    .setFileType(1)
                    .setFileName(fileName);
            CreateProcessRequest processStartRequest = new CreateProcessRequest()
                    .setInitiatorUserId(userId)
                    .setTaskName("xxx发起的流程")
                    .setFiles(Arrays.asList(files0))
                    .setParticipants(Arrays.asList(participants0))
                    .setCcs(Arrays.asList(ccs0));
            CreateProcessResponse processWithOptions = client.createProcessWithOptions(processStartRequest, processStartHeaders, new RuntimeOptions());
            return processWithOptions.getBody();
        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("processStart fail！");
        }
    }

    private void preProcess(Client client, String corpAccessToken, String userId) {

        checkCorpInfo(client, corpAccessToken);

        checkUserInfo(client, corpAccessToken, userId);

    }

    /**
     * 判断企业是否实名认证
     *
     * @param client
     * @param corpAccessToken
     */
    private void checkCorpInfo(Client client, String corpAccessToken) {

        try {
            GetCorpInfoHeaders getCorpInfoHeaders = new GetCorpInfoHeaders();
            getCorpInfoHeaders.xAcsDingtalkAccessToken = corpAccessToken;
            GetCorpInfoResponse corpInfoWithOptions = client.getCorpInfoWithOptions(getCorpInfoHeaders, new RuntimeOptions());

            GetCorpInfoResponseBody body = corpInfoWithOptions.getBody();

            String orgRealName = body.getOrgRealName();

            if (StringUtils.isBlank(orgRealName)) {
                throw new RuntimeException("企业尚未在e签宝完成实名认证，请先进行实名认证！");
            }

        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("checkCorpInfo fail！");
        }
    }

    /**
     * 判断用户是否实名认证
     *
     * @param client
     * @param corpAccessToken
     * @param userId
     */
    private void checkUserInfo(Client client, String corpAccessToken, String userId) {

        try {
            GetUserInfoHeaders getUserInfoHeaders = new GetUserInfoHeaders();
            getUserInfoHeaders.xAcsDingtalkAccessToken = corpAccessToken;
            GetUserInfoResponse userInfoResponse = client.getUserInfoWithOptions(userId, getUserInfoHeaders, new RuntimeOptions());
            GetUserInfoResponseBody body = userInfoResponse.getBody();
            String userRealName = body.getUserRealName();

            if (StringUtils.isBlank(userRealName)) {
                throw new RuntimeException("您尚未在e签宝完成实名认证，请先进行实名认证！");
            }

        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("checkUserInfo fail！");
        }
    }

    public Map<String, String> uploadFile(String authCorpId) {

        String fileName = "contract.pdf";
        String contentType = "application/octet-stream";
        Resource resource = new ClassPathResource(fileName);

        InputStream inputStream = null;
        try {
            Client client = createClient();
            File file = resource.getFile();
            inputStream = resource.getInputStream();
            Long fileSize = Integer.valueOf(inputStream.available()).longValue();

            // 计算ContentMD5值
            byte[] bytes = DigestUtils.md5(inputStream);
            String contentMd5 = new String(Base64.encodeBase64(bytes));

            // 获取上传文件地址
            GetFileUploadUrlHeaders getFileUploadUrlHeaders = new GetFileUploadUrlHeaders();
            getFileUploadUrlHeaders.xAcsDingtalkAccessToken = AccessTokenUtil.getCorpAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret(), authCorpId);
            GetFileUploadUrlRequest getFileUploadUrlRequest = new GetFileUploadUrlRequest()
                    .setContentMd5(contentMd5)
                    .setContentType(contentType)
                    .setFileName(fileName)
                    .setFileSize(fileSize)
                    .setConvert2Pdf(false);
            GetFileUploadUrlResponse fileUploadUrlWithOptions = client.getFileUploadUrlWithOptions(getFileUploadUrlRequest, getFileUploadUrlHeaders, new RuntimeOptions());
            GetFileUploadUrlResponseBody fileUploadUrlWithOptionsBody = fileUploadUrlWithOptions.getBody();

            // 上传文件
            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.addHeader("Content-MD5", contentMd5);
            builder.addHeader("Content-Type", contentType);
            Request request = builder.url(fileUploadUrlWithOptionsBody.getUploadUrl()).put(RequestBody.create(MediaType.get("application/octet-stream"), file)).build();
            log.info("upload file request: {}", JSON.toJSON(request));
            Response response = okHttpClient.newCall(request).execute();
            log.info("upload file response: {}", JSON.toJSON(response));
            int code = response.code();
            if (code != HttpStatus.OK.value()) {
                throw new RuntimeException("上传文件失败！");
            }
            response.close();
            Map resultMap = new HashMap();
            resultMap.put("fileId", fileUploadUrlWithOptionsBody.getFileId());
            resultMap.put("fileName", fileName);

            return resultMap;
        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("upload file fail！");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public GetFileInfoResponseBody getUploadFile(String fileId, String authCorpId) {
        try {
            Client client = createClient();
            GetFileInfoHeaders getFileInfoHeaders = new GetFileInfoHeaders();
            getFileInfoHeaders.xAcsDingtalkAccessToken = AccessTokenUtil.getCorpAccessToken(appConfig.getSuiteKey(), appConfig.getSuiteSecret(), authCorpId);
            GetFileInfoResponse fileInfoWithOptions = client.getFileInfoWithOptions(fileId, getFileInfoHeaders, new RuntimeOptions());
            GetFileInfoResponseBody body = fileInfoWithOptions.getBody();
            return body;

        } catch (TeaException e) {
            throw new InvokeDingTalkException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getUploadFile fail！");
        }
    }


}
