package com.kjh.telegram.naverapi.search;

import com.kjh.telegram.naverapi.NaverAPI;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class WebDocument extends NaverAPI {

    public static String search(String message){
        try {
            HttpResponse<JsonNode> res = Unirest.get("https://openapi.naver.com/v1/search/news.json")
                    .headers(openAPIHeaders)
                    .queryString("query", message)
                    .queryString("display", 5)
                    .asJson();
            JSONArray jsonArray = res.getBody().getObject().getJSONArray("items");
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                builder.append("<a href=\"").append(object.getString("originallink")).append("\">").append(object.getString("title").replace("<b>", "").replace("</b>", "")).append("</a>")
                        .append("\n")
                        .append(object.getString("description"))
                        .append("\n");
            }
            return builder.toString();
        } catch (Exception e) {
            return "검색 결과를 찾을 수 없습니다.";
        }
    }
}
