package ru.job4j.tracker.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "BRANDAUTOS")
public class BrandAuto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(mappedBy = "brandAuto")
    private List<ModelAuto> modelAutos = new ArrayList<>();

    public BrandAuto() {
    }

    public BrandAuto(String name) {
        this.name = name;
    }

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

    public List<ModelAuto> getModelAutos() {
        return modelAutos;
    }

    public void setModelAutos(List<ModelAuto> modelAutos) {
        this.modelAutos = modelAutos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BrandAuto brandAuto = (BrandAuto) o;
        return id == brandAuto.id && Objects.equals(name, brandAuto.name)
                && Objects.equals(modelAutos, brandAuto.modelAutos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelAutos);
    }

    @Override
    public String toString() {
        return "Brand: " + name;
    }
}
