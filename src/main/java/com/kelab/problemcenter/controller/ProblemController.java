package com.kelab.problemcenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemInfo;
import com.kelab.info.problemcenter.query.ProblemQuery;
import com.kelab.problemcenter.builder.ProblemBuilder;
import com.kelab.problemcenter.dal.domain.ProblemDomain;
import com.kelab.problemcenter.service.ProblemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 删除题目
     */
    @DeleteMapping("/problem.do")
    @Verify(sizeLimit = "ids [1, 200]")
    public JsonAndModel delete(Context context, @RequestParam("ids") List<Integer> ids) {
        problemService.deleteProblems(context, ids);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 修改题目
     */
    @PutMapping("/problem.do")
    @Verify(notNull = "record.id")
    public JsonAndModel update(Context context, @RequestBody ProblemInfo record) {
        ProblemDomain updateRecord = ProblemBuilder.buildUpdateProblemDomain(record);
        problemService.updateProblem(context, updateRecord);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 题目总数
     */
    @GetMapping("/problem/count.do")
    public JsonAndModel problemTotal(Context context) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(problemService.problemTotal(context))
                .build();
    }

    /**
     * 查询来源列表
     */
    @GetMapping("/problem/source.do")
    public JsonAndModel querySource(Context context, Integer limit) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(problemService.querySource(context, limit))
                .build();
    }
}
