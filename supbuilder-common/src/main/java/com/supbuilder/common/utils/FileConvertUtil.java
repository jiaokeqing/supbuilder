package com.supbuilder.common.utils;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/**
 * 文件格式转换工具类
 *
 * @version 1.0
 * @since JDK1.8
 */
public class FileConvertUtil {
    /** 默认转换后文件后缀 */
    private static final String DEFAULT_SUFFIX = "pdf";
    /** openoffice_port */
    private static final Integer OPENOFFICE_PORT = 8100;

    /**
     * 方法描述 office文档转换为PDF(处理本地文件)
     *
     * @param sourcePath 源文件路径
     * @param suffix     源文件后缀
     * @return InputStream 转换后文件输入流
     */
    public static InputStream convertLocaleFile(String sourcePath, String suffix) throws Exception {
        File inputFile = new File(sourcePath);
        InputStream inputStream = new FileInputStream(inputFile);
        return covertCommonByStream(inputStream, suffix);
    }



    /**
     * 方法描述  office文档转换为PDF(处理网络文件)
     *
     * @param netFileUrl 网络文件路径
     * @param suffix     文件后缀
     * @return InputStream 转换后文件输入流
     */
    public static InputStream convertNetFile(String netFileUrl, String suffix) throws Exception {
        // 创建URL
        URL url = new URL(netFileUrl);
        // 试图连接并取得返回状态码
        URLConnection urlconn = url.openConnection();
        urlconn.connect();
        HttpURLConnection httpconn = (HttpURLConnection) urlconn;
        int httpResult = httpconn.getResponseCode();
        if (httpResult == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = urlconn.getInputStream();
            return covertCommonByStream(inputStream, suffix);
        }
        return null;
    }

    /**
     * 方法描述  将文件以流的形式转换
     *
     * @param inputStream 源文件输入流
     * @param suffix      源文件后缀
     * @return InputStream 转换后文件输入流
     */
    public static InputStream covertCommonByStream(InputStream inputStream, String suffix) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(OPENOFFICE_PORT);
        connection.connect();
        DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
        DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
        DocumentFormat targetFormat = formatReg.getFormatByFileExtension(DEFAULT_SUFFIX);
        DocumentFormat sourceFormat = formatReg.getFormatByFileExtension(suffix);
        converter.convert(inputStream, sourceFormat, out, targetFormat);
        connection.disconnect();
        return outputStreamConvertInputStream(out);
    }

    /**
     * 方法描述 outputStream转inputStream
     *
     */
    public static ByteArrayInputStream outputStreamConvertInputStream(final OutputStream out) throws Exception {
        ByteArrayOutputStream baos=(ByteArrayOutputStream) out;
        return new ByteArrayInputStream(baos.toByteArray());
    }

}
