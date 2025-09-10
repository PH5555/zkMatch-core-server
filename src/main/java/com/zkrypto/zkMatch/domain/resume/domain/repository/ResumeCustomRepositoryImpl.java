package com.zkrypto.zkMatch.domain.resume.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import com.zkrypto.zkMatch.domain.resume.domain.entity.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.zkrypto.zkMatch.domain.resume.domain.entity.QResume.resume;

@Repository
@RequiredArgsConstructor
public class ResumeCustomRepositoryImpl implements ResumeCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Resume> findCandidateResume(List<String> licenses, Integer employPeriod, String educationType) {
        BooleanBuilder builder = new BooleanBuilder();
        if (licenses != null && !licenses.isEmpty()) {
            builder.or(resume.resumeType.eq(ResumeType.LICENSE));
        }
        if (employPeriod != null && employPeriod > 0) {
            builder.or(resume.resumeType.eq(ResumeType.EXPERIENCE));
        }
        if (educationType != null && !educationType.isEmpty()) {
            builder.or(resume.resumeType.eq(ResumeType.EDUCATION));
        }
        return jpaQueryFactory.selectFrom(resume)
                .where(builder).fetch();
    }
}
