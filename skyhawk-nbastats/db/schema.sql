CREATE TABLE IF NOT EXISTS player_stats (
    id VARCHAR(255) PRIMARY KEY,
    player_id VARCHAR(255) NOT NULL,
    game_id VARCHAR(255) NOT NULL,
    team_id VARCHAR(255) NOT NULL,
    points INTEGER CHECK (points >= 0),
    rebounds INTEGER CHECK (rebounds >= 0),
    assists INTEGER CHECK (assists >= 0),
    steals INTEGER CHECK (steals >= 0),
    blocks INTEGER CHECK (blocks >= 0),
    fouls INTEGER CHECK (fouls >= 0 AND fouls <= 6),
    turnovers INTEGER CHECK (turnovers >= 0),
    minutes_played DOUBLE PRECISION CHECK (minutes_played >= 0 AND minutes_played <= 48),
    last_updated TIMESTAMP
);