package com.raizesdonordeste.api.application.exception;

public class FalhaNoPagamentoException extends RuntimeException {
    public FalhaNoPagamentoException(String msg){
        super(msg);
    }
}
