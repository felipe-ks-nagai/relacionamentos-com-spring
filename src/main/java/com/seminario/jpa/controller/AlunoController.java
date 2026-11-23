package com.seminario.jpa.controller;

import com.seminario.jpa.dto.AlunoRequest;
import com.seminario.jpa.dto.AlunoResponse;
import com.seminario.jpa.dto.CursoResumoResponse;
import com.seminario.jpa.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    // ---------- CRUD ----------

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoResponse criar(@Valid @RequestBody AlunoRequest request) {
        return alunoService.criar(request);
    }

    @GetMapping
    public List<AlunoResponse> listar() {
        return alunoService.listar();
    }

    @GetMapping("/{id}")
    public AlunoResponse buscar(@PathVariable Long id) {
        return alunoService.buscar(id);
    }

    @PutMapping("/{id}")
    public AlunoResponse atualizar(@PathVariable Long id, @Valid @RequestBody AlunoRequest request) {
        return alunoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        alunoService.excluir(id);
    }

    // ---------- 1:1 Perfil ----------

    @PutMapping("/{id}/perfil/{perfilId}")
    public AlunoResponse associarPerfil(@PathVariable Long id, @PathVariable Long perfilId) {
        return alunoService.associarPerfil(id, perfilId);
    }

    @DeleteMapping("/{id}/perfil")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerPerfil(@PathVariable Long id) {
        alunoService.removerPerfil(id);
    }

    // ---------- N:N Cursos ----------

    @PostMapping("/{alunoId}/cursos/{cursoId}")
    public AlunoResponse matricular(@PathVariable Long alunoId, @PathVariable Long cursoId) {
        return alunoService.matricular(alunoId, cursoId);
    }

    @GetMapping("/{id}/cursos")
    public List<CursoResumoResponse> listarCursos(@PathVariable Long id) {
        return alunoService.listarCursos(id);
    }

    @DeleteMapping("/{alunoId}/cursos/{cursoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelarMatricula(@PathVariable Long alunoId, @PathVariable Long cursoId) {
        alunoService.cancelarMatricula(alunoId, cursoId);
    }
}
