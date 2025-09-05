package com.zkrypto.zkMatch.domain.corporation.domain.constant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.global.utils.BaseDigestUtil;
import com.zkrypto.zkMatch.global.utils.DateFormatter;
import com.zkrypto.zkMatch.global.utils.HexUtil;
import com.zkrypto.zkMatch.global.utils.JsonUtil;
import lombok.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveUserInfoReqDto {
    private String firstname;
    private String lastname;
    private String did;
    private String email;
    private String pii;

    private String vcSchemaId;
    private String vcSchemaTitle;
    private String vcSchemaIndex;

    private Map<String, String> fields;

    private String userInfo;

    public static SaveUserInfoReqDto from(Recruit recruit, String vcSchemaId) {
        String lastname = recruit.getMember().getName().substring(0, 1);
        String firstname = recruit.getMember().getName().substring(1);
        return SaveUserInfoReqDto.builder()
                .firstname(firstname)
                .lastname(lastname)
                .pii(generatePii(firstname, lastname))
                .vcSchemaId(vcSchemaId)
                .userInfo(getUserInfo(recruit))
                .build();
    }

    private static String generatePii(String firstname, String lastname) {
        String json = JsonUtil.serializeAndSort(SerializeUserInfoData.builder()
                .firstname(firstname)
                .lastname(lastname)
                .build());

        byte[] hashedDataBytes = BaseDigestUtil.generateHash(json.getBytes(StandardCharsets.UTF_8));
        return HexUtil.toHexString(hashedDataBytes);
    }

    private static String getUserInfo(Recruit recruit) {
        Map<String, String> fields = new HashMap<>();
        fields.put("prjName", recruit.getPost().getTitle());
        fields.put("startDate", DateFormatter.format(recruit.getPost().getStartDate()));
        fields.put("endDate", DateFormatter.format(recruit.getCreatedAt()));

        ObjectMapper objectMapper = new ObjectMapper();
        String userInfoJson = null;
        try {
            userInfoJson = objectMapper.writeValueAsString(fields);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return userInfoJson;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class SerializeUserInfoData {
        private String lastname;
        private String firstname;
    }
}
