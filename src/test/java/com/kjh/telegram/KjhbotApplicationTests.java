package com.kjh.telegram;

import com.kjh.telegram.naverapi.translate.Translation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KjhbotApplicationTests {

    @Test
    public void contextLoads() {
        String str = "한영 개구리";

        assert Translation.Language.contains(str.substring(0, 1));
    }

}
