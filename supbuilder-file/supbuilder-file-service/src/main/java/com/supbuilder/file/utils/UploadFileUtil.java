package com.supbuilder.file.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 矫克清
 */
public class UploadFileUtil {

    public static String uploadFile(MultipartFile file, String upLoadPath) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String fileUrl = null;
        //获取文件的输入流
        inputStream = file.getInputStream();
        //获取上传时的文件名
        String fileName = file.getOriginalFilename();
        File targetFile = new File(upLoadPath + fileName);
        //判断文件父目录是否存在
        if (!targetFile.getParentFile().exists()) {
            //不存在就创建一个
            targetFile.getParentFile().mkdirs();
        }
        file.transferTo(targetFile);
        fileUrl = upLoadPath + fileName;
        if (inputStream != null) {
            inputStream.close();

        }
        if (outputStream != null) {

            outputStream.close();

        }
        return fileUrl;
    }

    public static Boolean deleteUploadfile(String url) {
        Boolean res = true;
        File file = new File(url);
        if (file.exists()) {//文件是否存在
            res = file.delete();//删除文件
        }
        return res;
    }

    public static void downLoad(String url, String fileName, HttpServletResponse response) {
        OutputStream os = null;
        File file = new File(url);
        if (file.exists()) {//文件是否存在
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
                response.setContentType("application/force-download;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                //response.setHeader("Content-Disposition", "attachment;filename="+fileName);
                response.addHeader("Param", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (os != null) {
                    try {
                        os.flush();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /*
     * @Description:获取服务器某路径下全部文件名
     * @Author: SunMengFei
     * @Date: 2021/8/25 18:55
     * @Param: [filepath]
     * @Return: boolean
     */
    public static List<String> readAllFile(String filepath) {
        List<String> fileNames = new ArrayList<>();
        try {
            File file = new File(filepath);
            if (file.isDirectory()) {
                fileNames = Stream.of(file.list()).collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return fileNames;

    }

}
