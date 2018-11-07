package es.urjc.etsii.labtel.service.mapper;

import es.urjc.etsii.labtel.domain.*;
import es.urjc.etsii.labtel.service.dto.ProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Project and its DTO ProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {


    @Mapping(target = "items", ignore = true)
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
