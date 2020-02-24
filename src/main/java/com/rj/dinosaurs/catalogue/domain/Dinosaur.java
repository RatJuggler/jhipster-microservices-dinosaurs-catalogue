package com.rj.dinosaurs.catalogue.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import com.rj.dinosaurs.catalogue.domain.enumeration.Diet;

/**
 * A Dinosaur.
 */
@Entity
@Table(name = "dinosaur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dinosaur")
public class Dinosaur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "name", length = 64, nullable = false, unique = true)
    private String name;

    @Size(max = 64)
    @Column(name = "pronunciation", length = 64)
    private String pronunciation;

    @Size(max = 64)
    @Column(name = "meaning", length = 64)
    private String meaning;

    @Min(value = 0)
    @Max(value = 999)
    @Column(name = "weight")
    private Integer weight;

    @Min(value = 0)
    @Max(value = 999)
    @Column(name = "length")
    private Integer length;

    @Min(value = 0)
    @Max(value = 999)
    @Column(name = "height")
    private Integer height;

    @Enumerated(EnumType.STRING)
    @Column(name = "diet")
    private Diet diet;

    @NotNull
    @Min(value = 0L)
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @NotNull
    @Column(name = "created_dt", nullable = false)
    private Instant createdDt;

    @Min(value = 0L)
    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_dt")
    private Instant modifiedDt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("dinosaurs")
    private Epoch epochItLived;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("dinosaurs")
    private Clade clade;

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

    public Dinosaur name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public Dinosaur pronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
        return this;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getMeaning() {
        return meaning;
    }

    public Dinosaur meaning(String meaning) {
        this.meaning = meaning;
        return this;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public Integer getWeight() {
        return weight;
    }

    public Dinosaur weight(Integer weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getLength() {
        return length;
    }

    public Dinosaur length(Integer length) {
        this.length = length;
        return this;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getHeight() {
        return height;
    }

    public Dinosaur height(Integer height) {
        this.height = height;
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Diet getDiet() {
        return diet;
    }

    public Dinosaur diet(Diet diet) {
        this.diet = diet;
        return this;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Dinosaur createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDt() {
        return createdDt;
    }

    public Dinosaur createdDt(Instant createdDt) {
        this.createdDt = createdDt;
        return this;
    }

    public void setCreatedDt(Instant createdDt) {
        this.createdDt = createdDt;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public Dinosaur modifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Instant getModifiedDt() {
        return modifiedDt;
    }

    public Dinosaur modifiedDt(Instant modifiedDt) {
        this.modifiedDt = modifiedDt;
        return this;
    }

    public void setModifiedDt(Instant modifiedDt) {
        this.modifiedDt = modifiedDt;
    }

    public Epoch getEpochItLived() {
        return epochItLived;
    }

    public Dinosaur epochItLived(Epoch epoch) {
        this.epochItLived = epoch;
        return this;
    }

    public void setEpochItLived(Epoch epoch) {
        this.epochItLived = epoch;
    }

    public Clade getClade() {
        return clade;
    }

    public Dinosaur clade(Clade clade) {
        this.clade = clade;
        return this;
    }

    public void setClade(Clade clade) {
        this.clade = clade;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dinosaur)) {
            return false;
        }
        return id != null && id.equals(((Dinosaur) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Dinosaur{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pronunciation='" + getPronunciation() + "'" +
            ", meaning='" + getMeaning() + "'" +
            ", weight=" + getWeight() +
            ", length=" + getLength() +
            ", height=" + getHeight() +
            ", diet='" + getDiet() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdDt='" + getCreatedDt() + "'" +
            ", modifiedBy=" + getModifiedBy() +
            ", modifiedDt='" + getModifiedDt() + "'" +
            "}";
    }
}
