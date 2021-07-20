package com.aliyun.dingtalk.factory;

import com.aliyun.dingtalk.config.ApplicationContextHolder;
import com.aliyun.dingtalk.service.handler.EventHandler;
import com.aliyun.dingtalk.service.handler.impl.SuiteTicketEventHandler;
import com.aliyun.dingtalk.service.handler.impl.SuiteAuthCodeEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 回调测试事件工厂
 */
@Component
public class SuiteEventHandlerFactory extends AbstractEventHandlerFactory {

    @Autowired
    private ApplicationContextHolder applicationContextHolder;

    @Override
    public EventHandler getEventHandler(String eventType) {
        if ("suite_ticket".equalsIgnoreCase(eventType)) {
            return applicationContextHolder.getApplicationContext().getBean(SuiteTicketEventHandler.class);
        } else if ("tmp_auth_code".equals(eventType)) {
            // http推送方式授权应用
            return applicationContextHolder.getApplicationContext().getBean(SuiteAuthCodeEventHandler.class);
        } else if ("org_suite_auth".equals(eventType)) {
            // syncHttp推送方式授权应用
            return applicationContextHolder.getApplicationContext().getBean(SuiteAuthCodeEventHandler.class);
        } else {
            throw new RuntimeException("tmp eventType not match！");
        }
    }
}
