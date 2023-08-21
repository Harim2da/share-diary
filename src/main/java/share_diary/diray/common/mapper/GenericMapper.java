package share_diary.diray.common.mapper;

import java.util.List;
import java.util.Set;
import org.mapstruct.IterableMapping;
import org.mapstruct.Named;

public interface GenericMapper<D, E> {

    E asEntity(D dto);

    @Named(value = "asDTO")
    D asDTO(E entity);

    List<E> asEntityList(List<D> dtoList);

    @IterableMapping(qualifiedByName = "asDTO")
    List<D> asDTOList(List<E> entityList);

    @IterableMapping(qualifiedByName = "asDTO")
    Set<D> asDTOSet(Set<E> entitySet);
}
