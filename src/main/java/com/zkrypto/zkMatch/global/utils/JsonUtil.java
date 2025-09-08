package com.zkrypto.zkMatch.global.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String serializeAndSort(Object obj) {
        try{
            String jsonString = mapper.writeValueAsString(obj);
            ObjectNode node = (ObjectNode) mapper.readTree(jsonString);
            jsonString = mapper.writeValueAsString(node);
            return jsonString.replaceAll("\\s", "");
        }
        catch (Exception e) {
            log.info(e.getMessage());
            throw new CustomException(ErrorCode.VP_JSON_FORMAT_ERROR);
        }
    }

    public static String serializeToJson(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }
}
