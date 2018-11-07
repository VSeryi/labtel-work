package es.urjc.etsii.labtel.repository.search;

import es.urjc.etsii.labtel.domain.ProjectPermission;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProjectPermission entity.
 */
public interface ProjectPermissionSearchRepository extends ElasticsearchRepository<ProjectPermission, Long> {
}
