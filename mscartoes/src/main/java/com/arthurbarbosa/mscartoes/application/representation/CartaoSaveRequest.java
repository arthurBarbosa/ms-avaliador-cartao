package com.arthurbarbosa.mscartoes.application.representation;

import com.arthurbarbosa.mscartoes.domain.Cartao;
import com.arthurbarbosa.mscartoes.domain.enums.BandeiraCartao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoSaveRequest {

    private String nome;
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limite;

    public Cartao toModel() {
        return new Cartao(nome, bandeira, renda, limite);
    }
}
