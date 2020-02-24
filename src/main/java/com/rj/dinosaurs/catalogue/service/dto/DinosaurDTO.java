package com.rj.dinosaurs.catalogue.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.rj.dinosaurs.catalogue.domain.enumeration.Diet;

/**
 * A DTO for the {@link com.rj.dinosaurs.catalogue.domain.Dinosaur} entity.
 */
public class DinosaurDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String name;

    @Size(max = 64)
    private String pronunciation;

    @Size(max = 64)
    private String meaning;

    @Min(value = 0)
    @Max(value = 999)
    private Integer weight;

    @Min(value = 0)
    @Max(value = 999)
    private Integer length;

    @Min(value = 0)
    @Max(value = 999)
    private Integer height;

    private Diet diet;

    @NotNull
    @Min(value = 0L)
    private Long createdBy;

    @NotNull
    private Instant createdDt;

    @Min(value = 0L)
    private Long modifiedBy;

    private Instant modifiedDt;


    private Long epochItLivedId;

    private Long cladeId;

    private String cladeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Instant createdDt) {
        this.createdDt = createdDt;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Instant getModifiedDt() {
        return modifiedDt;
    }

    public void setModifiedDt(Instant modifiedDt) {
        this.modifiedDt = modifiedDt;
    }

    public Long getEpochItLivedId() {
        return epochItLivedId;
    }

    public void setEpochItLivedId(Long epochId) {
        this.epochItLivedId = epochId;
    }

    public Long getCladeId() {
        return cladeId;
    }

    public void setCladeId(Long cladeId) {
        this.cladeId = cladeId;
    }

    public String getCladeName() {
        return cladeName;
    }

    public void setCladeName(String cladeName) {
        this.cladeName = cladeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DinosaurDTO dinosaurDTO = (DinosaurDTO) o;
        if (dinosaurDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dinosaurDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DinosaurDTO{" +
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
            ", epochItLivedId=" + getEpochItLivedId() +
            ", cladeId=" + getCladeId() +
            ", cladeName='" + getCladeName() + "'" +
            "}";
    }
}
