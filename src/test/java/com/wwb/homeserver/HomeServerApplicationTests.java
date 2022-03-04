package com.wwb.homeserver;

import com.wwb.homeserver.api.TestConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HomeServerApplicationTests {

    @Autowired
    private TestConsumer testConsumer;

    @Test
    void contextLoads() {
        testConsumer.testConsumer();
    }

}
