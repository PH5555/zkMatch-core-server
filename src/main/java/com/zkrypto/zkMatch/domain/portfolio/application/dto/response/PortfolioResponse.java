package com.zkrypto.zkMatch.domain.portfolio.application.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class PortfolioResponse {
    private String name;
    private String email;
    private String phoneNumber;
    private Boolean isIDVerified;
    private Visa visa; //did
    private String koreanSkillGrade; //did
    private List<LanguageSkill> languageSkills;
    private List<JobHistory> jobHistories; //did
    private List<SchoolHistory> schoolHistories; //did
    private List<License> licenses; //did
    private String personalStatement;

    @Getter
    public static class Visa {
        private String visaType;
        private String expiredDate;
    }

    @Getter
    public static class LanguageSkill {
        private String language;
        private String level;
    }

    @Getter
    public static class JobHistory {
        private String corporationName;
        private String duration;
        private String duty;
    }

    @Getter
    public static class SchoolHistory {
        private String schoolName;
        private String duration;
        private String major;
        private String type;
    }

    @Getter
    public static class License {
        private String licenseName;
        private String issuer;
        private String issueDate;
    }
}
