package com.raizesdonordeste.api.exception;

public class ProdutoIndisponivelException extends RuntimeException {
    public ProdutoIndisponivelException(String msg){
        super(msg);
    }
}
