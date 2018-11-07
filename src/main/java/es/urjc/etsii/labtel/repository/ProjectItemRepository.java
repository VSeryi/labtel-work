package es.urjc.etsii.labtel.repository;

import es.urjc.etsii.labtel.domain.ProjectItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProjectItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectItemRepository extends JpaRepository<ProjectItem, Long> {

}
