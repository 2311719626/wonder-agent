package com.hezhaohui.agent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DocumentLoaderTest {

    @Resource
    private DocumentLoader documentLoader;

    @Test
    void loadMarkdowns() {
        documentLoader.loadMarkdowns();
    }
}