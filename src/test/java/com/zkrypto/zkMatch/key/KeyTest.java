package com.zkrypto.zkMatch.key;

import com.zkrypto.zkMatch.domain.ca.application.service.CaService;
import com.zkrypto.zkMatch.domain.ca.application.service.SnarkCaServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KeyTest {
    @Autowired
    SnarkCaServiceImpl caService;

    @Test
    void 키_가져오기_테스트() {
        String verifyKey = caService.getVerifyKey();
        Assertions.assertThat(verifyKey.length()).isNotEqualTo(0);
    }
}
