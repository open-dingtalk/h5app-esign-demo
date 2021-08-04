package com.aliyun.dingtalk.service.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalk.service.handler.EventHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Properties;

/**
 * 推送suiteTicket
 */
@Service
public class SuiteTicketEventHandler implements EventHandler {

    /**
     * 接收到推送过来的suiteTicket时保存到application.properties(target里面)
     * @param eventJson
     */
    @Override
    public void handler(JSONObject eventJson) {

        String suiteTicket = eventJson.getString("SuiteTicket");
        if (StringUtils.isBlank(suiteTicket)) {
            JSONObject bizData = eventJson.getJSONObject("biz_data");
            suiteTicket = bizData.getString("suiteTicket");
        }
        Properties properties = new Properties();
        String fileName = "application.properties";
        Resource resource = new ClassPathResource(fileName);
        OutputStream outputStream = null;
        try {
            PathResource pathResource = new PathResource(resource.getURI());
            outputStream = pathResource.getOutputStream();
            properties.load(resource.getInputStream());
            properties.setProperty("dingtalk.suite_ticket", suiteTicket);
            properties.store(pathResource.getOutputStream(), "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
