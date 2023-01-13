package com.supbuilder.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    /**
     * pdf合并
     * @param pdfFiles
     * @param fileId
     */
    void pdfMerge(MultipartFile[] pdfFiles, String fileId);

    /**
     * pdf 按页拆分 拆分单个文件页数
     * @param pdfFile
     * @param splitSize 拆分单个文件页数
     * @param fileId
     */
    void pdfSplit(MultipartFile pdfFile,Integer splitSize, String fileId);

    /**
     * pdf按范围拆分
     * @param pdfFile
     * @param range "1-7"表示复制1到7页、"8-"表示复制从第八页之后到文档末尾
     * @param fileId
     */
    void pdfSplitByRange(MultipartFile pdfFile,String  range, String fileId);

}
