package com.seminario.jpa.service;

import com.seminario.jpa.dto.CursoResumoResponse;
import com.seminario.jpa.dto.InstrutorRequest;
import com.seminario.jpa.dto.InstrutorResponse;
import com.seminario.jpa.entity.Curso;
import com.seminario.jpa.entity.Instrutor;
import com.seminario.jpa.repository.CursoRepository;
import com.seminario.jpa.repository.InstrutorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
public class InstrutorService {

    private final InstrutorRepository instrutorRepository;
    private final CursoRepository cursoRepository;

    public InstrutorService(InstrutorRepository instrutorRepository, CursoRepository cursoRepository) {
        this.instrutorRepository = instrutorRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public InstrutorResponse criar(InstrutorRequest request) {
        var instrutor = new Instrutor();
        instrutor.setNome(request.nome());
        instrutor.setEmail(request.email());
        return InstrutorResponse.from(instrutorRepository.save(instrutor));
    }

    @Transactional(readOnly = true)
    public List<InstrutorResponse> listar() {
        return instrutorRepository.findAll().stream().map(InstrutorResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public InstrutorResponse buscar(Long id) {
        return InstrutorResponse.from(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<CursoResumoResponse> listarCursos(Long id) {
        buscarEntidade(id);
        return cursoRepository.findByInstrutor_Id(id).stream()
                .sorted(Comparator.comparing(Curso::getId))
                .map(CursoResumoResponse::from)
                .toList();
    }

    @Transactional
    public InstrutorResponse atualizar(Long id, InstrutorRequest request) {
        var instrutor = buscarEntidade(id);
        instrutor.setNome(request.nome());
        instrutor.setEmail(request.email());
        return InstrutorResponse.from(instrutorRepository.save(instrutor));
    }

    @Transactional
    public void excluir(Long id) {
        var instrutor = buscarEntidade(id);
        if (cursoRepository.existsByInstrutor_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O instrutor possui cursos. "
                            + "Troque ou exclua os cursos antes.");
        }
        instrutorRepository.delete(instrutor);
    }

    private Instrutor buscarEntidade(Long id) {
        return instrutorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instrutor não encontrado: " + id));
    }
}
