package com.kjh.telegram.naverapi.cloud;

import com.kjh.telegram.naverapi.NaverAPI;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Maps extends NaverAPI {

    public static File get(String message) throws IOException, JSONException {
        HttpResponse<JsonNode> res = Unirest.get("https://naveropenapi.apigw.ntruss.com/map-place/v1/search")
                .headers(cloudAPIHeaders)
                .queryString("coordinate", "127.1054221,37.3591614")
                .queryString("query", message)
                .asJson();
        JSONObject object = res.getBody().getObject().getJSONArray("places").getJSONObject(0);
        String x = object.getString("x");
        String y = object.getString("y");

        HttpResponse<File> resFile = Unirest.get("https://naveropenapi.apigw.ntruss.com/map-static/v2/raster")
                .headers(cloudAPIHeaders)
                .queryString("w", 800)
                .queryString("h", 600)
                .queryString("center", x + "," + y)
                .queryString("level", 16)
                .asFile("D:\\telegrambotdata/" + System.currentTimeMillis() + ".png");
        File file = resFile.getBody();
        file.createNewFile();
        return file;
    }
}
