package com.zkrypto.zkMatch.resume;

import com.zkrypto.zkMatch.domain.resume.domain.constant.BaseVc;
import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Rollback
public class ResumeTest {

    @Test
    void VC_변환_테스트() {
        String data = "{\n" +
                "    \"ci\": \"dkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdkdk\",\n" +
                "    \"name\": \"김동현\",\n" +
                "    \"pid\": \"020306-0000000\",\n" +
                "    \"license\": \"정보처리기사\",\n" +
                "    \"expired\": \"2030-05-30\"\n" +
                "}";
        boolean res = BaseVc.checkVcFormat(data, ResumeType.LICENSE);
        Assertions.assertThat(res).isTrue();
    }
}
