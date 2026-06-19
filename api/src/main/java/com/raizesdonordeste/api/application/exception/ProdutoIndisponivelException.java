package com.raizesdonordeste.api.application.exception;

public class ProdutoIndisponivelException extends RuntimeException {
    public ProdutoIndisponivelException(String msg){
        super(msg);
    }
}
