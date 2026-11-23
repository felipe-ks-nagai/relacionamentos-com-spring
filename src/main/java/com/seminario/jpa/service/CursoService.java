package com.seminario.jpa.service;

import com.seminario.jpa.dto.AlunoResumoResponse;
import com.seminario.jpa.dto.CursoRequest;
import com.seminario.jpa.dto.CursoResponse;
import com.seminario.jpa.entity.Aluno;
import com.seminario.jpa.entity.Curso;
import com.seminario.jpa.entity.Instrutor;
import com.seminario.jpa.repository.AlunoRepository;
import com.seminario.jpa.repository.CursoRepository;
import com.seminario.jpa.repository.InstrutorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final InstrutorRepository instrutorRepository;
    private final AlunoRepository alunoRepository;

    public CursoService(CursoRepository cursoRepository,
                        InstrutorRepository instrutorRepository,
                        AlunoRepository alunoRepository) {
        this.cursoRepository = cursoRepository;
        this.instrutorRepository = instrutorRepository;
        this.alunoRepository = alunoRepository;
    }

    // O JSON traz só o instrutorId; o service busca o instrutor e chama setInstrutor
    @Transactional
    public CursoResponse criar(CursoRequest request) {
        var instrutor = buscarInstrutor(request.instrutorId());
        var curso = new Curso();
        curso.setNome(request.nome());
        curso.setCargaHoraria(request.cargaHoraria());
        curso.setInstrutor(instrutor);
        return CursoResponse.from(cursoRepository.save(curso));
    }

    @Transactional(readOnly = true)
    public List<CursoResponse> listar() {
        return cursoRepository.findAll().stream().map(CursoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CursoResponse buscar(Long id) {
        return CursoResponse.from(buscarEntidade(id));
    }

    @Transactional
    public CursoResponse atualizar(Long id, CursoRequest request) {
        var curso = buscarEntidade(id);
        curso.setNome(request.nome());
        curso.setCargaHoraria(request.cargaHoraria());
        curso.setInstrutor(buscarInstrutor(request.instrutorId()));
        return CursoResponse.from(cursoRepository.save(curso));
    }

    @Transactional
    public CursoResponse trocarInstrutor(Long cursoId, Long instrutorId) {
        var curso = buscarEntidade(cursoId);
        curso.setInstrutor(buscarInstrutor(instrutorId));   // altera o lado dono
        return CursoResponse.from(cursoRepository.save(curso));
    }

    @Transactional(readOnly = true)
    public List<AlunoResumoResponse> listarAlunos(Long cursoId) {
        return buscarEntidade(cursoId).getAlunos().stream()
                .sorted(Comparator.comparing(Aluno::getId))
                .map(AlunoResumoResponse::from)
                .toList();
    }

    @Transactional
    public void excluir(Long id) {
        var curso = buscarEntidade(id);
        // limpa a tabela de junção (lado dono = Aluno) antes de apagar o curso
        alunoRepository.findByCursos_Id(id)
                .forEach(aluno -> aluno.getCursos().removeIf(c -> c.getId().equals(id)));
        cursoRepository.delete(curso);
    }

    private Curso buscarEntidade(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado: " + id));
    }

    private Instrutor buscarInstrutor(Long id) {
        return instrutorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instrutor não encontrado: " + id));
    }
}
