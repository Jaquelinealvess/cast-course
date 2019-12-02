package br.com.cast.avaliacao.repository.curso;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;



import br.com.cast.avaliacao.model.Curso;
import br.com.cast.avaliacao.repository.filter.CursoFilter;

public class CursoRepositoryImpl implements CursoRepositoryQuery{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Curso> filtrar(CursoFilter cursoFilter) {
		System.out.println("filtrando");
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Curso> criteria = builder.createQuery(Curso.class);
		Root<Curso> root = criteria.from(Curso.class);
		
		//criar restrições
		Predicate[] predicates = criarRestricoes(cursoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Curso> query = manager.createQuery(criteria);
		return query.getResultList();
	}

	private Predicate[] criarRestricoes(CursoFilter cursoFilter, CriteriaBuilder builder, Root<Curso> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if(!StringUtils.isEmpty(cursoFilter.getDescricaoAssunto())) {
			System.out.println("restricao descricao" + cursoFilter.getDescricaoAssunto());
			predicates.add(builder.like(
					builder.lower(root.get("descricaoAssunto")), "%" + cursoFilter.getDescricaoAssunto().toLowerCase() + "%"));			
		}
		
		if(cursoFilter.getDataInicio() != null) {
			predicates.add(
					builder.greaterThanOrEqualTo(root.get("dataInicio"), cursoFilter.getDataInicio()));
		}
		
		if(cursoFilter.getDataTermino() != null) {
			predicates.add(
					builder.lessThanOrEqualTo(root.get("dataTermino"), cursoFilter.getDataTermino()));
		}		
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	

}
