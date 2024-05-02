package ua.com.vitkovska.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ua.com.vitkovska.dto.response.UploadJsonResponseDto;
import ua.com.vitkovska.dto.player.CreatePlayerDto;
import ua.com.vitkovska.dto.player.PlayerDetailsDto;
import ua.com.vitkovska.dto.player.PlayerInfoDto;
import ua.com.vitkovska.dto.player.PlayerQueryDto;
import ua.com.vitkovska.dto.player.UpdatePlayerDto;

import java.io.IOException;
import java.util.Optional;

public interface PlayerService {

    PlayerDetailsDto create(CreatePlayerDto entity);

    Optional<PlayerDetailsDto> getById(int id);

    PlayerDetailsDto update(UpdatePlayerDto entity);

    boolean deleteById(int id);

    Page<PlayerInfoDto> list(PlayerQueryDto entity);

    void generateReport(HttpServletResponse httpServletResponse, PlayerQueryDto playerQueryDto);

    UploadJsonResponseDto uploadFromFile(MultipartFile multipart) throws IOException;
}
