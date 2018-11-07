package es.urjc.etsii.labtel.repository;

import es.urjc.etsii.labtel.domain.ProjectPermission;
import es.urjc.etsii.labtel.service.dto.ProjectPermissionDTO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProjectPermission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectPermissionRepository extends JpaRepository<ProjectPermission, Long> {

	List<ProjectPermission> findByUserLogin(String userLogin);

	List<ProjectPermission> findByProjectId(Long projectId);
	
}
