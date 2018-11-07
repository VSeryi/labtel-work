package es.urjc.etsii.labtel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProjectItem.
 */
@Entity
@Table(name = "project_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projectitem")
public class ProjectItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "project_item_item",
               joinColumns = @JoinColumn(name = "project_items_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "items_id", referencedColumnName = "id"))
    private Set<Item> items = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("items")
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProjectItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<Item> getItems() {
        return items;
    }

    public ProjectItem items(Set<Item> items) {
        this.items = items;
        return this;
    }

    public ProjectItem addItem(Item item) {
        this.items.add(item);
        return this;
    }

    public ProjectItem removeItem(Item item) {
        this.items.remove(item);
        return this;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Project getProject() {
        return project;
    }

    public ProjectItem project(Project project) {
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
        ProjectItem projectItem = (ProjectItem) o;
        if (projectItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
