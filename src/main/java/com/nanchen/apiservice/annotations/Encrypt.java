package com.nanchen.apiservice.annotations;

import com.nanchen.apiservice.handler.EncryptAndDecryptHandler;
import com.nanchen.apiservice.handler.NBBankHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.PARAMETER,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypt {
    Class<? extends EncryptAndDecryptHandler> handler() default NBBankHandler.class;
}
