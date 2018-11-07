package es.urjc.etsii.labtel.service.dto;

import java.io.Serializable;
import java.util.Objects;
import es.urjc.etsii.labtel.domain.enumeration.TypePermission;

/**
 * A DTO for the ProjectPermission entity.
 */
public class ProjectPermissionDTO implements Serializable {

    private Long id;

    private TypePermission permission;

    private Long userId;

    private String userLogin;

    private Long projectId;

    private String projectValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypePermission getPermission() {
        return permission;
    }

    public void setPermission(TypePermission permission) {
        this.permission = permission;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectValue() {
        return projectValue;
    }

    public void setProjectValue(String projectValue) {
        this.projectValue = projectValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectPermissionDTO projectPermissionDTO = (ProjectPermissionDTO) o;
        if (projectPermissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectPermissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectPermissionDTO{" +
            "id=" + getId() +
            ", permission='" + getPermission() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", project=" + getProjectId() +
            ", project='" + getProjectValue() + "'" +
            "}";
    }
}
