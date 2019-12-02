package br.com.cast.avaliacao.repository.curso;

import java.util.List;

import br.com.cast.avaliacao.model.Curso;
import br.com.cast.avaliacao.repository.filter.CursoFilter;

public interface CursoRepositoryQuery {
	
	public List<Curso> filtrar(CursoFilter cursoFilter);
}
