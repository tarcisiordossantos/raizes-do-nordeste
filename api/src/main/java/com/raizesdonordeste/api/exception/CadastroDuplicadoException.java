package com.raizesdonordeste.api.exception;

public class CadastroDuplicadoException extends RuntimeException {
    public CadastroDuplicadoException(String msg){
        super(msg);
    }
}
