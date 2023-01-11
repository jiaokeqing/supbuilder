package com.supbuilder.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {

    void toPdf(MultipartFile pdfFile, String fileId);

}
