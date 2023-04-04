package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Period;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "pe")
@Table(name = "pe")
public class P {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column
    private Period period;

    @Column
    private io.hypersistence.utils.hibernate.type.basic.Inet inet;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public io.hypersistence.utils.hibernate.type.basic.Inet getInet() {
        return inet;
    }

    public void setInet(io.hypersistence.utils.hibernate.type.basic.Inet inet) {
        this.inet = inet;
    }

    @Override
    public String toString() {
        return "P{id=%d, period=%s, inet=%s}".formatted(id, period, inet);
    }
}
