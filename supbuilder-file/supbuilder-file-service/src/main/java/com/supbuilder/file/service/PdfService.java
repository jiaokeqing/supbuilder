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

    /**
     * pdf转图片  0 png  1 jpg
     * @param pdfFile
     * @param fileId
     */
    void toImg(MultipartFile pdfFile,String fileId,Integer type);

    /**
     * pdf转txt
     * @param pdfFile
     * @param fileId
     */
    void toTxt(MultipartFile pdfFile,String fileId);
    /**
     * pdf转html
     * @param pdfFile
     * @param fileId
     */
    void toHtml(MultipartFile pdfFile,String fileId);
}
