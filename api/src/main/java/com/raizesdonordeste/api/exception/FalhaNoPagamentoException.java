package com.raizesdonordeste.api.exception;

public class FalhaNoPagamentoException extends RuntimeException {
    public FalhaNoPagamentoException(String msg){
        super(msg);
    }
}
