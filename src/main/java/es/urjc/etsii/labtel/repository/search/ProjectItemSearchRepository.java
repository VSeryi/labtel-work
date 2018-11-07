package es.urjc.etsii.labtel.repository.search;

import es.urjc.etsii.labtel.domain.ProjectItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProjectItem entity.
 */
public interface ProjectItemSearchRepository extends ElasticsearchRepository<ProjectItem, Long> {
}
