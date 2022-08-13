package com.kjh.telegram.naverapi;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class NaverAPI {

    private String secret;

    private String id;

    private String cloudSecret;

    private String cloudId;

    protected static Map<String, String> openAPIHeaders = new HashMap<>();
    protected static Map<String, String> cloudAPIHeaders = new HashMap<>();

    @PostConstruct
    private void init(){
        openAPIHeaders.put("X-Naver-Client-Id", "");
        openAPIHeaders.put("X-Naver-Client-Secret", "");
        cloudAPIHeaders.put("X-NCP-APIGW-API-KEY", "");
        cloudAPIHeaders.put("X-NCP-APIGW-API-KEY-ID", "");
    }
}
