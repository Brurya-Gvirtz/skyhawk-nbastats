package com.skyhawk.statsaggregation.model;

public class Team {
    private Long id;
    private String name;
    private String city;
    private String abbreviation;

    public Team() {
    }

    public Team(Long id, String name, String city, String abbreviation) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.abbreviation = abbreviation;
    }

    // Getters and Setters
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
