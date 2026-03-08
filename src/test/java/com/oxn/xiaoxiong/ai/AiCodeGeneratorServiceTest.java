package com.oxn.xiaoxiong.ai;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCodeGeneratorServiceFactoryTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void aiCodeGeneratorService() {
       /* String s = aiCodeGeneratorService.chatWithAi("你是谁呀？");
        System.out.println(s);*/
    }
}