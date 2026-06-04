package com.raizesdonordeste.api.exception;

public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException(String msg){
        super(msg);
    }
}
