package share_diary.diray.common.mapper;

import javax.persistence.EntityManager;

public class ReferenceMapper {

    private final EntityManager entityManager;

    public ReferenceMapper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
