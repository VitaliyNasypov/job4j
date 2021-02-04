package ru.job4j.tracker.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "MODELAUTOS")
public class ModelAuto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "brand_auto_id")
    private BrandAuto brandAuto;

    public ModelAuto() {
    }

    public ModelAuto(String name) {
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

    public BrandAuto getBrandAuto() {
        return brandAuto;
    }

    public void setBrandAuto(BrandAuto brandAuto) {
        this.brandAuto = brandAuto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModelAuto modelAuto = (ModelAuto) o;
        return id == modelAuto.id && Objects.equals(name, modelAuto.name)
                && Objects.equals(brandAuto, modelAuto.brandAuto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, brandAuto);
    }

    @Override
    public String toString() {
        return "Model: " + name;
    }
}
