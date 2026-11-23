package com.seminario.jpa.controller;

import com.seminario.jpa.dto.AlunoResumoResponse;
import com.seminario.jpa.dto.CursoRequest;
import com.seminario.jpa.dto.CursoResponse;
import com.seminario.jpa.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CursoResponse criar(@Valid @RequestBody CursoRequest request) {
        return cursoService.criar(request);
    }

    @GetMapping
    public List<CursoResponse> listar() {
        return cursoService.listar();
    }

    @GetMapping("/{id}")
    public CursoResponse buscar(@PathVariable Long id) {
        return cursoService.buscar(id);
    }

    @PutMapping("/{id}")
    public CursoResponse atualizar(@PathVariable Long id, @Valid @RequestBody CursoRequest request) {
        return cursoService.atualizar(id, request);
    }

    @PutMapping("/{id}/instrutor/{instrutorId}")
    public CursoResponse trocarInstrutor(@PathVariable Long id, @PathVariable Long instrutorId) {
        return cursoService.trocarInstrutor(id, instrutorId);
    }

    @GetMapping("/{id}/alunos")
    public List<AlunoResumoResponse> listarAlunos(@PathVariable Long id) {
        return cursoService.listarAlunos(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        cursoService.excluir(id);
    }
}
