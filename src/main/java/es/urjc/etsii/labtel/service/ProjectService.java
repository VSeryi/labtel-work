package es.urjc.etsii.labtel.service;

import es.urjc.etsii.labtel.domain.Project;
import es.urjc.etsii.labtel.domain.ProjectItem;
import es.urjc.etsii.labtel.domain.User;
import es.urjc.etsii.labtel.repository.ProjectRepository;
import es.urjc.etsii.labtel.security.AuthoritiesConstants;
import es.urjc.etsii.labtel.security.SecurityUtils;
import es.urjc.etsii.labtel.service.dto.ProjectDTO;
import es.urjc.etsii.labtel.service.dto.ProjectPermissionDTO;
import es.urjc.etsii.labtel.service.mapper.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Project.
 */
@Service
@Transactional
public class ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;
    
    private final ProjectPermissionService projectPermissionService;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper, ProjectPermissionService projectPermissionService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectPermissionService = projectPermissionService;
    }

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save
     * @return the persisted entity
     */
    public ProjectDTO save(ProjectDTO projectDTO) {
        log.debug("Request to save Project : {}", projectDTO);

        Project project = projectMapper.toEntity(projectDTO);
        project = projectRepository.save(project);
        return projectMapper.toDto(project);
    }

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        
        return projectRepository.findAll(pageable)
            .map(projectMapper::toDto);
    }


    /**
     * Get one project by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProjectDTO> findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findById(id)
            .map(projectMapper::toDto);
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
    }

	public Page<ProjectDTO> findAllByPermission(Pageable pageable) {
	       log.debug("Request to get all Projects");
	       
	       List<ProjectPermissionDTO> permissions = projectPermissionService.findAllByUserLogin(SecurityUtils.getCurrentUserLogin().get());
	        
	       List<Long> projectIds = permissions.stream().map(e -> e.getProjectId()).collect(Collectors.toList());
	       
	        return projectRepository.findAllByIdIn(projectIds, pageable)
	            .map(projectMapper::toDto);
	    }
}
