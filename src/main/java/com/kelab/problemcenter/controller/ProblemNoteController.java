package com.kelab.problemcenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemNoteInfo;
import com.kelab.info.problemcenter.query.ProblemNoteQuery;
import com.kelab.problemcenter.convert.ProblemNoteConvert;
import com.kelab.problemcenter.service.ProblemNoteService;
import com.kelab.problemcenter.service.ProblemNoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProblemNoteController {

    private ProblemNoteService problemNoteService;

    public ProblemNoteController(ProblemNoteService problemNoteService) {
        this.problemNoteService = problemNoteService;
    }

    /**
     * 分页查询笔记
     */
    @GetMapping("/problem/note.do")
    @Verify(numberLimit = {"query.page [1, 100000]", "query.rows [1, 100000]"})
    public JsonAndModel queryPage(Context context, ProblemNoteQuery query) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(problemNoteService.queryPage(context, query))
                .build();
    }

    /**
     * 添加笔记
     */
    @PostMapping("/problem/note.do")
    @Verify(notNull = {"record.title", "record.text", "record.problemId"})
    public JsonAndModel save(Context context, @RequestBody ProblemNoteInfo record) {
        problemNoteService.save(context, ProblemNoteConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 修改笔记
     */
    @PutMapping("/problem/note.do")
    @Verify(notNull = {"record.id"})
    public JsonAndModel update(Context context, @RequestBody ProblemNoteInfo record) {
        problemNoteService.update(context, ProblemNoteConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 删除笔记
     */
    @DeleteMapping("/problem/note.do")
    @Verify(sizeLimit = "ids [1, 200]")
    public JsonAndModel delete(Context context,@RequestParam("ids") List<Integer> ids) {
        problemNoteService.delete(context, ids);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }
}
