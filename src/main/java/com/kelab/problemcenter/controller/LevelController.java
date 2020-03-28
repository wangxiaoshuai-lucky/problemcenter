package com.kelab.problemcenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.LevelInfo;
import com.kelab.info.problemcenter.info.LevelProblemInfo;
import com.kelab.problemcenter.convert.LevelConvert;
import com.kelab.problemcenter.dal.domain.LevelProblemDomain;
import com.kelab.problemcenter.service.LevelService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class LevelController {


    private LevelService levelService;

    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }


    /**
     * 查询段位
     */
    @GetMapping("/level.do")
    public JsonAndModel queryAllLevel(Context context) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(levelService.queryAllLevel(context))
                .build();
    }

    /**
     * 添加段位
     */
    @PostMapping("/level.do")
    @Verify(notNull = {"record.name", "record.grade"})
    public JsonAndModel saveLevel(Context context, @RequestBody LevelInfo record) {
        levelService.saveLevel(context, LevelConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 修改段位
     */
    @PutMapping("/level.do")
    @Verify(notNull = {"record.id", "record.name", "record.grade"})
    public JsonAndModel updateLevel(Context context, @RequestBody LevelInfo record) {
        levelService.updateLevel(context, LevelConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 删除段位
     */
    @DeleteMapping("/level.do")
    @Verify(sizeLimit = "ids [1, 200]")
    public JsonAndModel deleteLevel(Context context, @RequestParam("ids") List<Integer> ids) {
        levelService.deleteLevel(context, ids);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 查看某个段位的题目 管理员端
     */
    @GetMapping("/queryProblems.do")
    @Verify(notNull = "*")
    public JsonAndModel queryProblems(Context context, Integer levelId) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(levelService.queryAllProblem(context, levelId))
                .build();
    }

    /**
     * 查询每个小段位的题目列表 用户端
     */
    @GetMapping("/levelProblem.do")
    @Verify(notNull = "*")
    public JsonAndModel queryLevelProblems(Context context, Integer levelId) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(levelService.queryAllGradeInfo(context, levelId))
                .build();
    }

    /**
     * 插入段位题目
     */
    @PostMapping("/levelProblem.do")
    @Verify(notNull = "*")
    public JsonAndModel insertProblem(Context context, @RequestBody Set<LevelProblemInfo> records) {
        if (!CollectionUtils.isEmpty(records)) {
            levelService.insertProblem(context, records.stream().map(LevelConvert::infoToDomain).collect(Collectors.toList()));
        }
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }
}
