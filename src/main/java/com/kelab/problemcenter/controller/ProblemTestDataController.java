package com.kelab.problemcenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemTestDataInfo;
import com.kelab.problemcenter.convert.ProblemTestDataConvert;
import com.kelab.problemcenter.service.ProblemTestDataService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProblemTestDataController {

    private ProblemTestDataService problemTestDataService;

    public ProblemTestDataController(ProblemTestDataService problemTestDataService) {
        this.problemTestDataService = problemTestDataService;
    }

    /**
     * 下载判题数据
     */
    @GetMapping("/testdata/download.do")
    @Verify(notNull = {"context.operatorId", "context.operatorRoleId", "problemId"})
    public Object downloadTestData(Context context, Integer problemId, String key) {
        return problemTestDataService.downloadTestData(context, problemId, key);
    }

    /**
     * 查询判题数据
     */
    @GetMapping("/testdata/query.do")
    @Verify(notNull = "*")
    public JsonAndModel queryByProblemId(Context context, Integer problemId) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(problemTestDataService.queryByProblemId(context, problemId))
                .build();
    }

    /**
     * 更新判题数据
     */
    @PutMapping("/testdata/update.do")
    @Verify(notNull = {"record.id", "record.in", "record.out"})
    public JsonAndModel updateTestData(Context context, @RequestBody ProblemTestDataInfo record) {
        problemTestDataService.updateProblemTestData(context, ProblemTestDataConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 删除判题数据
     */
    @DeleteMapping("/testdata/delete.do")
    @Verify(sizeLimit = "ids [1, 200]")
    public JsonAndModel deleteProblemTestData(Context context, @RequestParam("ids") List<Integer> ids) {
        problemTestDataService.deleteProblemTestData(context, ids);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 上传测试数据
     */
    @PostMapping(value = "/testdata/upload.do")
    public JsonAndModel uploadTestData(Context context, Integer problemId, @RequestBody MultipartFile file) throws Exception {
        problemTestDataService.uploadTestData(context, problemId, file);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }
}
