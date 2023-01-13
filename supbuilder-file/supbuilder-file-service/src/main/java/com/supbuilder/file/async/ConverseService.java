package com.supbuilder.file.async;

import cn.hutool.core.io.file.FileNameUtil;
import com.aspose.pdf.SaveFormat;
import com.aspose.pdf.TextAbsorber;
import com.aspose.pdf.devices.PngDevice;
import com.aspose.pdf.devices.Resolution;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.supbuilder.common.core.constant.FileHandleTypeConstants;

import com.supbuilder.common.core.constant.FileTypeSuffixConstants;
import com.supbuilder.common.core.util.RedisUtil;
import com.supbuilder.file.api.constant.FileStatusEnum;
import com.supbuilder.file.api.vo.FileHandleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

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

    /**
     * 图片转pdf
     *
     * @param sourceFile
     * @param targetFile
     * @param downloadUrl
     * @param fileId
     */
    @Async
    public void img2Pdf(List<String> sourceFile, String targetFile, String downloadUrl, String fileId) {
        System.out.println("启动图片转pdf处理程序...");
        long start = System.currentTimeMillis();


        try {

            File file = new File(targetFile);
            // 第一步：创建一个document对象。
            Document document = new Document();
            document.setMargins(0, 0, 0, 0);
            // 第二步：
            // 创建一个PdfWriter实例，
            PdfWriter.getInstance(document, new FileOutputStream(file));
            // 第三步：打开文档。
            document.open();
            // 第四步：在文档中增加图片。


            for (int i = 0; i < sourceFile.size(); i++) {
                if (sourceFile.get(i).toLowerCase().endsWith(".bmp")
                        || sourceFile.get(i).toLowerCase().endsWith(".jpg")
                        || sourceFile.get(i).toLowerCase().endsWith(".jpeg")
                        || sourceFile.get(i).toLowerCase().endsWith(".gif")
                        || sourceFile.get(i).toLowerCase().endsWith(".png")) {
                    Image img = Image.getInstance(sourceFile.get(i));
                    img.setAlignment(Image.ALIGN_CENTER);
                    // 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
                    document.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
                    document.newPage();
                    document.add(img);
                }
            }
            // 第五步：关闭文档。
            document.close();


            System.out.println("图片转换文档到 PDF..." + targetFile);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");

            //更新处理结果
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, downloadUrl, FileStatusEnum.SUCCESS, "文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {

            e.printStackTrace();
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }

    }


    /**
     * pdf转pptx
     *
     * @param sourceFile
     * @param targetFile
     * @param downloadUrl
     * @param fileId
     */
    @Async
    public void pdf2Ppt(String sourceFile, String targetFile, String downloadUrl, String fileId) {
        System.out.println("启动pdf转ppt处理程序...");
        long start = System.currentTimeMillis();


        try {

            FileOutputStream os = new FileOutputStream(targetFile);
            com.aspose.pdf.Document doc = new com.aspose.pdf.Document(sourceFile);
            doc.save(os, SaveFormat.Pptx);
            os.close();


            System.out.println("pdf转换文档到ppt..." + targetFile);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");

            //更新处理结果
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, downloadUrl, FileStatusEnum.SUCCESS, "文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {

            e.printStackTrace();
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }

    }


    /**
     * pdf转excel xlsx
     *
     * @param sourceFile
     * @param targetFile
     * @param downloadUrl
     * @param fileId
     */
    @Async
    public void pdf2Excel(String sourceFile, String targetFile, String downloadUrl, String fileId) {
        System.out.println("启动pdf转excel处理程序...");
        long start = System.currentTimeMillis();


        try {

            FileOutputStream os = new FileOutputStream(targetFile);
            com.aspose.pdf.Document doc = new com.aspose.pdf.Document(sourceFile);
            doc.save(os, SaveFormat.Excel);
            os.close();


            System.out.println("pdf转换文档到excel..." + targetFile);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");

            //更新处理结果
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, downloadUrl, FileStatusEnum.SUCCESS, "文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {

            e.printStackTrace();
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }

    }

    /**
     * pdf转图片
     *
     * @param sourceFile
     * @param targetDir
     * @param downloadUrl 拆分后的图片zip
     * @param fileId
     * @param suffix      png/jpeg
     */
    @Async
    public void pdf2Img(String sourceFile, String targetDir, String downloadUrl, String fileId, String suffix) {
        System.out.println("启动pdf转图片处理程序...");
        long start = System.currentTimeMillis();


        try {

            Resolution resolution = new Resolution(300);
            File imageDir = new File(targetDir);
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            com.aspose.pdf.Document doc = new com.aspose.pdf.Document(sourceFile);
            PngDevice pngDevice = new PngDevice(resolution);
            for (int pageCount = 1; pageCount <= doc.getPages().size(); pageCount++) {
                OutputStream imageStream = new FileOutputStream(imageDir + "/" + pageCount + suffix);
                pngDevice.process(doc.getPages().get_Item(pageCount), imageStream);
                imageStream.close();
            }


            System.out.println("pdf转换文档到图片..." + targetDir);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");


            //todo 将解析后的图片进行压缩


            //更新处理结果
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, downloadUrl, FileStatusEnum.SUCCESS, "文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {

            e.printStackTrace();
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }

    }

    /**
     * pdf转txt
     *
     * @param sourceFile
     * @param targetFile
     * @param downloadUrl
     * @param fileId
     */
    @Async
    public void pdf2Txt(String sourceFile, String targetFile, String downloadUrl, String fileId) {
        System.out.println("启动pdf转txt处理程序...");
        long start = System.currentTimeMillis();
        com.aspose.pdf.Document pdfDocument = new com.aspose.pdf.Document(sourceFile);
        TextAbsorber ta = new TextAbsorber();
        ta.visit(pdfDocument);

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
            writer.write(ta.getText());
            writer.close();


            System.out.println("pdf转换文档到txt..." + targetFile);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");


            //todo 将转化后的文件压缩为zip压缩包


            //更新处理结果
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, downloadUrl, FileStatusEnum.SUCCESS, "文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {

            e.printStackTrace();
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }

    }

    /**
     * pdf转html
     *
     * @param sourceFile
     * @param targetFile
     * @param downloadUrl
     * @param fileId
     */
    @Async
    public void pdf2Html(String sourceFile, String targetFile, String downloadUrl, String fileId) {
        System.out.println("启动pdf转html处理程序...");
        long start = System.currentTimeMillis();


        try {
            com.aspose.pdf.Document doc = new com.aspose.pdf.Document(sourceFile);
            doc.save(targetFile, SaveFormat.Html);

            System.out.println("pdf转换文档到html..." + targetFile);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");


            //更新处理结果
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, downloadUrl, FileStatusEnum.SUCCESS, "文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {

            e.printStackTrace();
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }

    }


    /**
     * pdf合并
     *
     * @param sourceFile
     * @param targetFile
     * @param downloadUrl
     * @param fileId
     */
    @Async
    public void pdfMerge(List<String> sourceFile, String targetFile, String downloadUrl, String fileId) {
        System.out.println("启动pdf合并处理程序...");
        long start = System.currentTimeMillis();


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document document = new Document();// 创建一个新的PDF
        byte[] pdfs = new byte[0];
        try {
            PdfCopy copy = new PdfCopy(document, bos);
            document.open();
            for (String sourceFileUrl : sourceFile) {// 取出单个PDF的数据
                InputStream is = new FileInputStream(sourceFileUrl);
                PdfReader reader = new PdfReader(inputStream2byte(is));
                int pageTotal = reader.getNumberOfPages();
                for (int pageNo = 1; pageNo <= pageTotal; pageNo++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, pageNo);
                    copy.addPage(page);
                }
                reader.close();
            }
            document.close();
            pdfs = bos.toByteArray();
            bos.close();
            copy.close();

            OutputStream outputStream = new FileOutputStream(targetFile);
            outputStream.write(pdfs);
            outputStream.close();
            System.out.println("pdf合并文档..." + targetFile);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");


            //更新处理结果
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, downloadUrl, FileStatusEnum.SUCCESS, "文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {
            e.printStackTrace();
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }

    }


    @Async
    public void pdfSplit(String sourceFile,String targetFile, String downloadUrl, String fileId,int splitSize) {
        System.out.println("启动pdf按页拆分处理程序...");
        long start = System.currentTimeMillis();
        PdfReader reader;

        String name = FileNameUtil.mainName(sourceFile);

        try {
            reader = new PdfReader(sourceFile);

            int numberOfPages = reader.getNumberOfPages();
            int newFileCount = 0;
            // PageNumber是从1开始计数的
            int pageNumber = 1;
            while (pageNumber <= numberOfPages) {
                Document doc = new Document();
                String splitFileName = targetFile.substring(0, targetFile.length() - 4)  + "_" + newFileCount + FileTypeSuffixConstants.PDF_SUFFIX;
                System.out.println(splitFileName);
                PdfCopy  pdfCopy = new PdfCopy(doc, new FileOutputStream(splitFileName));
                doc.open();
                // 将pdf按页复制到新建的PDF中
                for (int i = 1; pageNumber <= numberOfPages && i <= splitSize; ++i, pageNumber++) {
                    doc.newPage();
                    PdfImportedPage page = pdfCopy.getImportedPage(reader, pageNumber);
                    pdfCopy.addPage(page);
                }
                doc.close();
                newFileCount++;
                pdfCopy.close();
            }
            System.out.println("pdf按页拆分文档..." + targetFile);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");


            //todo  压缩为zip

            //更新处理结果
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, downloadUrl, FileStatusEnum.SUCCESS, "文件处理成功");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        } catch (Exception e) {
            e.printStackTrace();
            FileHandleVO fileHandleVO = new FileHandleVO(fileId, null, FileStatusEnum.FAIL, "文件处理失败");
            redisUtil.hset(FileHandleTypeConstants.FILE_CONVERSE, fileId, fileHandleVO, 1800);
        }

    }




    private byte[] inputStream2byte(InputStream inputStream) {
        byte[] buffer = new byte[0];
        try (InputStream fis = inputStream;
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
