package com.seminario.jpa.config;

import com.seminario.jpa.entity.Aluno;
import com.seminario.jpa.entity.Curso;
import com.seminario.jpa.entity.Instrutor;
import com.seminario.jpa.entity.Perfil;
import com.seminario.jpa.repository.AlunoRepository;
import com.seminario.jpa.repository.CursoRepository;
import com.seminario.jpa.repository.InstrutorRepository;
import com.seminario.jpa.repository.PerfilRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Banco em memória: a cada start o Hibernate recria o esquema (create-drop)
 * e este componente recria os dados iniciais dos slides.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final PerfilRepository perfilRepository;
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;
    private final CursoRepository cursoRepository;

    public DataInitializer(PerfilRepository perfilRepository,
                           AlunoRepository alunoRepository,
                           InstrutorRepository instrutorRepository,
                           CursoRepository cursoRepository) {
        this.perfilRepository = perfilRepository;
        this.alunoRepository = alunoRepository;
        this.instrutorRepository = instrutorRepository;
        this.cursoRepository = cursoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Perfis (1:1)
        var perfilAna = perfilRepository.save(novoPerfil("11911111111", "São Paulo"));
        var perfilPedro = perfilRepository.save(novoPerfil("19922222222", "Campinas"));

        // Instrutores (1:N)
        var carlos = instrutorRepository.save(novoInstrutor("Carlos", "carlos@email.com"));
        var mariana = instrutorRepository.save(novoInstrutor("Mariana", "mariana@email.com"));

        // Cursos
        var java = cursoRepository.save(novoCurso("Java e Spring", 40, carlos));
        var apis = cursoRepository.save(novoCurso("APIs REST com Spring", 20, carlos));
        var jpa = cursoRepository.save(novoCurso("JPA e Hibernate", 24, mariana));

        // Alunos + matrículas (N:N)
        var ana = novoAluno("Ana", "ana@email.com", perfilAna);
        ana.getCursos().add(java);
        ana.getCursos().add(apis);
        alunoRepository.save(ana);

        var pedro = novoAluno("Pedro", "pedro@email.com", perfilPedro);
        pedro.getCursos().add(java);
        pedro.getCursos().add(jpa);
        alunoRepository.save(pedro);
    }

    private Perfil novoPerfil(String telefone, String cidade) {
        var p = new Perfil();
        p.setTelefone(telefone);
        p.setCidade(cidade);
        return p;
    }

    private Instrutor novoInstrutor(String nome, String email) {
        var i = new Instrutor();
        i.setNome(nome);
        i.setEmail(email);
        return i;
    }

    private Curso novoCurso(String nome, int cargaHoraria, Instrutor instrutor) {
        var c = new Curso();
        c.setNome(nome);
        c.setCargaHoraria(cargaHoraria);
        c.setInstrutor(instrutor);
        return c;
    }

    private Aluno novoAluno(String nome, String email, Perfil perfil) {
        var a = new Aluno();
        a.setNome(nome);
        a.setEmail(email);
        a.setPerfil(perfil);
        return a;
    }
}
