package com.nanchen.apiservice.factory;

import com.nanchen.apiservice.annotations.EncryptException;
import com.nanchen.apiservice.handler.EncryptAndDecryptHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @Author baofa
 * @Date 2022/8/23 14:00
 * 这个构建工程可以放在yml文件里优化
 **/
@Component
public class EncyptAndDecryptFactory {

    private static HashMap<String,EncryptAndDecryptHandler> urlHandler = new HashMap<>();

    //带*的url集合
    private static final List<String> specialUrls = new ArrayList<>();

    private static String special = "/**";

    @Autowired
    ApplicationContext applicationContext;

    public EncryptAndDecryptHandler getHandler(String url){
        EncryptAndDecryptHandler handler = null;
        if (urlHandler.containsKey(url)){
            handler = urlHandler.get(url);
        }else {
            for (String specialUrl : specialUrls) {
                String deleteSpecial = specialUrl.replace(special, "");
                if (url.contains(deleteSpecial)) {
                    urlHandler.put(url,urlHandler.get(specialUrl));
                    handler = urlHandler.get(specialUrl);
                }
            }
        }
        return handler;
    }

    @PostConstruct
    public void build(){
        Map<String, EncryptAndDecryptHandler> handlerMap = applicationContext.getBeansOfType(EncryptAndDecryptHandler.class);
        for (String className : handlerMap.keySet()) {
            EncryptAndDecryptHandler handler = handlerMap.get(className);
            EncryptException encryptException = handler.getClass().getAnnotation(EncryptException.class);
            if (Objects.nonNull(encryptException)) {
                String[] urls = encryptException.urls();
                for (String url : urls) {
                    urlHandler.put(url,handler);
                    if (url.contains(special)){
                        specialUrls.add(url);
                    }
                }
            }
        }
    }
}
