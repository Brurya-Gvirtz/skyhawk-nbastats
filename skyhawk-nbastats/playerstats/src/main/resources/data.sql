-- Insert teams
INSERT INTO team (id, name, city, abbreviation) VALUES
(1, 'Lakers', 'Los Angeles', 'LAL'),
(2, 'Warriors', 'San Francisco', 'GSW');

-- Insert players
INSERT INTO player (id, first_name, last_name, jersey_number, position) VALUES
(1, 'LeBron', 'James', '23', 'SF'),
(2, 'Stephen', 'Curry', '30', 'PG');

-- Insert game
INSERT INTO game (id, home_team_id, away_team_id, game_date, season, game_status) VALUES
(1, 1, 2, '2025-03-15 19:30:00', '2024-2025', 'COMPLETED');

-- Insert player game stats
INSERT INTO player_game_stats (id, player_id, game_id, team_id, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played, last_updated) VALUES
(1, 1, 1, 1, 28, 7, 9, 2, 1, 3, 4, 35.5, '2025-03-15 22:00:00'),
(2, 2, 1, 2, 33, 5, 7, 1, 0, 2, 2, 36.0, '2025-03-15 22:00:00');
