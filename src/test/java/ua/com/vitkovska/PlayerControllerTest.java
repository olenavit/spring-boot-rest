package ua.com.vitkovska;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ua.com.vitkovska.commons.Constants;
import ua.com.vitkovska.dao.PlayerDao;
import ua.com.vitkovska.dao.TeamDao;
import ua.com.vitkovska.dto.response.PlayerPageResponseDto;
import ua.com.vitkovska.dto.response.UploadJsonResponseDto;
import ua.com.vitkovska.model.Player;
import ua.com.vitkovska.model.Team;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringBootRestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PlayerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private ObjectMapper objectMapper;
    private Player player;
    private static Team team;

    private final String playerJsonBody = """
              {
                  "name":"%s",
                  "surname":"%s",
                  "yearOfBirth":%d,
                  "position":"%s",
                  "teamId":"%s"
              }
            """;
    private final int notValidId = 100;

    @BeforeAll
    public static void fillDatabaseTeamTable(@Autowired TeamDao teamDao) {
        team = new Team();
        team.setName("Orlando Magic");
        teamDao.save(team);
    }

    @BeforeEach
    public void fillDatabasePlayerTable() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("position");
        player = new Player();
        player.setName("Jayson");
        player.setSurname("Tatum");
        player.setYearOfBirth(1998);
        player.setPosition(arrayList);
        player.setTeam(team);
        playerDao.save(player);
    }

    @AfterEach
    public void clearDatabasePlayerTable() {
        playerDao.deleteAll();
    }

    @Test
    public void testShouldCreatePlayer() throws Exception {
        String name = "Kristaps";
        String surname = "Porzingis";
        int yearOfBirth = 1996;
        String position = "shooter, power forward";
        int teamId = team.getId();

        String body = playerJsonBody.formatted(name, surname, yearOfBirth, position, teamId);
        MvcResult mvcResult = mvc.perform(post(Constants.Path.PLAYER_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated())
                .andReturn();
        Player responsePlayer = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Player.class);
        Integer playerId = responsePlayer.getId();

        assertNotNull(playerId);

        assertTrue(playerId >= 1);

        Player actualPlayer = playerDao.findById(playerId).orElse(null);

        assertNotNull(actualPlayer);

        assertEquals(actualPlayer, responsePlayer);
    }


    @Test
    public void testShouldNotCreatePlayerWithNotValidBody() throws Exception {
        mvc.perform(post(Constants.Path.PLAYER_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testShouldRetrievePlayerById() throws Exception {
        System.out.println(playerDao.findAll());
        MvcResult mvcResult = mvc.perform(get(Constants.Path.PLAYER_API + Constants.Path.ID_PATH_VARIABLE, player.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Player actualPlayer = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Player.class);
        assertEquals(player, actualPlayer);
    }

    @Test
    public void testShouldNotRetrievePlayerByNotValidId() throws Exception {
        mvc.perform(get(Constants.Path.PLAYER_API + Constants.Path.ID_PATH_VARIABLE, notValidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testShouldUpdatePlayerById() throws Exception {
        String body = """
                {"name": "Olena"}"
                """;
        MvcResult mvcResult = mvc.perform(put(Constants.Path.PLAYER_API + Constants.Path.ID_PATH_VARIABLE, player.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();
        player = playerDao.findById(player.getId()).orElse(null);
        assertNotNull(player);
        Player responsePlayer = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Player.class);
        assertEquals(player, responsePlayer);
    }


    @Test
    public void testShouldNotUpdatePlayerByNotValidId() throws Exception {
        String body = """
                {"name": "Olena"}"
                """;
        mvc.perform(put(Constants.Path.TEAM_API + Constants.Path.ID_PATH_VARIABLE, notValidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testShouldNotUpdateTeamNotValidBody() throws Exception {
        String body = """
                {"yearOfBirth": 1600}"
                """;
        mvc.perform(put(Constants.Path.PLAYER_API + Constants.Path.ID_PATH_VARIABLE, player.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testShouldDeletePlayerById() throws Exception {
        mvc.perform(delete(Constants.Path.PLAYER_API + Constants.Path.ID_PATH_VARIABLE, player.getId()))
                .andExpect(status().isOk());
        assertNull(playerDao.findById(player.getId()).orElse(null));
    }

    @Test
    public void testShouldNotDeleteTeamByNotValidId() throws Exception {
        mvc.perform(delete(Constants.Path.PLAYER_API + Constants.Path.ID_PATH_VARIABLE, notValidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testShouldRetrievePlayerInfoAndPages() throws Exception {
        String body = """
                {
                "page": 0,
                "size": 1
                }
                """;

        MvcResult mvcResult = mvc.perform(post(Constants.Path.PLAYER_API + Constants.Path.LIST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andReturn();
        PlayerPageResponseDto playerPageResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PlayerPageResponseDto.class);
        assertEquals(playerPageResponseDto.getTotalPages(), 1);
        assertEquals(playerPageResponseDto.getList().size(), 1);

    }

    @Test
    public void testShouldUploadPlayersFromFile() throws Exception {
        File file1 = new File("src/test/resources/populatePlayersTest.json");
        byte[] attachedfile = Files.readAllBytes(file1.toPath());
        MockMultipartFile file  =  new MockMultipartFile(
                "file",
                "populatePlayers.json",
                MediaType.APPLICATION_JSON.getType(),
                attachedfile
        );
        MvcResult mvcResult = mvc.perform(multipart(Constants.Path.PLAYER_API+Constants.Path.UPLOAD).file(file))
                .andExpect(status().isOk())
                .andReturn();
        UploadJsonResponseDto uploadJsonResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),UploadJsonResponseDto.class);
        assertEquals(2,uploadJsonResponseDto.getNumCreated());
        assertEquals(1,uploadJsonResponseDto.getNumNotCreated());

    }

}