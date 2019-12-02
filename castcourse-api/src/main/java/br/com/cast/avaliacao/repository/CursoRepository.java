package br.com.cast.avaliacao.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cast.avaliacao.model.Curso;
import br.com.cast.avaliacao.repository.curso.CursoRepositoryQuery;

public interface CursoRepository extends JpaRepository<Curso, Long>, CursoRepositoryQuery{
	
	List<Curso> findAllByDataInicioLessThanEqualAndDataTerminoGreaterThanEqual(LocalDate dataTermino, LocalDate dataInicio);

}
