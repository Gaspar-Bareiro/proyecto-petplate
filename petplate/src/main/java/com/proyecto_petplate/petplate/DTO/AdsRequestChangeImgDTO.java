package com.proyecto_petplate.petplate.DTO;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdsRequestChangeImgDTO {
    private String token;
    private String location;
    private MultipartFile img;
}
