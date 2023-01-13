package com.supbuilder.file.controller;

import com.supbuilder.common.core.util.R;
import com.supbuilder.file.api.constant.FileStatusEnum;
import com.supbuilder.file.api.vo.FileHandleVO;
import com.supbuilder.file.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author 矫克清
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/pdf")
public class PdfController {

    private final PdfService pdfService;

    @PostMapping("/toDocx")
    public R toWord(@RequestParam(value = "file", required = true) MultipartFile pdfFile) {
        String fileId = UUID.randomUUID().toString();
        pdfService.toWord(pdfFile, fileId);

        FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.ING, "文件正在处理，请稍等...");
        return R.ok(fileHandleVO, "文件正在处理，请稍等...");
    }

    @PostMapping("/toPpt")
    public R toPpt(@RequestParam(value = "file", required = true) MultipartFile pdfFile) {
        String fileId = UUID.randomUUID().toString();
        pdfService.toPpt(pdfFile, fileId);

        FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.ING, "文件正在处理，请稍等...");
        return R.ok(fileHandleVO, "文件正在处理，请稍等...");
    }


    @PostMapping("/toExcel")
    public R toExcel(@RequestParam(value = "file", required = true) MultipartFile pdfFile) {
        String fileId = UUID.randomUUID().toString();
        pdfService.toExcel(pdfFile, fileId);

        FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.ING, "文件正在处理，请稍等...");
        return R.ok(fileHandleVO, "文件正在处理，请稍等...");
    }

    @PostMapping("/toImg")
    public R toImg(@RequestParam(value = "file", required = true) MultipartFile pdfFile, @RequestParam("type") Integer type) {
        String fileId = UUID.randomUUID().toString();
        pdfService.toImg(pdfFile, fileId, type);

        FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.ING, "文件正在处理，请稍等...");
        return R.ok(fileHandleVO, "文件正在处理，请稍等...");
    }

    @PostMapping("/toTxt")
    public R toTxt(@RequestParam(value = "file", required = true) MultipartFile pdfFile) {
        String fileId = UUID.randomUUID().toString();
        pdfService.toTxt(pdfFile, fileId);

        FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.ING, "文件正在处理，请稍等...");
        return R.ok(fileHandleVO, "文件正在处理，请稍等...");
    }

    @PostMapping("/toHtml")
    public R toHtml(@RequestParam(value = "file", required = true) MultipartFile pdfFile) {
        String fileId = UUID.randomUUID().toString();
        pdfService.toHtml(pdfFile, fileId);

        FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.ING, "文件正在处理，请稍等...");
        return R.ok(fileHandleVO, "文件正在处理，请稍等...");
    }

    @PostMapping("/merge")
    public R toWord(@RequestPart("files") MultipartFile[] pdfFileList) {
        String fileId = UUID.randomUUID().toString();
        pdfService.pdfMerge(pdfFileList, fileId);

        FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.ING, "文件正在处理，请稍等...");
        return R.ok(fileHandleVO, "文件正在处理，请稍等...");
    }

    @PostMapping("/split")
    public R toWord(@RequestParam(value = "file", required = true) MultipartFile pdfFile,
                    @RequestParam(value = "type", required = true) Integer type,
                    @RequestParam(value = "splitSize", required = false) Integer splitSize,
                    @RequestParam(value = "range", required = false) String range) {
        String fileId = UUID.randomUUID().toString();

        if (type == 0) {
            pdfService.pdfSplit(pdfFile, splitSize, fileId);
        } else {
            pdfService.pdfSplitByRange(pdfFile, range, fileId);
        }


        FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.ING, "文件正在处理，请稍等...");
        return R.ok(fileHandleVO, "文件正在处理，请稍等...");
    }

}
