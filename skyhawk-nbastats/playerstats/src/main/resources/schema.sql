-- TEAM TABLE
CREATE TABLE team (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100),
    abbreviation VARCHAR(10)
);

-- PLAYER TABLE
CREATE TABLE player (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    jersey_number VARCHAR(10),
    position VARCHAR(20)
);

-- GAME TABLE
CREATE TABLE game (
    id BIGINT PRIMARY KEY,
    home_team_id BIGINT NOT NULL,
    away_team_id BIGINT NOT NULL,
    game_date TIMESTAMP NOT NULL,
    season VARCHAR(20),
    game_status VARCHAR(20),
    
    CONSTRAINT fk_home_team FOREIGN KEY (home_team_id) REFERENCES team(id),
    CONSTRAINT fk_away_team FOREIGN KEY (away_team_id) REFERENCES team(id)
);

-- PLAYER GAME STATS TABLE
CREATE TABLE player_game_stats (
    id BIGINT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    points INT DEFAULT 0,
    rebounds INT DEFAULT 0,
    assists INT DEFAULT 0,
    steals INT DEFAULT 0,
    blocks INT DEFAULT 0,
    fouls INT DEFAULT 0,
    turnovers INT DEFAULT 0,
    minutes_played DOUBLE PRECISION DEFAULT 0.0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_player FOREIGN KEY (player_id) REFERENCES player(id),
    CONSTRAINT fk_game FOREIGN KEY (game_id) REFERENCES game(id),
    CONSTRAINT fk_team FOREIGN KEY (team_id) REFERENCES team(id)
);
