package com.wwb.homeserver.api;

import lombok.extern.slf4j.Slf4j;
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

//    @PostMapping("/testConsumer")
//    public ResponseResult testConsumer(@RequestParam String name) {
//        TestRequest testRequest = new TestRequest();
//        testRequest.setName(name);
//        return ResponseResult.success(testProvider.testProvider(testRequest).getData());
//    }


}
