package com.rj.dinosaurs.catalogue.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.rj.dinosaurs.catalogue.domain.Clade} entity.
 */
public class CladeDTO implements Serializable {
    
    private Long id;

    @Size(max = 64)
    private String name;

    @Size(max = 64)
    private String pronunciation;

    @Size(max = 64)
    private String meaning;

    @Size(max = 64)
    private String description;

    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CladeDTO)) {
            return false;
        }

        return id != null && id.equals(((CladeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CladeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pronunciation='" + getPronunciation() + "'" +
            ", meaning='" + getMeaning() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
