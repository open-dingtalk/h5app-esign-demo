package com.aliyun.dingtalk.factory;

import com.aliyun.dingtalk.config.ApplicationContextHolder;
import com.aliyun.dingtalk.service.handler.EventHandler;
import com.aliyun.dingtalk.service.handler.impl.SuiteKeyEventHandler;
import com.aliyun.dingtalk.service.handler.impl.SuiteTicketEventHandler;
import com.aliyun.dingtalk.service.handler.impl.TmpAuthCodeEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 回调授权事件工厂
 */
@Component
public class TmpEventHandlerFactory extends AbstractEventHandlerFactory {

    @Autowired
    private ApplicationContextHolder applicationContextHolder;

    @Override
    public EventHandler getEventHandler(String eventType) {
        if ("tmp_auth_code".equals(eventType)) {
            return applicationContextHolder.getApplicationContext().getBean(TmpAuthCodeEventHandler.class);
        } else {
            throw new RuntimeException("tmp eventType not match！");
        }
    }
}
