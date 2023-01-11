package com.supbuilder.file.controller;

import com.supbuilder.common.core.util.R;
import com.supbuilder.file.api.constant.FileStatusEnum;
import com.supbuilder.file.api.vo.FileHandleVO;
import com.supbuilder.file.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelService excelService;
    @PostMapping("/toPdf")
    public R toWord(@RequestParam(value = "file",required = true) MultipartFile excelFile) {
        String fileId= UUID.randomUUID().toString();
        excelService.toPdf(excelFile,fileId);

        FileHandleVO fileHandleVO=new FileHandleVO(fileId,null, FileStatusEnum.ING,"文件正在处理，请稍等...");
        return R.ok(fileHandleVO,"文件正在处理，请稍等...");
    }
}
