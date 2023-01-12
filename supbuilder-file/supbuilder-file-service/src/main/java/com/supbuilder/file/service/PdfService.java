package com.supbuilder.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface PdfService {
    void toWord(MultipartFile pdfFile,String fileId);

    /**
     * pdf 转 pptx
     * @param pdfFile
     * @param fileId
     */
    void toPpt(MultipartFile pdfFile,String fileId);

    /**
     * pdf转excel  xlsx
     * @param pdfFile
     * @param fileId
     */
    void toExcel(MultipartFile pdfFile,String fileId);
}
