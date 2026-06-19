package com.raizesdonordeste.api.application.exception;

public class CadastroDuplicadoException extends RuntimeException {
    public CadastroDuplicadoException(String msg){
        super(msg);
    }
}
