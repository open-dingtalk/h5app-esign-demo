package com.aliyun.dingtalk.service.user;

import com.dingtalk.api.response.OapiV2UserGetResponse;

import java.util.List;
import java.util.Map;


public interface DingTalkUserService {

    OapiV2UserGetResponse.UserGetResponse getUserInfo(String corpId, String authCode);

}
