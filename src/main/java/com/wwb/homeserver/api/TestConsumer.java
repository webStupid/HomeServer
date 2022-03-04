package com.wwb.homeserver.api;

import com.wwb.homeserver.service.TestProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weibo
 * @date 2022/1/18
 */
@Slf4j
@RestController
@RequestMapping("/api/home")
public class TestConsumer {

    @Autowired
    private TestProvider testProvider;

    @GetMapping("/testConsumer")
    public String testConsumer(){
        String text = testProvider.testProvider();
        log.info(text);
        log.info("consumer");
        return "SUCCESS";
    }


}
