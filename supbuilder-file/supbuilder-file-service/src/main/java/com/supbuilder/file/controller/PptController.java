package com.supbuilder.file.controller;

import com.supbuilder.common.core.util.R;
import com.supbuilder.file.api.constant.FileStatusEnum;
import com.supbuilder.file.api.vo.FileHandleVO;
import com.supbuilder.file.service.PdfService;
import com.supbuilder.file.service.PptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author 矫克清
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/ppt")
public class PptController {

    private final PptService pptService;

    @PostMapping("/toPdf")
    public R toPdf(@RequestParam(value = "file",required = true) MultipartFile pdfFile) {
        String fileId= UUID.randomUUID().toString();
        pptService.toPdf(pdfFile,fileId);

        FileHandleVO fileHandleVO=new FileHandleVO(fileId,null, FileStatusEnum.ING,"文件正在处理，请稍等...");
        return R.ok(fileHandleVO,"文件正在处理，请稍等...");
    }

}
