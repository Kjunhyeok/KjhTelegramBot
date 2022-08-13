package com.kjh.telegram.exchange;

import lombok.Getter;

@Getter
public enum Country {
    USD("달러"),
    JPY("엔"),
    CNY("위안"),
    EUR("유로"),
    NONE("미추가")
    ;

    private final String kor;

    Country(String kor) {
        this.kor = kor;
    }

    public static Country search(String kor) {
        for (Country country : values()) {
            if (kor.contains(country.kor)) {
                return country;
            }
        }
        return NONE;
    }
}