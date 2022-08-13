package com.kjh.telegram.exchange;

import kong.unirest.Unirest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeApi {

    public static double exchange(String text, boolean won) {
        String crawl = Unirest.get("http://fx.kebhana.com/FER1101M.web").asString().getBody();
        JSONObject object = new JSONObject(crawl.replace("var exView = ", ""));
        JSONArray jsonArray = object.getJSONArray("리스트");
        Country country = Country.search(text);
        double price = Double.parseDouble(text.replace(country.getKor(), ""));
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.getString("통화명").contains(country.name())) {
                int divide = country == Country.JPY ? 100 : 1;
                if (won) {
                    return jsonObject.getDouble("매매기준율") * price / divide;
                } else {
                    return jsonObject.getDouble("매매기준율") / price / divide;
                }
            }
        }
        return price;
    }
}
