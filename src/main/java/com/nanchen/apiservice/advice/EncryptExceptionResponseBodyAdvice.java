package com.nanchen.apiservice.advice;

import com.alibaba.fastjson.JSONObject;
import com.nanchen.apiservice.annotations.Encrypt;
import com.nanchen.apiservice.factory.EncyptAndDecryptFactory;
import com.nanchen.apiservice.handler.EncryptAndDecryptHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @Author baofa
 * @Date 2022/8/21 18:05
 * 仅加密异常响应时的消息
 **/
@Component
@ControllerAdvice
@Slf4j
public class EncryptExceptionResponseBodyAdvice implements ResponseBodyAdvice {

    @Autowired
    EncyptAndDecryptFactory encyptAndDecryptFactory;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return methodParameter.hasParameterAnnotation(ExceptionHandler.class) || methodParameter.hasMethodAnnotation(ExceptionHandler.class);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String responseInfo = JSONObject.toJSONString(body);
        String path = serverHttpRequest.getURI().getPath();
        return encryptResponseInfo(responseInfo,path);
    }

    @SneakyThrows
    private String encryptResponseInfo(String responseInfo, String requestURI) {
        EncryptAndDecryptHandler handler = encyptAndDecryptFactory.getHandler(requestURI);
        if (Objects.nonNull(handler)) {
            String encryptResponse = handler.encrypt(responseInfo);
            return encryptResponse;
        }else {
            return responseInfo;
        }
    }
}
