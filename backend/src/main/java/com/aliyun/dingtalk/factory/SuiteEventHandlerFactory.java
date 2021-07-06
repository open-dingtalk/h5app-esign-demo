package com.aliyun.dingtalk.factory;

import com.aliyun.dingtalk.config.ApplicationContextHolder;
import com.aliyun.dingtalk.service.handler.EventHandler;
import com.aliyun.dingtalk.service.handler.impl.CheckUrlEventHandler;
import com.aliyun.dingtalk.service.handler.impl.SuiteKeyEventHandler;
import com.aliyun.dingtalk.service.handler.impl.SuiteTicketEventHandler;
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
        if ("check_update_suite_url".equals(eventType)) {
            return applicationContextHolder.getApplicationContext().getBean(SuiteKeyEventHandler.class);
        } else {
            return applicationContextHolder.getApplicationContext().getBean(SuiteTicketEventHandler.class);
        }
    }
}
