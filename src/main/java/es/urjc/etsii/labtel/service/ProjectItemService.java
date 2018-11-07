package es.urjc.etsii.labtel.service;

import es.urjc.etsii.labtel.domain.ProjectItem;
import es.urjc.etsii.labtel.repository.ProjectItemRepository;
import es.urjc.etsii.labtel.repository.search.ProjectItemSearchRepository;
import es.urjc.etsii.labtel.service.dto.ProjectItemDTO;
import es.urjc.etsii.labtel.service.mapper.ProjectItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProjectItem.
 */
@Service
@Transactional
public class ProjectItemService {

    private final Logger log = LoggerFactory.getLogger(ProjectItemService.class);

    private final ProjectItemRepository projectItemRepository;

    private final ProjectItemMapper projectItemMapper;

    private final ProjectItemSearchRepository projectItemSearchRepository;

    public ProjectItemService(ProjectItemRepository projectItemRepository, ProjectItemMapper projectItemMapper, ProjectItemSearchRepository projectItemSearchRepository) {
        this.projectItemRepository = projectItemRepository;
        this.projectItemMapper = projectItemMapper;
        this.projectItemSearchRepository = projectItemSearchRepository;
    }

    /**
     * Save a projectItem.
     *
     * @param projectItemDTO the entity to save
     * @return the persisted entity
     */
    public ProjectItemDTO save(ProjectItemDTO projectItemDTO) {
        log.debug("Request to save ProjectItem : {}", projectItemDTO);

        ProjectItem projectItem = projectItemMapper.toEntity(projectItemDTO);
        projectItem = projectItemRepository.save(projectItem);
        ProjectItemDTO result = projectItemMapper.toDto(projectItem);
        projectItemSearchRepository.save(projectItem);
        return result;
    }

    /**
     * Get all the projectItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectItems");
        return projectItemRepository.findAll(pageable)
            .map(projectItemMapper::toDto);
    }


    /**
     * Get one projectItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProjectItemDTO> findOne(Long id) {
        log.debug("Request to get ProjectItem : {}", id);
        return projectItemRepository.findById(id)
            .map(projectItemMapper::toDto);
    }

    /**
     * Delete the projectItem by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectItem : {}", id);
        projectItemRepository.deleteById(id);
        projectItemSearchRepository.deleteById(id);
    }

    /**
     * Search for the projectItem corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectItemDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProjectItems for query {}", query);
        return projectItemSearchRepository.search(queryStringQuery(query), pageable)
            .map(projectItemMapper::toDto);
    }
}
