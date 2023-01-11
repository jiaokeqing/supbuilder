package com.supbuilder.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface PptService {

    void toPdf(MultipartFile pptFile, String fileId);

}
