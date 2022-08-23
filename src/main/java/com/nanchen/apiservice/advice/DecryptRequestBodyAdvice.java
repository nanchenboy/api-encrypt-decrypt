package com.nanchen.apiservice.advice;

import com.nanchen.apiservice.annotations.Decrypt;
import com.nanchen.apiservice.handler.EncryptAndDecryptHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @Author baofa
 * @Date 2022/8/21 16:32
 **/
@Component
@Slf4j
@ControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        String requestBody = getRequestBody(httpInputMessage);
        log.info("请求数据{}", requestBody);
        try {
            String decryptMessage = getDecryptBody(requestBody, methodParameter);
            requestBody = decryptMessage;
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new RuntimeException("解密请求参数失败");
        }
        return new DecryptHttpInputMessage(httpInputMessage.getHeaders(), new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    private String getRequestBody(HttpInputMessage httpInputMessage) {
        String body = "";
        try {
            InputStream inputStream = httpInputMessage.getBody();
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            body = new String(data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("获取请求参数失败");
        }
        return body;
    }

    private String getDecryptBody(String requestBody, MethodParameter methodParameter) throws IllegalAccessException, InstantiationException, UnsupportedEncodingException {
        Decrypt decrypt = methodParameter.getParameterAnnotation(Decrypt.class);
        Class<? extends EncryptAndDecryptHandler> handler = decrypt.handler();
        EncryptAndDecryptHandler encryptAndDecryptHandler = handler.newInstance();
        String decryptMessage = encryptAndDecryptHandler.decrypt(requestBody);
        return decryptMessage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class DecryptHttpInputMessage implements HttpInputMessage {
        HttpHeaders headers;
        InputStream body;
    }
}
