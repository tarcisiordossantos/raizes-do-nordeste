package com.raizesdonordeste.api.config;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.raizesdonordeste.api.dto.ErroResponseDTO;
import com.raizesdonordeste.api.exception.CadastroDuplicadoException;
import com.raizesdonordeste.api.exception.FalhaNoPagamentoException;
import com.raizesdonordeste.api.exception.ProdutoIndisponivelException;
import com.raizesdonordeste.api.exception.UnidadeSemCardapioAtivoException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CadastroDuplicadoException.class)
    public ResponseEntity<ErroResponseDTO> tratarCadastroDuplicado(
        CadastroDuplicadoException ex, HttpServletRequest request)
    {
        ErroResponseDTO erro = new ErroResponseDTO(
            LocalDateTime.now(),
            HttpStatus.UNPROCESSABLE_CONTENT.value(),
            "Regra de Negócio Violada",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(erro);
    }

    @ExceptionHandler(FalhaNoPagamentoException.class)
    public ResponseEntity<ErroResponseDTO> tratarFalhaNoPagamento(
        FalhaNoPagamentoException ex, HttpServletRequest request
    ){
        ErroResponseDTO erro = new ErroResponseDTO(
            LocalDateTime.now(),
            HttpStatus.UNPROCESSABLE_CONTENT.value(),
            "Falha no Processamento do Pagamento",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(erro);
    }

    @ExceptionHandler(ProdutoIndisponivelException.class)
    public ResponseEntity<ErroResponseDTO> tratarProdutoIndisponivel(
        ProdutoIndisponivelException ex, HttpServletRequest request
    ){
        ErroResponseDTO erro = new ErroResponseDTO(
            LocalDateTime.now(),
            HttpStatus.UNPROCESSABLE_CONTENT.value(),
            "Produto Indisponível",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(erro);
    }

    @ExceptionHandler(UnidadeSemCardapioAtivoException.class)
    public ResponseEntity<ErroResponseDTO> tratarUnidadeInoperante(
        UnidadeSemCardapioAtivoException ex, HttpServletRequest request
    ){
        ErroResponseDTO erro = new ErroResponseDTO(
            LocalDateTime.now(),
            HttpStatus.UNPROCESSABLE_CONTENT.value(),
            "Unidade Inoperante",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(erro);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroResponseDTO> tratarRecursoNaoEncontrada(
        EntityNotFoundException ex, HttpServletRequest request
    ){
        ErroResponseDTO erro = new ErroResponseDTO(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Recurso Não Encontrado",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponseDTO> tratarArgumentoInvalido(
        IllegalArgumentException ex, HttpServletRequest request
    ){
        ErroResponseDTO erro = new ErroResponseDTO(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Argumento Inválido",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }


}
