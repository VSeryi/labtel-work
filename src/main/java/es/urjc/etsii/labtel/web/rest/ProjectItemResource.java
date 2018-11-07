package es.urjc.etsii.labtel.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.urjc.etsii.labtel.service.ProjectItemService;
import es.urjc.etsii.labtel.web.rest.errors.BadRequestAlertException;
import es.urjc.etsii.labtel.web.rest.util.HeaderUtil;
import es.urjc.etsii.labtel.web.rest.util.PaginationUtil;
import es.urjc.etsii.labtel.service.dto.ProjectItemDTO;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ProjectItem.
 */
@RestController
@RequestMapping("/api")
public class ProjectItemResource {

    private final Logger log = LoggerFactory.getLogger(ProjectItemResource.class);

    private static final String ENTITY_NAME = "projectItem";

    private final ProjectItemService projectItemService;

    public ProjectItemResource(ProjectItemService projectItemService) {
        this.projectItemService = projectItemService;
    }

    /**
     * POST  /project-items : Create a new projectItem.
     *
     * @param projectItemDTO the projectItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectItemDTO, or with status 400 (Bad Request) if the projectItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-items")
    @Timed
    public ResponseEntity<ProjectItemDTO> createProjectItem(@RequestBody ProjectItemDTO projectItemDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectItem : {}", projectItemDTO);
        if (projectItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new projectItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectItemDTO result = projectItemService.save(projectItemDTO);
        return ResponseEntity.created(new URI("/api/project-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-items : Updates an existing projectItem.
     *
     * @param projectItemDTO the projectItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectItemDTO,
     * or with status 400 (Bad Request) if the projectItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-items")
    @Timed
    public ResponseEntity<ProjectItemDTO> updateProjectItem(@RequestBody ProjectItemDTO projectItemDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectItem : {}", projectItemDTO);
        if (projectItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProjectItemDTO result = projectItemService.save(projectItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-items : get all the projectItems.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of projectItems in body
     */
    @GetMapping("/project-items")
    @Timed
    public ResponseEntity<List<ProjectItemDTO>> getAllProjectItems(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of ProjectItems");
        Page<ProjectItemDTO> page;
        if (eagerload) {
            page = projectItemService.findAllWithEagerRelationships(pageable);
        } else {
            page = projectItemService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/project-items?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /project-items/:id : get the "id" projectItem.
     *
     * @param id the id of the projectItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-items/{id}")
    @Timed
    public ResponseEntity<ProjectItemDTO> getProjectItem(@PathVariable Long id) {
        log.debug("REST request to get ProjectItem : {}", id);
        Optional<ProjectItemDTO> projectItemDTO = projectItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projectItemDTO);
    }

    /**
     * DELETE  /project-items/:id : delete the "id" projectItem.
     *
     * @param id the id of the projectItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectItem(@PathVariable Long id) {
        log.debug("REST request to delete ProjectItem : {}", id);
        projectItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/project-items?query=:query : search for the projectItem corresponding
     * to the query.
     *
     * @param query the query of the projectItem search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/project-items")
    @Timed
    public ResponseEntity<List<ProjectItemDTO>> searchProjectItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProjectItems for query {}", query);
        Page<ProjectItemDTO> page = projectItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/project-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
