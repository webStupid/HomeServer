package com.wwb.homeserver.api;

import com.wwb.commonbase.utils.response.ResponseResult;
import com.wwb.feignserver.accountserver.ITestProvider;
import com.wwb.feignserver.accountserver.entity.request.TestRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author weibo
 * @date 2022/1/18
 */
@Slf4j
@RestController
@RequestMapping("/api/home")
public class TestConsumer {

    @Autowired
    private ITestProvider testProvider;

    @PostMapping("/testConsumer")
    public ResponseResult testConsumer(@RequestParam String name) {
        TestRequest testRequest = new TestRequest();
        testRequest.setName(name);
        return ResponseResult.success(testProvider.testProvider(testRequest).getData());
    }


}
