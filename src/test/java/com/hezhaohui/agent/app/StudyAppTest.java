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
        // 第一轮对话
        String output1 = studyApp.doChat("什么是学习？", chatId);
        assertNotNull(output1);
        // 第二轮对话
        String output2 = studyApp.doChat("为什么学习很重要？", chatId);
        assertNotNull(output2);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        
        // 建立两轮对话上下文
        studyApp.doChat("如何高效学习？", chatId);
        studyApp.doChat("有什么技巧？", chatId);
        
        // 生成报告
        StudyApp.StudyReport report = studyApp.doChatWithReport("请总结学习建议报告", chatId);
        assertNotNull(report);
        assertNotNull(report.title());
        assertNotNull(report.content());
        assertNotNull(report.conclusion());
        assertNotNull(report.summary());
    }
}
