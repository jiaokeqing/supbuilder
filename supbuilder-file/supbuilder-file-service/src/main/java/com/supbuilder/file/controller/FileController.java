package com.supbuilder.file.controller;


import com.supbuilder.common.core.util.R;
import com.supbuilder.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file/converse")
public class FileController {

    private final FileService fileService;

    @GetMapping("/status/{fileId}")
    public R fileStatus(@PathVariable("fileId")String fileId){
        return fileService.getFileStatus(fileId);
    }
}
