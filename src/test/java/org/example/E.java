package org.example;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "en")
@Table(name = "en")
public class E {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(nullable = true)
    private int year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private SignPosition signPosition;

    @Column(nullable = true)
    private int day;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public SignPosition getSignPosition() {
        return signPosition;
    }

    public void setSignPosition(SignPosition signPosition) {
        this.signPosition = signPosition;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "E{id=%d, year=%d, signPosition=%s, day=%d}".formatted(id, year, signPosition, day);
    }
}
