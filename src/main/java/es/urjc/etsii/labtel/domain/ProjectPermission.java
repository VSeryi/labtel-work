package es.urjc.etsii.labtel.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import es.urjc.etsii.labtel.domain.enumeration.TypePermission;

/**
 * A ProjectPermission.
 */
@Entity
@Table(name = "project_permission")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projectpermission")
public class ProjectPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private TypePermission permission;

    @OneToOne    @JoinColumn(unique = true)
    private User user;

    @OneToOne    @JoinColumn(unique = true)
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypePermission getPermission() {
        return permission;
    }

    public ProjectPermission permission(TypePermission permission) {
        this.permission = permission;
        return this;
    }

    public void setPermission(TypePermission permission) {
        this.permission = permission;
    }

    public User getUser() {
        return user;
    }

    public ProjectPermission user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public ProjectPermission project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectPermission projectPermission = (ProjectPermission) o;
        if (projectPermission.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectPermission.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectPermission{" +
            "id=" + getId() +
            ", permission='" + getPermission() + "'" +
            "}";
    }
}
