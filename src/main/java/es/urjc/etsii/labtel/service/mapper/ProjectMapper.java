package es.urjc.etsii.labtel.service.mapper;

import es.urjc.etsii.labtel.domain.*;
import es.urjc.etsii.labtel.service.dto.ProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Project and its DTO ProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectItemMapper.class})
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {

    Project toEntity(ProjectDTO projectDTO);

    default Project fromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
