package com.arthurbarbosa.mscartoes.application.service;

import com.arthurbarbosa.mscartoes.domain.Cartao;
import com.arthurbarbosa.mscartoes.infra.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoService {

    private final CartaoRepository cartaoRepository;

    @Transactional
    public Cartao save(Cartao cartao) {
        return cartaoRepository.save(cartao);
    }

    public List<Cartao> getCartoesRendaMenorIgual(Long renda){
        var rendaBigDecimal = BigDecimal.valueOf(renda);
        return cartaoRepository.findByRendaLessThanEqual(rendaBigDecimal);
    }
}
