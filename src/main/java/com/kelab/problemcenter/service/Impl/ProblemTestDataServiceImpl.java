package com.kelab.problemcenter.service.Impl;

import com.google.common.base.Preconditions;
import com.kelab.info.base.constant.UserRoleConstant;
import com.kelab.info.context.Context;
import com.kelab.info.problemcenter.info.ProblemTestDataInfo;
import com.kelab.problemcenter.config.AppSetting;
import com.kelab.problemcenter.convert.ProblemTestDataConvert;
import com.kelab.problemcenter.dal.domain.ProblemTestDataDomain;
import com.kelab.problemcenter.dal.repo.ProblemTestDataRepo;
import com.kelab.problemcenter.service.ProblemTestDataService;
import com.kelab.problemcenter.support.ContextLogger;
import com.kelab.problemcenter.util.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProblemTestDataServiceImpl implements ProblemTestDataService {

    private ProblemTestDataRepo problemTestDataRepo;

    private ContextLogger contextLogger;

    public ProblemTestDataServiceImpl(ProblemTestDataRepo problemTestDataRepo,
                                      ContextLogger contextLogger) {
        this.problemTestDataRepo = problemTestDataRepo;
        this.contextLogger = contextLogger;
    }

    @Override
    public List<ProblemTestDataInfo> queryByProblemId(Context context, Integer problemId) {
        return convertToInfo(problemTestDataRepo.queryByProblemId(problemId));
    }

    @Override
    public void updateProblemTestData(Context context, ProblemTestDataDomain record) {
        if (record.getId() == -1) {
            // insert
            Preconditions.checkArgument(record.getProblemId() != null, "未指定题号");
            Preconditions.checkArgument(record.getIn() != null, "未指定输入");
            Preconditions.checkArgument(record.getOut() != null, "未指定输出");
            problemTestDataRepo.saveList(Collections.singletonList(record));
        } else {
            problemTestDataRepo.update(record);
        }
    }

    @Override
    public void deleteProblemTestData(Context context, List<Integer> ids) {
        problemTestDataRepo.delete(ids);
    }

    @Override
    public ResponseEntity<byte[]> downloadTestData(Context context, Integer problemId, String key) {
        if (context.getOperatorRoleId() != UserRoleConstant.ADMIN
                && context.getOperatorRoleId() != UserRoleConstant.TEACHER
                && !AppSetting.secretKey.equals(key)) {
            throw new IllegalArgumentException("无权限");
        }
        List<ProblemTestDataDomain> testDataList = problemTestDataRepo.queryByProblemId(problemId);
        if (!CollectionUtils.isEmpty(testDataList)) {
            File path = new File("/tmp/" + System.currentTimeMillis());
            File problemPath = new File(path, problemId.toString());
            Preconditions.checkState(problemPath.mkdirs());
            testDataList.forEach(singleTestData -> {
                File inFile = new File(problemPath, singleTestData.getId() + ".in");
                File outFile = new File(problemPath, singleTestData.getId() + ".out");
                try {
                    Preconditions.checkState(inFile.createNewFile());
                    FileUtils.write(inFile, singleTestData.getIn());
                    FileUtils.write(outFile, singleTestData.getOut());
                } catch (IOException e) {
                    contextLogger.error(context, "生成文件出错:%s", e.getMessage());
                }
            });
            String zipName = problemId + "判题数据.zip";
            zipName = new String(zipName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            ZipUtils.zipWithoutRoot(problemPath.getPath(), path.getPath() + "/" + zipName);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + zipName);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            try {
                return new ResponseEntity<>(FileUtils.readFileToByteArray(new File(path + "/" + zipName)), headers, HttpStatus.CREATED);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    FileUtils.deleteDirectory(path);
                } catch (Exception e) {
                    contextLogger.error(context, "删除文件失败：%s", e.getMessage());
                }
            }
        }
        return null;
    }

    @Override
    public void uploadTestData(Context context, Integer problemId, MultipartFile file) throws Exception {
        File workspace = new File("/tmp/" + System.currentTimeMillis());
        Preconditions.checkState(workspace.mkdirs());
        File zipFile = new File(workspace, "testData.zip");
        File testDataPath = new File(workspace, "testData");
        Preconditions.checkState(testDataPath.mkdirs());
        try {
            Preconditions.checkState(zipFile.createNewFile());
            file.transferTo(zipFile.toPath());
            ZipUtils.unzip(zipFile.getPath(), testDataPath.getPath());
            Map<String, ProblemTestDataDomain> newTestData = new HashMap<>();
            for (File single : Objects.requireNonNull(testDataPath.listFiles())) {
                int index = single.getName().indexOf(".");
                Preconditions.checkState(index != -1);
                String id = single.getName().substring(0, index);
                if (single.getName().endsWith(".in")) {
                    ProblemTestDataDomain testData = newTestData.getOrDefault(id, new ProblemTestDataDomain());
                    testData.setIn(FileUtils.readFileToString(single));
                    newTestData.put(id, testData);
                } else if (single.getName().endsWith(".out")) {
                    ProblemTestDataDomain testData = newTestData.getOrDefault(id, new ProblemTestDataDomain());
                    testData.setOut(FileUtils.readFileToString(single));
                    newTestData.put(id, testData);
                } else {
                    throw new IllegalArgumentException("判题数据有误");
                }
            }
            List<ProblemTestDataDomain> newRecords = new ArrayList<>(newTestData.values());
            if (!CollectionUtils.isEmpty(newRecords)) {
                newRecords.forEach(item -> item.setProblemId(problemId));
                problemTestDataRepo.saveList(newRecords);
            }
        } catch (Exception e) {
            contextLogger.error(context, "上传文件失败:%s", e.getMessage());
            throw new Exception(e);
        } finally {
            try {
                FileUtils.deleteDirectory(workspace);
            } catch (Exception e) {
                // 文件删除失败不用抛出去
                contextLogger.error(context, "文件删除失败:%s", e.getMessage());
            }
        }
    }

    private List<ProblemTestDataInfo> convertToInfo(List<ProblemTestDataDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(ProblemTestDataConvert::domainToInfo).collect(Collectors.toList());
    }
}
