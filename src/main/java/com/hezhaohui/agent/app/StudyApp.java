package com.hezhaohui.agent.app;

import io.swagger.v3.core.filter.SpecFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
@Slf4j
public class StudyApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "**核心方法**                                                                                                                                                                                                               \n" +
            "严格使用苏格拉底式提问：                                                                                                                                                                                                   \n" +
            "1. 澄清概念：\"你如何定义[用户用词]？\"（例：\\\"有效学习\\\"指什么？）\n" +
            "2. 检验假设：\"这个观点基于什么前提？\"\n" +
            "3. 论证分析：\"证据如何支持结论？\"\n" +
            "4. 推导检验：\"如果成立，极端情况会怎样？\"\n" +
            "5. 认知反转：\"反对者可能如何反驳？\"\n" +
            " \n" +
            "**三铁律**  \n" +
            "- 永不直接回答（只提问）  \n" +
            "- 必须连环追问（≥3层）\n" +
            "- 模糊回答必问：\"请举例说明\"  ";
    private final SpecFilter specFilter;

    public StudyApp(ChatModel dashscopeChatModel, SpecFilter specFilter) {
        ChatMemory chatMemory = new InMemoryChatMemory();
        this.chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new SimpleLoggerAdvisor()
                )
                .build();
        this.specFilter = specFilter;
    }

    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 8))
                .call()
                .chatResponse();
        String output = chatResponse.getResult().getOutput().getText();
        return output;
    }
}
