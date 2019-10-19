package com.kjh.telegram.naverapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class NaverAPI {

    @Value("${naver.secret}")
    private String secret;

    @Value("${naver.id}")
    private String id;

    @Value("${naver.cloud.secret}")
    private String cloudSecret;

    @Value("${naver.cloud.id}")
    private String cloudId;

    protected static Map<String, String> openAPIHeaders = new HashMap<>();
    protected static Map<String, String> cloudAPIHeaders = new HashMap<>();

    @PostConstruct
    private void init(){
        openAPIHeaders.put("X-Naver-Client-Id", id);
        openAPIHeaders.put("X-Naver-Client-Secret", secret);
        cloudAPIHeaders.put("X-NCP-APIGW-API-KEY", cloudSecret);
        cloudAPIHeaders.put("X-NCP-APIGW-API-KEY-ID", cloudId);
    }
}
