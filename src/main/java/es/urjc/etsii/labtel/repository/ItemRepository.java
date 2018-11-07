package es.urjc.etsii.labtel.repository;

import es.urjc.etsii.labtel.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "select distinct item from Item item left join fetch item.elements",
        countQuery = "select count(distinct item) from Item item")
    Page<Item> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct item from Item item left join fetch item.elements")
    List<Item> findAllWithEagerRelationships();

    @Query("select item from Item item left join fetch item.elements where item.id =:id")
    Optional<Item> findOneWithEagerRelationships(@Param("id") Long id);

}
