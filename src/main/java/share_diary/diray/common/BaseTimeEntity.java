package share_diary.diray.common;

import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    protected LocalDateTime createDate;

    @CreatedBy
    protected String createBy;

    @LastModifiedDate
    protected LocalDateTime lastModifyDate;

    @LastModifiedBy
    protected String lastModifiedBy;
}
