package com.raizesdonordeste.api.application.exception;

public class CancelamentoNaoPermitidoException extends RuntimeException {
    public CancelamentoNaoPermitidoException(String msg){
        super(msg);
    }
}
