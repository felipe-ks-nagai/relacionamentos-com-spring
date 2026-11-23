package com.seminario.jpa.service;

import com.seminario.jpa.dto.AlunoRequest;
import com.seminario.jpa.dto.AlunoResponse;
import com.seminario.jpa.dto.CursoResumoResponse;
import com.seminario.jpa.entity.Aluno;
import com.seminario.jpa.entity.Curso;
import com.seminario.jpa.repository.AlunoRepository;
import com.seminario.jpa.repository.CursoRepository;
import com.seminario.jpa.repository.PerfilRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PerfilRepository perfilRepository;
    private final CursoRepository cursoRepository;

    public AlunoService(AlunoRepository alunoRepository,
                        PerfilRepository perfilRepository,
                        CursoRepository cursoRepository) {
        this.alunoRepository = alunoRepository;
        this.perfilRepository = perfilRepository;
        this.cursoRepository = cursoRepository;
    }

    // ---------- CRUD ----------

    @Transactional
    public AlunoResponse criar(AlunoRequest request) {
        var aluno = new Aluno();
        aluno.setNome(request.nome());
        aluno.setEmail(request.email());
        return AlunoResponse.from(alunoRepository.save(aluno));
    }

    @Transactional(readOnly = true)
    public List<AlunoResponse> listar() {
        return alunoRepository.findAll().stream().map(AlunoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscar(Long id) {
        return AlunoResponse.from(buscarEntidade(id));
    }

    @Transactional
    public AlunoResponse atualizar(Long id, AlunoRequest request) {
        var aluno = buscarEntidade(id);
        aluno.setNome(request.nome());
        aluno.setEmail(request.email());
        return AlunoResponse.from(alunoRepository.save(aluno));
    }

    @Transactional
    public void excluir(Long id) {
        var aluno = buscarEntidade(id);
        aluno.getCursos().clear();      // limpa a tabela de junção antes de apagar
        alunoRepository.delete(aluno);  // o perfil (se existir) continua cadastrado
    }

    // ---------- @OneToOne: Aluno <-> Perfil ----------

    @Transactional
    public AlunoResponse associarPerfil(Long alunoId, Long perfilId) {
        var aluno = buscarEntidade(alunoId);
        var perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Perfil não encontrado: " + perfilId));

        // perfil já está com outro aluno? -> 409
        alunoRepository.findByPerfil_Id(perfilId)
                .filter(outro -> !outro.getId().equals(alunoId))
                .ifPresent(outro -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "O perfil " + perfilId + " já pertence ao aluno " + outro.getId());
                });

        aluno.setPerfil(perfil);
        return AlunoResponse.from(alunoRepository.save(aluno));
    }

    @Transactional
    public void removerPerfil(Long alunoId) {
        var aluno = buscarEntidade(alunoId);
        aluno.setPerfil(null);          // só zera a FK, não apaga o perfil
        alunoRepository.save(aluno);
    }

    // ---------- @ManyToMany: Aluno <-> Curso ----------

    @Transactional
    public AlunoResponse matricular(Long alunoId, Long cursoId) {
        var aluno = buscarEntidade(alunoId);
        var curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Curso não encontrado: " + cursoId));

        aluno.getCursos().add(curso);   // lado dono: o Hibernate faz o INSERT em aluno_curso
        return AlunoResponse.from(alunoRepository.save(aluno));
    }

    @Transactional
    public void cancelarMatricula(Long alunoId, Long cursoId) {
        var aluno = buscarEntidade(alunoId);
        boolean removido = aluno.getCursos().removeIf(c -> c.getId().equals(cursoId));
        if (!removido) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "O aluno " + alunoId + " não está matriculado no curso " + cursoId);
        }
        alunoRepository.save(aluno);
    }

    @Transactional(readOnly = true)
    public List<CursoResumoResponse> listarCursos(Long alunoId) {
        return buscarEntidade(alunoId).getCursos().stream()
                .sorted(Comparator.comparing(Curso::getId))
                .map(CursoResumoResponse::from)
                .toList();
    }

    private Aluno buscarEntidade(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado: " + id));
    }
}
