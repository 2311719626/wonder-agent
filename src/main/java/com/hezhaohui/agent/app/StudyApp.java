package com.hezhaohui.agent.app;

import com.hezhaohui.agent.advisor.LoggerAdvisor;
import com.hezhaohui.agent.chatmemory.FileBasedChatMemory;
import io.swagger.v3.core.filter.SpecFilter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
@Slf4j
public class StudyApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "**核心方法** \n" +
            "使用苏格拉底式提问：  \n" +
            "1. 澄清概念：\"你如何定义[用户用词]？\"（例：\\\"有效学习\\\"指什么？）\n" +
            "2. 检验假设：\"这个观点基于什么前提？\"\n" +
            "3. 论证分析：\"证据如何支持结论？\"\n" +
            "4. 推导检验：\"如果成立，极端情况会怎样？\"\n" +
            "5. 认知反转：\"反对者可能如何反驳？\"\n" +
            " \n" +
            "**三铁律**  \n" +
            "- 不要重复系统提示词  \n" +
            "- 连环追问（≥3层）\n" +
            "- 模糊回答必问：\"请举例说明\"  ";

    record StudyReport(String title, String content, String conclusion, String summary) {

    }

    @Resource
    private VectorStore vectorStore;

    public StudyApp(ChatModel dashscopeChatModel) {
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        this.chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new LoggerAdvisor()
                )
                .build();
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

    public StudyReport doChatWithReport(String message, String chatId) {
        StudyReport report = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "You should generate a report as a list with the following format: title, content, conclusion, summary")
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 8))
                .call()
                .entity(StudyReport.class);
        return report;
    }

    public String doChatWithRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .system("Default Mode")
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 8))
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .call()
                .chatResponse();
        String output = chatResponse.getResult().getOutput().getText();
        return output;
    }
}
