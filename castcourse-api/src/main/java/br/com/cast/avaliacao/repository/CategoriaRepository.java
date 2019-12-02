package br.com.cast.avaliacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cast.avaliacao.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
