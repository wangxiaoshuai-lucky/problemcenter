package com.kelab.problemcenter.util;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\wangzy\\Desktop\\Desktop");
        System.out.println(ZipUtils.unzip("C:\\Users\\wangzy\\Desktop\\Desktop.zip", "C:\\Users\\wangzy\\Desktop\\Desktop"));
        System.out.println(file.getPath());
    }
}
