package com.kelab.problemcenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemSubmitRecordInfo;
import com.kelab.info.problemcenter.query.ProblemSubmitRecordQuery;
import com.kelab.problemcenter.builder.ProblemSubmitRecordBuilder;
import com.kelab.problemcenter.result.SubmitResult;
import com.kelab.problemcenter.service.ProblemSubmitRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * 提交判题
     */
    @PostMapping("/submit.do")
    @Verify(notNull = {"record.problemId", "record.compilerId", "record.source", "context.operatorId"})
    public JsonAndModel submit(Context context, @RequestBody ProblemSubmitRecordInfo record) {
        SubmitResult submitResult = problemSubmitRecordService.submit(context, ProblemSubmitRecordBuilder.buildNewSubmitRecord(record));
        return JsonAndModel.builder(submitResult.getStatus())
                .data(submitResult.getSubmitId())
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
