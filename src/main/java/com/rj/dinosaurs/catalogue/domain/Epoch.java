package com.rj.dinosaurs.catalogue.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.rj.dinosaurs.catalogue.domain.enumeration.Period;

import com.rj.dinosaurs.catalogue.domain.enumeration.EpochRange;

/**
 * A Epoch.
 */
@Entity
@Table(name = "epoch")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "epoch")
public class Epoch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "period", nullable = false)
    private Period period;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "epoch", nullable = false)
    private EpochRange epoch;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    @Column(name = "from_ma", nullable = false)
    private Integer fromMa;

    @NotNull
    @Min(value = 0)
    @Max(value = 999)
    @Column(name = "to_ma", nullable = false)
    private Integer toMa;

    @OneToMany(mappedBy = "epochItLived")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dinosaur> dinosaurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public Epoch period(Period period) {
        this.period = period;
        return this;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public EpochRange getEpoch() {
        return epoch;
    }

    public Epoch epoch(EpochRange epoch) {
        this.epoch = epoch;
        return this;
    }

    public void setEpoch(EpochRange epoch) {
        this.epoch = epoch;
    }

    public Integer getFromMa() {
        return fromMa;
    }

    public Epoch fromMa(Integer fromMa) {
        this.fromMa = fromMa;
        return this;
    }

    public void setFromMa(Integer fromMa) {
        this.fromMa = fromMa;
    }

    public Integer getToMa() {
        return toMa;
    }

    public Epoch toMa(Integer toMa) {
        this.toMa = toMa;
        return this;
    }

    public void setToMa(Integer toMa) {
        this.toMa = toMa;
    }

    public Set<Dinosaur> getDinosaurs() {
        return dinosaurs;
    }

    public Epoch dinosaurs(Set<Dinosaur> dinosaurs) {
        this.dinosaurs = dinosaurs;
        return this;
    }

    public Epoch addDinosaur(Dinosaur dinosaur) {
        this.dinosaurs.add(dinosaur);
        dinosaur.setEpochItLived(this);
        return this;
    }

    public Epoch removeDinosaur(Dinosaur dinosaur) {
        this.dinosaurs.remove(dinosaur);
        dinosaur.setEpochItLived(null);
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
        if (!(o instanceof Epoch)) {
            return false;
        }
        return id != null && id.equals(((Epoch) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Epoch{" +
            "id=" + getId() +
            ", period='" + getPeriod() + "'" +
            ", epoch='" + getEpoch() + "'" +
            ", fromMa=" + getFromMa() +
            ", toMa=" + getToMa() +
            "}";
    }
}
