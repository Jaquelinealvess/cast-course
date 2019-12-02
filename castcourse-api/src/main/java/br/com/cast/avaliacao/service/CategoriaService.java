package br.com.cast.avaliacao.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.cast.avaliacao.model.Categoria;
import br.com.cast.avaliacao.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria atualizar(Long codigo, Categoria categoria) {
		  Categoria categoriaSalva = this.categoriaRepository.findById(codigo)
				  .orElseThrow(() -> new EmptyResultDataAccessException(1));
		  
			  BeanUtils.copyProperties(categoria, categoriaSalva, "codigo");

			  return this.categoriaRepository.save(categoriaSalva);
	}
}
