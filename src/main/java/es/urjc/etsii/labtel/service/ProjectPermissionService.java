package es.urjc.etsii.labtel.service;

import es.urjc.etsii.labtel.domain.ProjectPermission;
import es.urjc.etsii.labtel.repository.ProjectPermissionRepository;
import es.urjc.etsii.labtel.service.dto.ProjectPermissionDTO;
import es.urjc.etsii.labtel.service.mapper.ProjectPermissionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ProjectPermission.
 */
@Service
@Transactional
public class ProjectPermissionService {

    private final Logger log = LoggerFactory.getLogger(ProjectPermissionService.class);

    private final ProjectPermissionRepository projectPermissionRepository;

    private final ProjectPermissionMapper projectPermissionMapper;

    public ProjectPermissionService(ProjectPermissionRepository projectPermissionRepository, ProjectPermissionMapper projectPermissionMapper) {
        this.projectPermissionRepository = projectPermissionRepository;
        this.projectPermissionMapper = projectPermissionMapper;
    }

    /**
     * Save a projectPermission.
     *
     * @param projectPermissionDTO the entity to save
     * @return the persisted entity
     */
    public ProjectPermissionDTO save(ProjectPermissionDTO projectPermissionDTO) {
        log.debug("Request to save ProjectPermission : {}", projectPermissionDTO);

        ProjectPermission projectPermission = projectPermissionMapper.toEntity(projectPermissionDTO);
        projectPermission = projectPermissionRepository.save(projectPermission);
        ProjectPermissionDTO result = projectPermissionMapper.toDto(projectPermission);
        return result;
    }

    /**
     * Get all the projectPermissions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectPermissionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectPermissions");
        return projectPermissionRepository.findAll(pageable)
            .map(projectPermissionMapper::toDto);
    }


    /**
     * Get one projectPermission by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProjectPermissionDTO> findOne(Long id) {
        log.debug("Request to get ProjectPermission : {}", id);
        return projectPermissionRepository.findById(id)
            .map(projectPermissionMapper::toDto);
    }
    
    /**
     * Get one projectPermission by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<ProjectPermissionDTO> findAllByUserLogin(String userLogin) {
        log.debug("Request to get ProjectPermission : {}", userLogin);
        return projectPermissionRepository.findByUserLogin(userLogin)
        	.stream()
            .map(projectPermissionMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get one projectPermission by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<ProjectPermissionDTO> findAllByProjectId(Long projectId) {
        log.debug("Request to get ProjectPermission : {}", projectId);
        return projectPermissionRepository.findByProjectId(projectId)
        	.stream()
            .map(projectPermissionMapper::toDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Delete the projectPermission by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectPermission : {}", id);
        projectPermissionRepository.deleteById(id);
    }


}
