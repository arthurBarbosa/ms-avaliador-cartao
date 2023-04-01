package com.arthurbarbosa.msavaliadorcredito.application.representation;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProtocoloSolicitacaoCartao {
    private String protocolo;
    public ProtocoloSolicitacaoCartao(String protocolo) {
        this.protocolo = protocolo;
    }
}
