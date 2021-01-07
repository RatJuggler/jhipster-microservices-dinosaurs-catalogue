package com.rj.dinosaurs.catalogue.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import com.rj.dinosaurs.catalogue.domain.enumeration.Period;
import com.rj.dinosaurs.catalogue.domain.enumeration.EpochRange;

/**
 * A DTO for the {@link com.rj.dinosaurs.catalogue.domain.Epoch} entity.
 */
public class EpochDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Period period;

    @NotNull
    private EpochRange epoch;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    private Integer fromMa;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    private Integer toMa;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public EpochRange getEpoch() {
        return epoch;
    }

    public void setEpoch(EpochRange epoch) {
        this.epoch = epoch;
    }

    public Integer getFromMa() {
        return fromMa;
    }

    public void setFromMa(Integer fromMa) {
        this.fromMa = fromMa;
    }

    public Integer getToMa() {
        return toMa;
    }

    public void setToMa(Integer toMa) {
        this.toMa = toMa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EpochDTO)) {
            return false;
        }

        return id != null && id.equals(((EpochDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EpochDTO{" +
            "id=" + getId() +
            ", period='" + getPeriod() + "'" +
            ", epoch='" + getEpoch() + "'" +
            ", fromMa=" + getFromMa() +
            ", toMa=" + getToMa() +
            "}";
    }
}
