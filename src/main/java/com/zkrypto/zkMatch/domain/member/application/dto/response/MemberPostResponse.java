package com.zkrypto.zkMatch.domain.member.application.dto.response;

import com.zkrypto.zkMatch.domain.post.application.dto.response.PostResponse;
import com.zkrypto.zkMatch.domain.post.domain.entity.Post;
import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import com.zkrypto.zkMatch.global.utils.DateFormatter;
import lombok.Getter;

@Getter
public class MemberPostResponse extends PostResponse {
    private Status status;
    private String applyDate;

    public MemberPostResponse(Recruit recruit) {
        super(
                recruit.getPost().getPostId().toString(),
                recruit.getPost().getTitle(),
                recruit.getPost().getContent(),
                DateFormatter.format(recruit.getPost().getStartDate()),
                DateFormatter.format(recruit.getPost().getEndDate()),
                recruit.getPost().getMajorRequirement(),
                recruit.getPost().getEducationRequirement(),
                recruit.getPost().getExperienceRequirement() + "년 이상",
                recruit.getPost().getLicenseRequirement(),
                recruit.getPost().getSalaryStart(),
                recruit.getPost().getSalaryEnd(),
                recruit.getPost().getWorkSpace(),
                recruit.getPost().getWorkType(),
                recruit.getPost().getCategory(),
                recruit.getPost().getCorporation().getCorporationName()
        );
        this.status = recruit.getStatus();
        this.applyDate = DateFormatter.format(recruit.getCreatedAt());
    }

    public static MemberPostResponse from(Recruit recruit) {
        return new MemberPostResponse(recruit);
    }
}
