#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    INSERT INTO player_stats (id, player_id, game_id, team_id, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played, last_updated)
    VALUES ('1', 'player1', 'game1', 'team1', 10, 5, 7, 2, 1, 3, 4, 36.5, NOW());
EOSQL