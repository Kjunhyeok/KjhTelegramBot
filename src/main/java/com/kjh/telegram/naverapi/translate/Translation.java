package com.kjh.telegram.naverapi.translate;

import com.kjh.telegram.naverapi.NaverAPI;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

import java.util.HashMap;
import java.util.Map;

public class Translation extends NaverAPI {

    public enum Language {
        ko("한"),
        en("영"),
        ja("일"),
        zh("중"),
        vi("베"),
        id("인"),
        th("태"),
        de("독"),
        ru("러"),
        es("스"),
        it("이"),
        fr("프"),
        ;

        private final String abbreviation;
        private static final Map<String, Language> lookup = new HashMap<>();

        static{
            for(Language lan : values()){
                lookup.put(lan.getAbbreviation(), lan);
            }
        }

        Language(String abbreviation){
            this.abbreviation = abbreviation;
        }

        public String getAbbreviation() {
            return abbreviation;
        }

        public static Language get(String abbreviation) {
            return lookup.get(abbreviation);
        }

        public static boolean contains(String abbreviation) {
            return lookup.containsKey(abbreviation);
        }
    }

    public static String translate(String message){
        try {
            HttpResponse<JsonNode> res = Unirest.post("https://openapi.naver.com/v1/papago/n2mt")
                    .headers(openAPIHeaders)
                    .field("source", Language.get(message.substring(0, 1)))
                    .field("target", Language.get(message.substring(1, 2)).toString())
                    .field("text", message.substring(3))
                    .asJson();
            return res.getBody().getObject().getJSONObject("message").getJSONObject("result").getString("translatedText");
        } catch (UnirestException e) {
            return "번역에 실패하였습니다.";
        }
    }
}
