package com.arthurbarbosa.msavaliadorcredito.application.resource;

import com.arthurbarbosa.msavaliadorcredito.application.exceptions.DadosClienteNotFoundException;
import com.arthurbarbosa.msavaliadorcredito.application.exceptions.ErroComunicacaoMicroservicesException;
import com.arthurbarbosa.msavaliadorcredito.application.service.AvaliadorClienteService;
import com.arthurbarbosa.msavaliadorcredito.domain.model.DadosAvaliacao;
import com.arthurbarbosa.msavaliadorcredito.domain.model.DadosSolicitacaoEmissaoCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("avaliacoes-credito")
public class AvaliadorCreditoResource {

    private final AvaliadorClienteService avaliadorClienteService;

    @GetMapping
    public String status() {
        return "msavaliadorcredito:Ok!";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity<?> consultarSituacaoCliente(@RequestParam("cpf") String cpf) {
        try {
            var situacaoCliente = avaliadorClienteService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.valueOf(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> realizarAvaliacao(@RequestBody DadosAvaliacao dadosAvaliacao) {
        try {
            var retornoAvaliacaoCliente = avaliadorClienteService.realizarAvaliacao(dadosAvaliacao.getCpf(), dadosAvaliacao.getRenda());
            return ResponseEntity.ok(retornoAvaliacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.valueOf(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping("solicitacoes-cartao")
    public ResponseEntity<?> solicitarEmissaoDeCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados) {
        try {
            var protocolo = avaliadorClienteService.solicitacaoEmissaoCartao(dados);
            return ResponseEntity.ok(protocolo);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}