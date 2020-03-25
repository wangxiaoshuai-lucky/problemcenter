package com.kelab.problemcenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.usercenter.info.OnlineStatisticResult;
import com.kelab.problemcenter.service.ProblemSubmitRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InnerController {

    private ProblemSubmitRecordService problemSubmitRecordService;

    public InnerController(ProblemSubmitRecordService problemSubmitRecordService) {
        this.problemSubmitRecordService = problemSubmitRecordService;
    }

    /**
     * 获取每个小时的登录情况
     * 走缓存，endTime当前的整点时间, startTime昨天的整点时间
     */
    @GetMapping("/inner/countDay")
    @Verify(notNull = "*")
    public Map<String, OnlineStatisticResult> countDay(Long startTime, Long endTime) {
        return problemSubmitRecordService.countDay(startTime, endTime);
    }
}
