package es.urjc.etsii.labtel.service.mapper;

import es.urjc.etsii.labtel.domain.*;
import es.urjc.etsii.labtel.service.dto.ProjectItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProjectItem and its DTO ProjectItemDTO.
 */
@Mapper(componentModel = "spring", uses = {ItemMapper.class, ProjectMapper.class})
public interface ProjectItemMapper extends EntityMapper<ProjectItemDTO, ProjectItem> {

    @Mapping(source = "project.id", target = "projectId")
    ProjectItemDTO toDto(ProjectItem projectItem);

    @Mapping(source = "projectId", target = "project")
    ProjectItem toEntity(ProjectItemDTO projectItemDTO);

    default ProjectItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectItem projectItem = new ProjectItem();
        projectItem.setId(id);
        return projectItem;
    }
}
