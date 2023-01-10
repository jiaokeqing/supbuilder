package com.supbuilder.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface PdfService {
    void toWord(MultipartFile pdfFile,String fileId);
}
