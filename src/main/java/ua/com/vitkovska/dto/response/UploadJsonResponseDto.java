package ua.com.vitkovska.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadJsonResponseDto {
    private Integer numCreated;
    private Integer numNotCreated;
}
