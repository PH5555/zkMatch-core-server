package com.zkrypto.zkMatch.domain.resume.domain.constant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;

public class BaseVc {
    protected String ci;
    protected String name;

    public static boolean checkVcFormat(String data, ResumeType resumeType) {
        if (resumeType == ResumeType.EDUCATION) {
            return tryMappingVc(EducationVc.class, data);
        }
        else if(resumeType == ResumeType.EXPERIENCE) {
            return tryMappingVc(ExperienceVc.class, data);
        }
        else if(resumeType == ResumeType.LICENSE) {
            return tryMappingVc(LicenseVc.class, data);

        }
        else {
            return false;
        }
    }

    private static Boolean tryMappingVc(Object target, String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readValue(data, target.getClass());
            return true;
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.INVALID_VC_FORMAT);
        }
    }
}
