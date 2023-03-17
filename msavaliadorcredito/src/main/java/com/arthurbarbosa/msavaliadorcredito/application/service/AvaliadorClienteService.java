package com.arthurbarbosa.msavaliadorcredito.application.service;

import com.arthurbarbosa.msavaliadorcredito.application.exceptions.DadosClienteNotFoundException;
import com.arthurbarbosa.msavaliadorcredito.application.exceptions.ErroComunicacaoMicroservicesException;
import com.arthurbarbosa.msavaliadorcredito.domain.model.*;
import com.arthurbarbosa.msavaliadorcredito.infra.clients.CartoesResourceClient;
import com.arthurbarbosa.msavaliadorcredito.infra.clients.ClienteResourceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorClienteService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourceClient cartoesResourceClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
        try {
            var dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
            var cartoesCliente = cartoesResourceClient.getCartoesByCliente(cpf);

            return SituacaoCliente.builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesCliente.getBody())
                    .build();
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
        try {
            var dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
            var cartoesResponse = cartoesResourceClient.getCartoesRendaAte(renda);
            List<Cartao> cartoes = cartoesResponse.getBody();

            List<CartaoAprovado> listaCartoesAprovado = cartoes.stream().map(cartao -> {
                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadebd = BigDecimal.valueOf(dadosCliente.getIdade());
                BigDecimal fator = idadebd.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);
                return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovado);
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }
}
