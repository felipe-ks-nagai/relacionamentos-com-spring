package com.seminario.jpa.controller;

import com.seminario.jpa.dto.CursoResumoResponse;
import com.seminario.jpa.dto.InstrutorRequest;
import com.seminario.jpa.dto.InstrutorResponse;
import com.seminario.jpa.service.InstrutorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instrutores")
public class InstrutorController {

    private final InstrutorService instrutorService;

    public InstrutorController(InstrutorService instrutorService) {
        this.instrutorService = instrutorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstrutorResponse criar(@Valid @RequestBody InstrutorRequest request) {
        return instrutorService.criar(request);
    }

    @GetMapping
    public List<InstrutorResponse> listar() {
        return instrutorService.listar();
    }

    @GetMapping("/{id}")
    public InstrutorResponse buscar(@PathVariable Long id) {
        return instrutorService.buscar(id);
    }

    @GetMapping("/{id}/cursos")
    public List<CursoResumoResponse> listarCursos(@PathVariable Long id) {
        return instrutorService.listarCursos(id);
    }

    @PutMapping("/{id}")
    public InstrutorResponse atualizar(@PathVariable Long id, @Valid @RequestBody InstrutorRequest request) {
        return instrutorService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        instrutorService.excluir(id);
    }
}
