package com.aliyun.dingtalk.service.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalk.service.handler.EventHandler;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

/**
 * 推送suiteTicket
 */
@Service
public class SuiteKeyEventHandler implements EventHandler {

    /**
     * 接收到推送过来的suiteTicket时保存到application.properties(target里面)
     * @param eventJson
     */
    @Override
    public void handler(JSONObject eventJson) {
        Properties properties = new Properties();
        String fileName = "application.properties";
        Resource resource = new ClassPathResource(fileName);
        try {
            PathResource pathResource = new PathResource(resource.getURI());
            properties.load(resource.getInputStream());
            properties.setProperty("dingtalk.suite_ticket", eventJson.getString("SuiteTicket"));
            properties.store(pathResource.getOutputStream(), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
