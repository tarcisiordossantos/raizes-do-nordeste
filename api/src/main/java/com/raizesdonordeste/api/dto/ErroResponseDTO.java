package com.raizesdonordeste.api.dto;

import java.time.LocalDateTime;

public record ErroResponseDTO(
    LocalDateTime timestamp,
    int status,
    String erro,
    String mensagem,
    String path
) {

}
