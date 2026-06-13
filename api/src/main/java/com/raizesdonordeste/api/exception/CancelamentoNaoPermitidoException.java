package com.raizesdonordeste.api.exception;

public class CancelamentoNaoPermitidoException extends RuntimeException {
    public CancelamentoNaoPermitidoException(String msg){
        super(msg);
    }
}
