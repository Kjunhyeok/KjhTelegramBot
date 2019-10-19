package com.kjh.telegram.naverapi.search;

import com.kjh.telegram.naverapi.NaverAPI;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Dictionary extends NaverAPI {

    public static String search(String message){
        try {
            HttpResponse<JsonNode> res = Unirest.get("https://openapi.naver.com/v1/search/encyc.json")
                    .headers(openAPIHeaders)
                    .queryString("query", message)
                    .asJson();
            JSONArray jsonArray = res.getBody().getObject().getJSONArray("items");
            return jsonArray.getJSONObject(0).getString("link");
        } catch (Exception e) {
            e.printStackTrace();
            return "검색 결과를 찾을 수 없습니다.";
        }
    }
}
