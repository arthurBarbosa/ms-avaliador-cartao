package com.arthurbarbosa.mscartoes.application.resource;

import com.arthurbarbosa.mscartoes.application.representation.CartaoSaveRequest;
import com.arthurbarbosa.mscartoes.application.service.CartaoService;
import com.arthurbarbosa.mscartoes.application.service.ClienteCartaoService;
import com.arthurbarbosa.mscartoes.domain.Cartao;
import com.arthurbarbosa.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("cartoes")
public class CartaoResource {

    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

    @GetMapping
    public String status() {
        return "mscartoes:ok!";
    }

    @PostMapping
    public ResponseEntity save(@RequestBody CartaoSaveRequest request) {
        Cartao cartao = request.toModel();
        cartaoService.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") Long renda) {
        var list = cartaoService.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(list);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam("cpf") String cpf) {
        var lista = clienteCartaoService
                .listCartoesByCpf(cpf).stream()
                .map(CartoesPorClienteResponse::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }
}
