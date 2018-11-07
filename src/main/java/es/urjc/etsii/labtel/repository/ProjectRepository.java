package es.urjc.etsii.labtel.repository;

import es.urjc.etsii.labtel.domain.Project;
import es.urjc.etsii.labtel.service.dto.ProjectDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	Page<Project> findAllByIdIn(List<Long> projectIds, Pageable pageable);

}
