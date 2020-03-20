package com.kelab.problemcenter.controller;

import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemSubmitRecordQuery;
import com.kelab.problemcenter.service.ProblemSubmitRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProblemSubmitRecordController {

    private ProblemSubmitRecordService problemSubmitRecordService;

    public ProblemSubmitRecordController(ProblemSubmitRecordService problemSubmitRecordService) {
        this.problemSubmitRecordService = problemSubmitRecordService;
    }

    /**
     * 查询提交记录
     */
    @GetMapping("/submit.do")
    public JsonAndModel queryPage(Context context, ProblemSubmitRecordQuery query) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(problemSubmitRecordService.queryPage(context, query))
                .build();
    }

    /**
     * 查询总提交量
     */
    @GetMapping("/submit/count.do")
    public JsonAndModel judgeCount(Context context) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(problemSubmitRecordService.judgeCount(context))
                .build();
    }
}
