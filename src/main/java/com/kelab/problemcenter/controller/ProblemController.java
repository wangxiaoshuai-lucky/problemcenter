package com.kelab.problemcenter.controller;

import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemInfo;
import com.kelab.info.problemcenter.query.ProblemQuery;
import com.kelab.info.problemcenter.query.ProblemTagsQuery;
import com.kelab.problemcenter.builder.ProblemBuilder;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.service.ProblemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProblemController {

    private ProblemService problemService;


    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    /**
     * 分页查询题目
     */
    @GetMapping("/problem.do")
    public JsonAndModel queryPage(Context context, ProblemQuery query) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(problemService.queryPage(context, query))
                .build();
    }

    /**
     * 添加题目
     */
    @PostMapping("/problem.do")
    public JsonAndModel save(Context context, @RequestBody ProblemInfo problemInfo) {
        ProblemDomain newRecord = ProblemBuilder.buildNewProblemDomain(problemInfo);
        problemService.saveProblem(context, newRecord);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 分页查询标签
     */
    @GetMapping("/tags.do")
    public JsonAndModel queryPage(Context context, ProblemTagsQuery query) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(problemService.queryPage(context, query))
                .build();
    }
}
