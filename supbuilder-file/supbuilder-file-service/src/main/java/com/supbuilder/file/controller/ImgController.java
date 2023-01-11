package com.supbuilder.file.controller;

import com.supbuilder.common.core.util.R;
import com.supbuilder.file.api.constant.FileStatusEnum;
import com.supbuilder.file.api.vo.FileHandleVO;
import com.supbuilder.file.service.ExcelService;
import com.supbuilder.file.service.ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/img")
public class ImgController {
    private final ImgService imgService;
    @PostMapping("/toPdf")
    public R toWord(@RequestPart("files") MultipartFile[] imgFileList) {
        String fileId= UUID.randomUUID().toString();
        imgService.toPdf(imgFileList,fileId);

        FileHandleVO fileHandleVO=new FileHandleVO(fileId,null, FileStatusEnum.ING,"文件正在处理，请稍等...");
        return R.ok(fileHandleVO,"文件正在处理，请稍等...");
    }
}
