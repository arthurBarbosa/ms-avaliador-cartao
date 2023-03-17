package com.arthurbarbosa.msavaliadorcredito.application.exceptions;

import lombok.Getter;

public class ErroComunicacaoMicroservicesException extends Exception {
    @Getter
    private Integer status;

    public ErroComunicacaoMicroservicesException(String msg,Integer status){
        super(msg);
        this.status = status;
    }
}
