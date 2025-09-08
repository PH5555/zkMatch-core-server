package com.zkrypto.zkMatch.domain.resume.domain.constant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class BaseVc {
    protected String ci;
    protected String name;

    public static boolean checkVcFormat(String data, ResumeType resumeType) {
        if (resumeType == ResumeType.EDUCATION) {
            tryMappingVc(EducationVc.class, data);
            return true;
        }
        else if(resumeType == ResumeType.EXPERIENCE) {
            tryMappingVc(ExperienceVc.class, data);
            return true;
        }
        else if(resumeType == ResumeType.LICENSE) {
            tryMappingVc(LicenseVc.class, data);
            return true;
        }
        else if(resumeType == ResumeType.PORTFOLIO) {
            tryMappingVc(PortfolioVc.class, data);
            return true;
        }
        else {
            return false;
        }
    }

    public static Object mappingVc(String data, ResumeType resumeType) {
        if (resumeType == ResumeType.EDUCATION) {
            return tryMappingVc(EducationVc.class, data);
        }
        else if(resumeType == ResumeType.EXPERIENCE) {
            return tryMappingVc(ExperienceVc.class, data);
        }
        else if(resumeType == ResumeType.LICENSE) {
            return tryMappingVc(LicenseVc.class, data);
        }
        else if(resumeType == ResumeType.PORTFOLIO) {
            return tryMappingVc(PortfolioVc.class, data);
        }
        else {
            throw new CustomException(ErrorCode.INVALID_VC_TYPE);
        }
    }

    private static Object tryMappingVc(Class<?> target, String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(data, target);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.INVALID_VC_FORMAT);
        }
    }
}
