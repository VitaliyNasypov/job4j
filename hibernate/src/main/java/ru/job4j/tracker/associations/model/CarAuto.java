package ru.job4j.tracker.associations.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class CarAuto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "engine_id", foreignKey = @ForeignKey(name = "ENGINE_ID_FK"))
    private Engine engine;
    @ManyToMany(mappedBy = "carAutos")
    private List<Driver> drivers = new ArrayList<>();

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

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CarAuto carAuto = (CarAuto) o;
        return id == carAuto.id && Objects.equals(name, carAuto.name)
                && Objects.equals(engine, carAuto.engine)
                && Objects.equals(drivers, carAuto.drivers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, engine, drivers);
    }
}
