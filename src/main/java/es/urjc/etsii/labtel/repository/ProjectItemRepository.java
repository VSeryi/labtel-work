package es.urjc.etsii.labtel.repository;

import es.urjc.etsii.labtel.domain.ProjectItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ProjectItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectItemRepository extends JpaRepository<ProjectItem, Long> {

    @Query(value = "select distinct project_item from ProjectItem project_item left join fetch project_item.items",
        countQuery = "select count(distinct project_item) from ProjectItem project_item")
    Page<ProjectItem> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct project_item from ProjectItem project_item left join fetch project_item.items")
    List<ProjectItem> findAllWithEagerRelationships();

    @Query("select project_item from ProjectItem project_item left join fetch project_item.items where project_item.id =:id")
    Optional<ProjectItem> findOneWithEagerRelationships(@Param("id") Long id);

}
