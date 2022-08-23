package com.nanchen.apiservice.annotations;

import com.nanchen.apiservice.handler.EncryptAndDecryptHandler;
import com.nanchen.apiservice.handler.NBBankHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author baofa
 * @Date 2022/8/21 16:30
 **/
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Decrypt {
    Class<? extends EncryptAndDecryptHandler> handler() default NBBankHandler.class;
}