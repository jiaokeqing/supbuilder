package com.supbuilder.common.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 向指定URL发送GET/POST请求
 * @author jiaokeqing
 */
public class HttpUtils {
    /**
     * 获取可信任https链接，以避免不受信任证书出现peer not authenticated异常
     *
     * @param base
     * @return
     */
    public static HttpClient wrapClient(HttpClient base) {

        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs,String string) {
                }
                public void checkServerTrusted(X509Certificate[] xcs,String string) {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * http get请求
     * @param url 请求地址
     * @param headers 请求头
     * @param pmap 请求参数
     * @param ishttps 是否使用https  true:使用 false:不使用
     * @return
     */
    public static String sendHttpsGet(String url,Map<String, String> headers,Map<String, String> pmap,boolean ishttps){
        HttpClient client = new DefaultHttpClient();
        if(ishttps){
            client = wrapClient(client);
        }
        // 实例化HTTP方法
        HttpGet get = new HttpGet();
        for(String keyh : headers.keySet()) {
            get.setHeader(keyh,headers.get(keyh));
        }
        String params = "";
        for(String keyp : pmap.keySet()) {
            params += "&" + keyp + "=" + pmap.get(keyp);
        }
        url += params.replaceAll("^&", "?");
        String result ="";
        try {
            get.setURI(new URI(url));
            HttpResponse response = client.execute(get);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            result=new String(result.getBytes("ISO-8859-1"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;

    }

    /**
     * http post请求
     * @param url 请求地址
     * @param headers 请求头
     * @param pmap 请求参数
     * @param ishttps 是否使用https  true:使用 false:不使用
     * @return  HttpEntity  使用org.apache.http.util.EntityUtils.toString()、com.alibaba.fastjson.JSON.parseObject()解析
     */
    public static HttpEntity sendHttpsPost(String url,Map<String, String> headers,Map<String, String> pmap,boolean ishttps){
        HttpClient client = new DefaultHttpClient();
        if(ishttps){
            client = wrapClient(client);
        }
        HttpPost postrequest = new HttpPost(url);
        HttpEntity entity = null;
        try {
            for(String keyh : headers.keySet()) {
                postrequest.setHeader(keyh, headers.get(keyh));
            }
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for(String key : pmap.keySet()) {
                nvps.add(new BasicNameValuePair(key,pmap.get(key)));
            }

            postrequest.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            HttpResponse execute = client.execute(postrequest);
            entity = execute.getEntity();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    public static String get(String url){
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                HttpGet httpGet = new HttpGet(url);

                client = HttpClients.createDefault();
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                System.out.println(result);
                return result;
            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}