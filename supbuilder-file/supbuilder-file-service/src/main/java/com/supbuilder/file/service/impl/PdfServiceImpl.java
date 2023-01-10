package com.supbuilder.file.service.impl;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.supbuilder.common.core.constant.FileHandleTypeConstants;
import com.supbuilder.common.core.constant.FileTypeSuffixConstants;
import com.supbuilder.common.core.util.RedisUtil;
import com.supbuilder.file.api.constant.FileStatusEnum;
import com.supbuilder.file.api.vo.FileHandleVO;
import com.supbuilder.file.service.PdfService;
import com.supbuilder.file.utils.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class PdfServiceImpl implements PdfService {

    @Value("${supbuilder.converse.upload.path}")
    private String converseUploadPath;

    @Value("${supbuilder.converse.result.path}")
    private String conversedResultPath;


    @Value("${supbuilder.converse.download.url}")
    private String converseDownLoadUrl;


    @Autowired
    private RedisUtil redisUtil;

    @Async
    @Override
    public void toWord(MultipartFile pdfFile, String fileId) {


        String pdfUrl = null;
        try {
            pdfUrl = UploadFileUtil.uploadFile(pdfFile, converseUploadPath);
            File inPath = new File(pdfUrl);
            File outPath = new File(conversedResultPath + pdfFile.getOriginalFilename() + FileTypeSuffixConstants.DOCX_SUFFIX);

            FileHandleVO fileHandleVO=new FileHandleVO(fileId,null, FileStatusEnum.ING,"文件正在处理，请稍等...");
            //处理结果
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);


            //pdfActiveX PDDoc对象 主要建立PDF对象
            ActiveXComponent app = new ActiveXComponent("AcroExch.PDDoc");
            //PDF控制对象
            Dispatch pdfObject = app.getObject();
            //打开PDF文件，建立PDF操作的开始
            Dispatch.call(pdfObject, "Open", new Variant(inPath.getAbsolutePath()));
            Variant jsObj = Dispatch.call(pdfObject, "GetJSObject");
            Dispatch.call(jsObj.getDispatch(), "SaveAs", outPath.getPath(), "com.adobe.acrobat.docx");
            app.invoke("Close");


            String downloadUrl=converseDownLoadUrl+ pdfFile.getOriginalFilename() + FileTypeSuffixConstants.DOCX_SUFFIX;
            //更新处理结果
            fileHandleVO=new FileHandleVO(fileId,downloadUrl, FileStatusEnum.ING,"文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {
            e.printStackTrace();
            FileHandleVO fileHandleVO=new FileHandleVO(fileId,null, FileStatusEnum.ING,"文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }


    }


}
