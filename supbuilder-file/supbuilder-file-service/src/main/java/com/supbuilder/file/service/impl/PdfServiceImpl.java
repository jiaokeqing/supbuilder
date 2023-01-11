package com.supbuilder.file.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.supbuilder.common.core.constant.FileHandleTypeConstants;
import com.supbuilder.common.core.constant.FileTypeSuffixConstants;
import com.supbuilder.common.core.util.RedisUtil;
import com.supbuilder.file.api.constant.FileStatusEnum;
import com.supbuilder.file.api.vo.FileHandleVO;
import com.supbuilder.file.async.ConverseService;
import com.supbuilder.file.service.PdfService;
import com.supbuilder.file.utils.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfServiceImpl implements PdfService {

    @Value("${supbuilder.converse.upload.path}")
    private String converseUploadPath;

    @Value("${supbuilder.converse.result.path}")
    private String conversedResultPath;


    @Value("${supbuilder.converse.download.url}")
    private String converseDownLoadUrl;


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ConverseService converseService;

    @Override
    public void toWord(MultipartFile pdfFile, String fileId) {

        String timeDir = System.currentTimeMillis() + File.separator;


        try {
            //创建转换目录
            Path converseDir = Paths.get(conversedResultPath + timeDir);
            Files.createDirectory(converseDir);

            String sourceFile = null;
            sourceFile = UploadFileUtil.uploadFile(pdfFile, converseUploadPath + timeDir);
            String name = FileNameUtil.mainName(sourceFile);

            String targetFile = conversedResultPath + timeDir + name + FileTypeSuffixConstants.DOCX_SUFFIX;
            String downloadUrl = converseDownLoadUrl + timeDir + name + FileTypeSuffixConstants.DOCX_SUFFIX;
            converseService.pdfToDocx(sourceFile, targetFile, downloadUrl, fileId);


            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.ING, "文件正在处理，请稍等...");
            //处理结果
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);


        } catch (Exception e) {

            e.printStackTrace();
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }


    }

}
