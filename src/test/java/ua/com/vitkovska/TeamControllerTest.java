package ua.com.vitkovska;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ua.com.vitkovska.commons.Constants;
import ua.com.vitkovska.dao.TeamDao;
import ua.com.vitkovska.model.Team;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringBootRestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TeamControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TeamDao teamDao;

    @Autowired
    private ObjectMapper objectMapper;
    private Team team;

    private final String teamJsonBody = """
              {
                  "name": "%s"
              }
            """;
    private final int notValidId = 100;

    @BeforeEach
    public void fillDatabaseTeamTable() {
        team = new Team();
        team.setName("Orlando Magic");
        teamDao.save(team);
    }

    @AfterEach
    public void clearDatabaseTeamTable() {
        teamDao.deleteAll();
    }


    @Test
    public void testShouldCreateTeam() throws Exception {
        String name = "Miami Heat";
        String body = teamJsonBody.formatted(name);
        MvcResult mvcResult = mvc.perform(post(Constants.Path.TEAM_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();

        Team responseTeam = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Team.class);
        Integer teamId = responseTeam.getId();
        assertNotNull(teamId);
        assertTrue(teamId >= 1);

        Team actualTeam = teamDao.findById(teamId).orElse(null);
        assertNotNull(actualTeam);
        assertEquals(actualTeam, responseTeam);
    }

    @Test
    public void testShouldNotCreateTeamWithDuplicatedTeamName() throws Exception {
        String name = team.getName();
        String body = teamJsonBody.formatted(name);

        mvc.perform(post(Constants.Path.TEAM_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testShouldNotCreateTeamValidation() throws Exception {
        mvc.perform(post(Constants.Path.TEAM_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testShouldRetrieveAllTeams() throws Exception {
        MvcResult mvcResult = mvc.perform(get(Constants.Path.TEAM_API))
                .andExpect(status().isOk())
                .andReturn();
        List<Team> actualTeams = new ArrayList<>();
        JsonNode tree = objectMapper.readTree(mvcResult.getResponse().getContentAsByteArray());
        for (JsonNode jsonNode : tree) {
            Team nodeTeam = objectMapper.treeToValue(jsonNode, Team.class);
            actualTeams.add(nodeTeam);
        }
        List<Team> responseTeam = teamDao.findAll();
        assertEquals(responseTeam, actualTeams);
    }

    @Test
    public void testShouldRetrieveTeamById() throws Exception {
        MvcResult mvcResult = mvc.perform(get(Constants.Path.TEAM_API + Constants.Path.ID_PATH_VARIABLE, team.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Team actualTeam = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Team.class);
        assertEquals(team, actualTeam);
    }

    @Test
    public void testShouldNotRetrieveTeamByNotValidId() throws Exception {
        mvc.perform(get(Constants.Path.TEAM_API + Constants.Path.ID_PATH_VARIABLE, notValidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testShouldUpdateTeamById() throws Exception {
        String nameToUpdate = "Miami Heat";
        String body = teamJsonBody.formatted(nameToUpdate);
        MvcResult mvcResult = mvc.perform(put(Constants.Path.TEAM_API + Constants.Path.ID_PATH_VARIABLE, team.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();
        team = teamDao.findById(team.getId()).orElse(null);
        assertNotNull(team);
        Team responseTeam = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Team.class);
        assertEquals(team, responseTeam);
    }

    @Test
    public void testShouldNotUpdateTeamByNotValidId() throws Exception {
        String nameToUpdate = "Miami Heat";
        String body = teamJsonBody.formatted(nameToUpdate);
        mvc.perform(put(Constants.Path.TEAM_API + Constants.Path.ID_PATH_VARIABLE, notValidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testShouldNotUpdateTeamNotValidBody() throws Exception {
        mvc.perform(put(Constants.Path.TEAM_API + Constants.Path.ID_PATH_VARIABLE, team.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testShouldNotUpdateTeamExistedTeamName() throws Exception {
        teamDao.save(new Team(null, "Miami Heat"));
        String nameToUpdate = "Miami Heat";
        String body = teamJsonBody.formatted(nameToUpdate);
        mvc.perform(put(Constants.Path.TEAM_API + Constants.Path.ID_PATH_VARIABLE, team.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testShouldDeleteTeamById() throws Exception {
        mvc.perform(delete(Constants.Path.TEAM_API + Constants.Path.ID_PATH_VARIABLE, team.getId()))
                .andExpect(status().isOk());
        assertNull(teamDao.findById(team.getId()).orElse(null));
    }

    @Test
    public void testShouldNotDeleteTeamByNotValidId() throws Exception {
        mvc.perform(delete(Constants.Path.TEAM_API + Constants.Path.ID_PATH_VARIABLE, notValidId))
                .andExpect(status().isNotFound());
    }

}
