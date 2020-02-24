package com.rj.dinosaurs.catalogue.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Clade.
 */
@Entity
@Table(name = "clade")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "clade")
public class Clade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 64)
    @Column(name = "name", length = 64, unique = true)
    private String name;

    @Size(max = 64)
    @Column(name = "pronunciation", length = 64)
    private String pronunciation;

    @Size(max = 64)
    @Column(name = "meaning", length = 64)
    private String meaning;

    @Size(max = 64)
    @Column(name = "description", length = 64)
    private String description;

    @OneToMany(mappedBy = "clade")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dinosaur> dinosaurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Clade name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public Clade pronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
        return this;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getMeaning() {
        return meaning;
    }

    public Clade meaning(String meaning) {
        this.meaning = meaning;
        return this;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getDescription() {
        return description;
    }

    public Clade description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Dinosaur> getDinosaurs() {
        return dinosaurs;
    }

    public Clade dinosaurs(Set<Dinosaur> dinosaurs) {
        this.dinosaurs = dinosaurs;
        return this;
    }

    public Clade addDinosaur(Dinosaur dinosaur) {
        this.dinosaurs.add(dinosaur);
        dinosaur.setClade(this);
        return this;
    }

    public Clade removeDinosaur(Dinosaur dinosaur) {
        this.dinosaurs.remove(dinosaur);
        dinosaur.setClade(null);
        return this;
    }

    public void setDinosaurs(Set<Dinosaur> dinosaurs) {
        this.dinosaurs = dinosaurs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Clade)) {
            return false;
        }
        return id != null && id.equals(((Clade) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Clade{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pronunciation='" + getPronunciation() + "'" +
            ", meaning='" + getMeaning() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
