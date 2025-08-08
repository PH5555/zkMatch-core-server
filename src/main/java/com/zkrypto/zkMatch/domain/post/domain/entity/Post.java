package com.zkrypto.zkMatch.domain.post.domain.entity;

import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.global.utils.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID postId;

    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String majorRequirement;
    private String educationRequirement;
    private String experienceRequirement;

    @Convert(converter = StringListConverter.class)
    private List<String> licenseRequirement;
    private int salaryStart;
    private int salaryEnd;
    private String workSpace;
    private String workType;

    @Convert(converter = StringListConverter.class)
    private List<String> preferredSkill;

    @ManyToOne
    @JoinColumn(name = "corporation_id")
    private Corporation corporation;

    public Post(String title, String content, LocalDateTime startDate, LocalDateTime endDate, String majorRequirement, String educationRequirement, String experienceRequirement, List<String> licenseRequirement, int salaryStart, int salaryEnd, String workSpace, String workType, List<String> preferredSkill, Corporation corporation) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.majorRequirement = majorRequirement;
        this.educationRequirement = educationRequirement;
        this.experienceRequirement = experienceRequirement;
        this.licenseRequirement = licenseRequirement;
        this.salaryStart = salaryStart;
        this.salaryEnd = salaryEnd;
        this.workSpace = workSpace;
        this.workType = workType;
        this.preferredSkill = preferredSkill;
        this.corporation = corporation;
    }

    public static Post from(PostCreationCommand command, Corporation corporation) {
        return new Post(
                command.getTitle(),
                command.getContent(),
                command.getStartDate(),
                command.getEndDate(),
                command.getMajorRequirement(),
                command.getEducationRequirement(),
                command.getExperienceRequirement(),
                command.getLicenseRequirement(),
                command.getSalaryStart(),
                command.getSalaryEnd(),
                command.getWorkSpace(),
                command.getWorkType(),
                command.getPreferredSkill(),
                corporation
        );
    }
}
