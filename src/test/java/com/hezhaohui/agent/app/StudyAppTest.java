package com.hezhaohui.agent.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class StudyAppTest {

    @Resource
    private StudyApp studyApp;

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        String message = "什么是学习？";
        String output = studyApp.doChat(message, chatId);
        assertNotNull(output);
    }
}
