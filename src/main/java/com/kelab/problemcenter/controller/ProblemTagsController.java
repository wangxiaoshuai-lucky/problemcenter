package com.kelab.problemcenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemTagsInfo;
import com.kelab.info.problemcenter.query.ProblemTagsQuery;
import com.kelab.problemcenter.convert.ProblemTagsConvert;
import com.kelab.problemcenter.service.ProblemTagsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProblemTagsController {

    private ProblemTagsService problemTagsService;

    public ProblemTagsController(ProblemTagsService problemTagsService) {
        this.problemTagsService = problemTagsService;
    }

    /**
     * 分页查询标签
     */
    @GetMapping("/tags.do")
    public JsonAndModel queryPage(Context context, ProblemTagsQuery query) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(problemTagsService.queryPage(context, query))
                .build();
    }

    /**
     * 添加标签
     */
    @PostMapping("/tags.do")
    @Verify(notNull = "record.name")
    public JsonAndModel save(Context context, @RequestBody ProblemTagsInfo record) {
        problemTagsService.save(context, ProblemTagsConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 修改标签
     */
    @PutMapping("/tags.do")
    @Verify(notNull = {"record.name", "record.id"})
    public JsonAndModel update(Context context, @RequestBody ProblemTagsInfo record) {
        problemTagsService.update(context, ProblemTagsConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/tags.do")
    @Verify(sizeLimit = "ids [1, 200]")
    public JsonAndModel delete(Context context,@RequestParam("ids") List<Integer> ids) {
        problemTagsService.delete(context, ids);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }
}
