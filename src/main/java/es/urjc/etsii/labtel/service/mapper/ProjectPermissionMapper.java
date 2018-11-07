package es.urjc.etsii.labtel.service.mapper;

import es.urjc.etsii.labtel.domain.*;
import es.urjc.etsii.labtel.service.dto.ProjectPermissionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProjectPermission and its DTO ProjectPermissionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ProjectMapper.class})
public interface ProjectPermissionMapper extends EntityMapper<ProjectPermissionDTO, ProjectPermission> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.value", target = "projectValue")
    ProjectPermissionDTO toDto(ProjectPermission projectPermission);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "projectId", target = "project")
    ProjectPermission toEntity(ProjectPermissionDTO projectPermissionDTO);

    default ProjectPermission fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectPermission projectPermission = new ProjectPermission();
        projectPermission.setId(id);
        return projectPermission;
    }
}
