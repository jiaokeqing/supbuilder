package com.supbuilder.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface WordService {
    void toPdf(MultipartFile wordFile,String fileId);
}
