package ru.job4j.tracker.associations.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "history_owner",
            joinColumns = {
                    @JoinColumn(name = "driver_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "car_id", nullable = false,
                    updatable = false)})
    private List<CarAuto> carAutos = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CarAuto> getCarAutos() {
        return carAutos;
    }

    public void setCarAutos(List<CarAuto> carAutos) {
        this.carAutos = carAutos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Driver driver = (Driver) o;
        return id == driver.id && Objects.equals(name, driver.name)
                && Objects.equals(carAutos, driver.carAutos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, carAutos);
    }
}
