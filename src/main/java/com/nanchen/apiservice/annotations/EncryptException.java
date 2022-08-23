package com.nanchen.apiservice.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author baofa
 * @Date 2022/8/22 13:40
 * 需加密的url /test/**代表加密所有以test开头的地址
 **/
@Target({ElementType.METHOD,ElementType.PARAMETER,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptException {
//    Class<? extends EncryptAndDecryptHandler> handler() default NBBankHandler.class;
    String[] urls();
}
