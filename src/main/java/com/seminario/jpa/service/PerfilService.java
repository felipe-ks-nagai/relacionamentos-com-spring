package com.seminario.jpa.service;

import com.seminario.jpa.dto.PerfilRequest;
import com.seminario.jpa.dto.PerfilResponse;
import com.seminario.jpa.entity.Perfil;
import com.seminario.jpa.repository.AlunoRepository;
import com.seminario.jpa.repository.PerfilRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final AlunoRepository alunoRepository;

    public PerfilService(PerfilRepository perfilRepository, AlunoRepository alunoRepository) {
        this.perfilRepository = perfilRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public PerfilResponse criar(PerfilRequest request) {
        var perfil = new Perfil();
        perfil.setTelefone(request.telefone());
        perfil.setCidade(request.cidade());
        return PerfilResponse.from(perfilRepository.save(perfil));
    }

    @Transactional(readOnly = true)
    public List<PerfilResponse> listar() {
        return perfilRepository.findAll().stream().map(PerfilResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PerfilResponse buscar(Long id) {
        return PerfilResponse.from(buscarEntidade(id));
    }

    @Transactional
    public PerfilResponse atualizar(Long id, PerfilRequest request) {
        var perfil = buscarEntidade(id);
        perfil.setTelefone(request.telefone());
        perfil.setCidade(request.cidade());
        return PerfilResponse.from(perfilRepository.save(perfil));
    }

    @Transactional
    public void excluir(Long id) {
        var perfil = buscarEntidade(id);
        if (alunoRepository.existsByPerfil_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O perfil está associado a um aluno. Desfaça a associação antes de excluir.");
        }
        perfilRepository.delete(perfil);
    }

    private Perfil buscarEntidade(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado: " + id));
    }
}
