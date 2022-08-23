package com.nanchen.apiservice.advice;

import com.alibaba.fastjson.JSONObject;
import com.nanchen.apiservice.annotations.Encrypt;
import com.nanchen.apiservice.handler.EncryptAndDecryptHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Author baofa
 * @Date 2022/8/21 18:05
 * 仅加密成功时的消息
 **/
@Component
@ControllerAdvice
@Slf4j
public class EncryptSuccessResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return methodParameter.hasParameterAnnotation(Encrypt.class) || methodParameter.hasMethodAnnotation(Encrypt.class);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String responseInfo = JSONObject.toJSONString(body);
        return encryptResponseInfo(responseInfo,methodParameter);
    }

    @SneakyThrows
    private String encryptResponseInfo(String responseInfo, MethodParameter methodParameter) {
        Encrypt encrypt = methodParameter.getMethodAnnotation(Encrypt.class);
        Class<? extends EncryptAndDecryptHandler> handler = encrypt.handler();
        EncryptAndDecryptHandler encryptAndDecryptHandler = handler.newInstance();
        String encryptResponse = encryptAndDecryptHandler.encrypt(responseInfo);
        return encryptResponse;
    }
}
