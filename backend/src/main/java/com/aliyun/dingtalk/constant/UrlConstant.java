package com.aliyun.dingtalk.constant;

public class UrlConstant {
    /**
     * 钉钉网关gettoken地址
     */
    public static final String GET_TOKEN_URL = "https://oapi.dingtalk.com/gettoken";


    /**
     * 通过免登授权码获取用户信息 url
     */
    public static final String GET_USER_INFO_URL = "https://oapi.dingtalk.com/topapi/v2/user/getuserinfo";

    /**
     * 根据用户id获取用户详情 url
     */
    public static final String USER_GET_URL = "https://oapi.dingtalk.com/topapi/v2/user/get";

    /**
     * 发起审批实例的接口url
     */
    public static final String GET_PERMANENT_CODE_URL = "https://oapi.dingtalk.com/service/get_permanent_code?suite_access_token=SUITE_ACCESS_TOKEN";

    /**
     * 获取第三方应用的suiteAccessToken url
     */
    public static final String SUITE_ACCESS_TOKEN_URL = "https://oapi.dingtalk.com/service/get_suite_token";

    /**
     * 激活企业授权的应用
     */
    public static final String ACTIVATE_SUITE_URL = "https://oapi.dingtalk.com/service/activate_suite?suite_access_token=SUITE_ACCESS_TOKEN";

    /**
     * 获取第三方应用授权企业的access_token
     */
    public static final String GET_CORP_ACCESS_TOKEN_URL = "https://oapi.dingtalk.com/service/get_corp_token";
}
