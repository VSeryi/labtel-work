package es.urjc.etsii.labtel.web.rest;

import com.codahale.metrics.annotation.Timed;

import es.urjc.etsii.labtel.security.SecurityUtils;
import es.urjc.etsii.labtel.service.ProjectPermissionService;
import es.urjc.etsii.labtel.web.rest.errors.BadRequestAlertException;
import es.urjc.etsii.labtel.web.rest.util.HeaderUtil;
import es.urjc.etsii.labtel.web.rest.util.PaginationUtil;
import es.urjc.etsii.labtel.service.dto.ProjectPermissionDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing ProjectPermission.
 */
@RestController
@RequestMapping("/api")
public class ProjectPermissionResource {

    private final Logger log = LoggerFactory.getLogger(ProjectPermissionResource.class);

    private static final String ENTITY_NAME = "projectPermission";

    private final ProjectPermissionService projectPermissionService;

    public ProjectPermissionResource(ProjectPermissionService projectPermissionService) {
        this.projectPermissionService = projectPermissionService;
    }

    /**
     * POST  /project-permissions : Create a new projectPermission.
     *
     * @param projectPermissionDTO the projectPermissionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectPermissionDTO, or with status 400 (Bad Request) if the projectPermission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-permissions")
    @Timed
    public ResponseEntity<ProjectPermissionDTO> createProjectPermission(@RequestBody ProjectPermissionDTO projectPermissionDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectPermission : {}", projectPermissionDTO);
        if (projectPermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new projectPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectPermissionDTO result = projectPermissionService.save(projectPermissionDTO);
        return ResponseEntity.created(new URI("/api/project-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-permissions : Updates an existing projectPermission.
     *
     * @param projectPermissionDTO the projectPermissionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectPermissionDTO,
     * or with status 400 (Bad Request) if the projectPermissionDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectPermissionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-permissions")
    @Timed
    public ResponseEntity<ProjectPermissionDTO> updateProjectPermission(@RequestBody ProjectPermissionDTO projectPermissionDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectPermission : {}", projectPermissionDTO);
        if (projectPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProjectPermissionDTO result = projectPermissionService.save(projectPermissionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectPermissionDTO.getId().toString()))
            .body(result);
    }
    
    
    /**
     * GET  /project-permissions : get all the projectPermissions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projectPermissions in body
     */
    @GetMapping("/project-permissions")
    @Timed
    public ResponseEntity<List<ProjectPermissionDTO>> getAllProjectPermissions(Pageable pageable) {
        log.debug("REST request to get a page of ProjectPermissions");
        Page<ProjectPermissionDTO> page = projectPermissionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-permissions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /project-permissions/:id : get the "id" projectPermission.
     *
     * @param id the id of the projectPermissionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectPermissionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-permissions/{id}")
    @Timed
    public ResponseEntity<ProjectPermissionDTO> getProjectPermission(@PathVariable Long id) {
        log.debug("REST request to get ProjectPermission : {}", id);
        Optional<ProjectPermissionDTO> projectPermissionDTO = projectPermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projectPermissionDTO);
    }
    
    /**
     * GET  /project-permissions/:id : get the "id" projectPermission.
     *
     * @param id the id of the projectPermissionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectPermissionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-permissions/me")
    @Timed
    public ResponseEntity<List<ProjectPermissionDTO>> getProjectPermissions() {
    	Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        log.debug("REST request to get ProjectPermission : {}", userLogin);
        List<ProjectPermissionDTO> projectPermissionsDTO = projectPermissionService.findAllByUserLogin(userLogin.get());
        return ResponseEntity.ok().body(projectPermissionsDTO);
    }
    
    /**
     * GET  /project-permissions/:id : get the "id" projectPermission.
     *
     * @param id the id of the projectPermissionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectPermissionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-permissions/project")
    @Timed
    public ResponseEntity<List<ProjectPermissionDTO>> getProjectPermissions(@RequestParam Long projectId) {
        log.debug("REST request to get a page of ProjectPermissions");
        List<ProjectPermissionDTO> projectPermissionsDTO = projectPermissionService.findAllByProjectId(projectId);
        return ResponseEntity.ok().body(projectPermissionsDTO);
    }

    /**
     * DELETE  /project-permissions/:id : delete the "id" projectPermission.
     *
     * @param id the id of the projectPermissionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-permissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectPermission(@PathVariable Long id) {
        log.debug("REST request to delete ProjectPermission : {}", id);
        projectPermissionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
