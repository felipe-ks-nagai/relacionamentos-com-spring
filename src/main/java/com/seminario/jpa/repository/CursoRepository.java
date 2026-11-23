package com.seminario.jpa.repository;

import com.seminario.jpa.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByInstrutor_Id(Long instrutorId);

    List<Curso> findByInstrutor_Id(Long instrutorId);
}
