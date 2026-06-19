package com.raizesdonordeste.api.api.dto;

public record LoginResponseDTO(String accessToken, String tokenType, Long expiresIn, UsuarioSumarioDTO usuario) {

}
