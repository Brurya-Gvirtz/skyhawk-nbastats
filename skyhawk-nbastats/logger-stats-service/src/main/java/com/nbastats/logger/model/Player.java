package com.nbastats.logger.model;

public class Player {
    private Long id;
    private String name;
    private String position;

    public Player() {
    }

    public Player(Long id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
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

	public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

