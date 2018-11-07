package es.urjc.etsii.labtel.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ProjectItem entity.
 */
public class ProjectItemDTO implements Serializable {

    private Long id;

    private Integer quantity;

    private Set<ItemDTO> items = new HashSet<>();

    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<ItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<ItemDTO> items) {
        this.items = items;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectItemDTO projectItemDTO = (ProjectItemDTO) o;
        if (projectItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", project=" + getProjectId() +
            "}";
    }
}
