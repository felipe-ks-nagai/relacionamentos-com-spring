package com.seminario.jpa.controller;

import com.seminario.jpa.dto.PerfilRequest;
import com.seminario.jpa.dto.PerfilResponse;
import com.seminario.jpa.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfis")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PerfilResponse criar(@Valid @RequestBody PerfilRequest request) {
        return perfilService.criar(request);
    }

    @GetMapping
    public List<PerfilResponse> listar() {
        return perfilService.listar();
    }

    @GetMapping("/{id}")
    public PerfilResponse buscar(@PathVariable Long id) {
        return perfilService.buscar(id);
    }

    @PutMapping("/{id}")
    public PerfilResponse atualizar(@PathVariable Long id, @Valid @RequestBody PerfilRequest request) {
        return perfilService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        perfilService.excluir(id);
    }
}
