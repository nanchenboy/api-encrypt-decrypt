package com.nanchen.apiservice.handler;

/**
 * @Author baofa
 * @Date 2022/8/21 17:42
 **/
public interface EncryptAndDecryptHandler {
    
    /**
     *  解密请求数据
     **/
    String decrypt(String requestbody);
    
    /**
     *  加密响应数据
     **/
    String encrypt(String reponse);
}
