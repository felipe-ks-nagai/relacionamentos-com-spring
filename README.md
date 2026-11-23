# Seminário JPA · Relacionamentos (backend)

Spring Boot 3 + Spring Data JPA + H2 (em memória). Implementa todos os exemplos dos slides:

| Relação | Entidades | Dono da FK |
|---|---|---|
| `@OneToOne` | Aluno ↔ Perfil | `alunos.perfil_id` (UNIQUE, aceita null) |
| `@ManyToOne` / `@OneToMany` | Curso → Instrutor | `cursos.instrutor_id` (NOT NULL) |
| `@ManyToMany` | Aluno ↔ Curso | tabela `aluno_curso` (`@JoinTable` em Aluno) |

## Como rodar

Requisitos: Java 17+ e Maven 3.8+.

```bash
mvn spring-boot:run
```

- API: http://localhost:8080
- Console H2: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:relacionamentos` · usuário `sa` · senha em branco
- O arquivo `requests.http` segue o roteiro das demonstrações dos slides.

## Dados iniciais (DataInitializer)

- Perfis: 1 = São Paulo (Ana), 2 = Campinas (Pedro)
- Alunos: Ana (id 1), Pedro (id 2)
- Instrutores: Carlos (1), Mariana (2)
- Cursos: Java e Spring (1, Carlos), APIs REST com Spring (2, Carlos), JPA e Hibernate (3, Mariana)
- Matrículas: Ana → 1, 2 · Pedro → 1, 3

## Endpoints

**Perfis** — `POST /perfis` · `GET /perfis` · `GET /perfis/{id}` · `PUT /perfis/{id}` · `DELETE /perfis/{id}` (409 se estiver em uso)

**Alunos** — `POST /alunos` · `GET /alunos` · `GET /alunos/{id}` · `PUT /alunos/{id}` · `DELETE /alunos/{id}`
- 1:1: `PUT /alunos/{id}/perfil/{perfilId}` (409 se o perfil é de outro aluno) · `DELETE /alunos/{id}/perfil` (204)
- N:N: `POST /alunos/{alunoId}/cursos/{cursoId}` · `GET /alunos/{id}/cursos` · `DELETE /alunos/{alunoId}/cursos/{cursoId}` (204)

**Instrutores** — `POST /instrutores` · `GET /instrutores` · `GET /instrutores/{id}` · `GET /instrutores/{id}/cursos` · `PUT /instrutores/{id}` · `DELETE /instrutores/{id}` (409 se tiver cursos)

**Cursos** — `POST /cursos` (com `instrutorId`) · `GET /cursos` · `GET /cursos/{id}` · `PUT /cursos/{id}` · `PUT /cursos/{id}/instrutor/{instrutorId}` · `GET /cursos/{id}/alunos` · `DELETE /cursos/{id}`

## Pontos do seminário no código

- Dono × inverso: `Aluno`/`Curso` (`@JoinColumn`/`@JoinTable`) gravam; `Instrutor.cursos` e `Curso.alunos` (`mappedBy`) só leem.
- Integridade: `InstrutorService.excluir` e `PerfilService.excluir` respondem 409 antes de o banco recusar.
- DTOs: a entidade nunca sai do service; `*ResumoResponse` corta os ciclos do JSON.
- `spring.jpa.open-in-view=false`: os DTOs são montados dentro do `@Transactional`.
- Para ver a recursão infinita (demo do slide 63), faça um controller devolver a entidade `Instrutor`.
