package com.nbastats.query;

import com.nbastats.query.controller.StatsQueryController;
import com.nbastats.query.dto.PlayerStatsSummary;
import com.nbastats.query.dto.TeamStatsSummary;
import com.nbastats.query.service.StatsQueryService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsQueryController.class)
@Import(StatsQueryControllerTest.MockStatsQueryServiceConfig.class)
class StatsQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @TestConfiguration
    static class MockStatsQueryServiceConfig {
        @Bean
        public StatsQueryService statsQueryService() {
            StatsQueryService mock = Mockito.mock(StatsQueryService.class);

            // Sample mock behavior
            Mockito.when(mock.getAveragePointsForPlayer(anyString()))
                   .thenReturn(new PlayerStatsSummary("player1", 0, 25.0, 0, 0, 0, 0, 0, 0));

            Mockito.when(mock.getAveragePointsForTeam(anyString()))
                   .thenReturn(new TeamStatsSummary("team1", 0, 100.0, 0, 0, 0, 0, 0, 0));

            return mock;
        }
    }

    @Test
    void testGetStatsByPlayer() throws Exception {
        mockMvc.perform(get("/api/stats/player/player1"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetStatsByTeam() throws Exception {
        mockMvc.perform(get("/api/stats/team/team1"))
               .andExpect(status().isOk());
    }
}


