package com.seminario.jpa.repository;

import com.seminario.jpa.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    boolean existsByPerfil_Id(Long perfilId);

    Optional<Aluno> findByPerfil_Id(Long perfilId);

    List<Aluno> findByCursos_Id(Long cursoId);
}
