package com.supbuilder.common.constant;

/**
 * 统一处理码
 * @author jiaokeqing
 */
public interface CommonConstant {

	/** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    Integer SC_INTERNAL_SERVER_ERROR_500 = 500;

    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    Integer SC_OK_200 = 200;
    
    /**表示发送的请求没有通过 HTTP 认证的认证信息 401*/
    Integer SC_UNAUTHORZIED_401 = 401;

    /**表示对请求资源的访问被服务器拒绝*/
    Integer SC_FORBIDDEN_403 =403;


    /** Token缓存时间：3600秒即一小时 */
    int  TOKEN_EXPIRE_TIME  = 3600;

    /**表示对请求资源的访问被服务器拒绝*/
   Integer USER_NOT_REGISTER =20005;
    

}
