package com.supbuilder.file.async;


import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.supbuilder.common.core.constant.FileHandleTypeConstants;
import com.supbuilder.common.core.constant.FileTypeSuffixConstants;
import com.supbuilder.common.core.util.RedisUtil;
import com.supbuilder.file.api.constant.FileStatusEnum;
import com.supbuilder.file.api.vo.FileHandleVO;
import com.supbuilder.file.utils.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author 矫克清
 */
@Component
public class ConverseService {
    static final int wdFormatPDF = 17;// PDF 格式
    private static final int xlTypePDF = 0;  // xls格式
    @Autowired
    private RedisUtil redisUtil;

    /**
     * doc、docx转pdf
     *
     * @param sourceFile
     * @param targetFile
     * @param downloadUrl
     * @param fileId
     */
    @Async
    public void word2Pdf(String sourceFile, String targetFile, String downloadUrl, String fileId) {
        System.out.println("启动Word转pdf处理程序...");

        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch doc = null;


        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();


            doc = Dispatch.call(docs, "Open", sourceFile).toDispatch();
            System.out.println("打开文档..." + sourceFile);
            System.out.println("转换文档到PDF..." + targetFile);
            File tofile = new File(targetFile);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc,
                    "SaveAs",
                    targetFile,
                    wdFormatPDF);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");
            app.invoke("Quit");


            //更新处理结果
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, downloadUrl, FileStatusEnum.SUCCESS, "文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {

            e.printStackTrace();
            if (app != null) {
                app.invoke("Close");
            }
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }


    }

    /**
     * pdf转docx
     *
     * @param sourceFile
     * @param targetFile
     * @param downloadUrl
     * @param fileId
     */
    @Async
    public void pdf2Docx(String sourceFile, String targetFile, String downloadUrl, String fileId) {
        System.out.println("启动Pdf转word处理程序...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch doc = null;

        try {


            File inPath = new File(sourceFile);
            File outPath = new File(targetFile);


            //pdfActiveX PDDoc对象 主要建立PDF对象
            app = new ActiveXComponent("AcroExch.PDDoc");
            //PDF控制对象
            Dispatch pdfObject = app.getObject();
            //打开PDF文件，建立PDF操作的开始
            Dispatch.call(pdfObject, "Open", new Variant(inPath.getAbsolutePath()));
            Variant jsObj = Dispatch.call(pdfObject, "GetJSObject");
            Dispatch.call(jsObj.getDispatch(), "SaveAs", outPath.getPath(), "com.adobe.acrobat.docx");
            app.invoke("Close");


            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");

            //更新处理结果
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, downloadUrl, FileStatusEnum.SUCCESS, "文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {

            e.printStackTrace();
            if (app != null) {
                app.invoke("Close");
            }
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }

    }


    /**
     * excel转pdf 支持多sheet
     *
     * @param sourceFile
     * @param targetFile
     * @param downloadUrl
     * @param fileId
     */
    @Async
    public void excel2Pdf(String sourceFile, String targetFile, String downloadUrl, String fileId) {
        System.out.println("启动excel转pdf处理程序...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch excel = null;

        try {


            app = new ActiveXComponent("Excel.Application");
            app.setProperty("Visible", false);
            Dispatch excels = app.getProperty("Workbooks").toDispatch();
            excel = Dispatch.call(excels, "Open", sourceFile, false, true).toDispatch();
            Dispatch.call(excel, "ExportAsFixedFormat", xlTypePDF, targetFile);
            System.out.println("打开文档..." + sourceFile);
            System.out.println("excel转换文档到 PDF..." + targetFile);


            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");

            //更新处理结果
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, downloadUrl, FileStatusEnum.SUCCESS, "文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {

            e.printStackTrace();
            if (app != null) {
                app.invoke("Close");
            }
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }

    }

    /**
     * ppt转pdf
     *
     * @param sourceFile
     * @param targetFile
     * @param downloadUrl
     * @param fileId
     */
    @Async
    public void ppt2Pdf(String sourceFile, String targetFile, String downloadUrl, String fileId) {
        System.out.println("启动ppt转pdf处理程序...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch ppt = null;

        try {


            ComThread.InitSTA();
            app = new ActiveXComponent("PowerPoint.Application");
            Dispatch ppts = app.getProperty("Presentations").toDispatch();

            // 因POWER.EXE的发布规则为同步，所以设置为同步发布
            ppt = Dispatch.call(ppts, "Open", sourceFile,
                    // ReadOnly
                    true,
                    // Untitled指定文件是否有标题
                    true,
                    // WithWindow指定文件是否可见
                    false
            ).toDispatch();
            // ppSaveAsPDF为特定值32
            Dispatch.call(ppt, "SaveAs", targetFile, 32);

            System.out.println("ppt转换文档到 PDF..." + targetFile);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");

            //更新处理结果
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, downloadUrl, FileStatusEnum.SUCCESS, "文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {

            e.printStackTrace();
            if (app != null) {
                app.invoke("Close");
            }
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }

    }

}
