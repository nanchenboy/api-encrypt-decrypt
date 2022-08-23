package com.nanchen.apiservice.handler;

import org.springframework.stereotype.Component;

/**
 * @Author baofa
 * @Date 2022/8/23 13:30
 **/
@Component
public class NBBankHandler implements EncryptAndDecryptHandler {
    @Override
    public String decrypt(String requestbody) {
        return "这是解密请求数据";
    }

    @Override
    public String encrypt(String reponse) {
        return "这是加密响应数据";
    }
}
