package ru.job4j.tracker.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "CANDIDATES")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int experience;
    private int salary;
    @OneToOne(fetch = FetchType.LAZY)
    private JobBase jobBase;

    public Candidate() {
    }

    public Candidate(String name, int experience, int salary) {
        this.name = name;
        this.experience = experience;
        this.salary = salary;
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

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public JobBase getJobBase() {
        return jobBase;
    }

    public void setJobBase(JobBase jobBase) {
        this.jobBase = jobBase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id && experience == candidate.experience
                && salary == candidate.salary
                && Objects.equals(name, candidate.name)
                && Objects.equals(jobBase, candidate.jobBase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, experience, salary, jobBase);
    }
}
