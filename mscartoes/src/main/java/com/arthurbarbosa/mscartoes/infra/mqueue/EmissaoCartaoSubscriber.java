package com.arthurbarbosa.mscartoes.infra.mqueue;

import com.arthurbarbosa.mscartoes.application.representation.DadosSolicitacaoEmissaoCartao;
import com.arthurbarbosa.mscartoes.domain.ClienteCartao;
import com.arthurbarbosa.mscartoes.infra.repository.CartaoRepository;
import com.arthurbarbosa.mscartoes.infra.repository.ClienteCartaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmissaoCartaoSubscriber {

    private final CartaoRepository cartaoRepository;
    private final ClienteCartaoRepository clienteCartaoRepository;

    @RabbitListener(queues = "${mq.queues.emissao-cartoes}")
    public void receberSolicitacaoEmissao(@Payload String payload){
        try {
            var mapper = new ObjectMapper();

            var dados = mapper.readValue(payload, DadosSolicitacaoEmissaoCartao.class);
            var cartao = cartaoRepository.findById(dados.getIdCartao()).orElseThrow();

            ClienteCartao clienteCartao = new ClienteCartao();
            clienteCartao.setCartao(cartao);
            clienteCartao.setCpf(dados.getCpf());
            clienteCartao.setLimiteBasico(dados.getLimiteLiberado());

            clienteCartaoRepository.save(clienteCartao);
        }catch (Exception e){
            log.error("Erro ao receber solicitacao de emissao de cartao: {} ", e.getMessage());
        }
    }
}
