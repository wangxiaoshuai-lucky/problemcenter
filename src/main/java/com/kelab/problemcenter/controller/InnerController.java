package com.kelab.problemcenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemInfo;
import com.kelab.info.usercenter.info.OnlineStatisticResult;
import com.kelab.problemcenter.service.ProblemService;
import com.kelab.problemcenter.service.ProblemSubmitRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class InnerController {

    private ProblemSubmitRecordService problemSubmitRecordService;

    private ProblemService problemService;

    public InnerController(ProblemSubmitRecordService problemSubmitRecordService,
                           ProblemService problemService) {
        this.problemSubmitRecordService = problemSubmitRecordService;
        this.problemService = problemService;
    }

    /**
     * 获取每个小时的登录情况
     * 走缓存，endTime当前的整点时间, startTime昨天的整点时间
     */
    @GetMapping("/inner/countDay")
    @Verify(notNull = "*")
    public Map<String, OnlineStatisticResult> countDay(Context context, Long startTime, Long endTime) {
        return problemSubmitRecordService.countDay(context, startTime, endTime);
    }

    /**
     * 通过ids查询题目信息
     * 走缓存
     */
    @GetMapping("/inner/queryByIds")
    @Verify(sizeLimit = "ids [1, 10000]")
    public List<ProblemInfo> queryByIds(Context context, @RequestParam("ids") List<Integer> ids) {
        return problemService.queryByIds(context, ids);
    }
}
