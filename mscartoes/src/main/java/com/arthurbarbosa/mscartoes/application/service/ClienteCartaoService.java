package com.arthurbarbosa.mscartoes.application.service;

import com.arthurbarbosa.mscartoes.domain.ClienteCartao;
import com.arthurbarbosa.mscartoes.infra.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository cartaoRepository;

    public List<ClienteCartao> listCartoesByCpf(String cpf) {
        return cartaoRepository.findByCpf(cpf);
    }
}
