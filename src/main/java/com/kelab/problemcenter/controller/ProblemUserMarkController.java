package com.kelab.problemcenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemUserMarkInfo;
import com.kelab.problemcenter.service.ProblemUserMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProblemUserMarkController {


    private ProblemUserMarkService problemUserMarkService;

    @Autowired(required = false)
    public ProblemUserMarkController(ProblemUserMarkService problemUserMarkService) {
        this.problemUserMarkService = problemUserMarkService;
    }

    /**
     * 获取用户的做题记录
     */
    @GetMapping("/user/problem/challenging.do")
    @Verify(notNull = "userId")
    public JsonAndModel queryAcAndChallenging(Context context, Integer userId) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(problemUserMarkService.queryAcAndChallenging(context, userId))
                .build();
    }

    /**
     * 获取用户的收藏记录
     */
    @GetMapping("/user/problem/collect.do")
    public JsonAndModel collect(Context context) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(problemUserMarkService.collect(context))
                .build();
    }

    /**
     * 取消/添加收藏
     */
    @PostMapping("/user/problem/collect.do")
    @Verify(notNull = {"context.operatorId", "problemUserMarkInfo.userId", "problemUserMarkInfo.problemId"})
    public JsonAndModel saveOrDeleteProblemCollect(Context context, @RequestBody ProblemUserMarkInfo problemUserMarkInfo) {
        // 强制检验是否为自己修改
        if (!context.getOperatorId().equals(problemUserMarkInfo.getUserId())) {
            return JsonAndModel.builder(StatusMsgConstant.ILLEGAL_ACCESS_ERROR).build();
        }
        problemUserMarkService.deleteOrSave(context, problemUserMarkInfo);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }
}
