package br.com.cast.avaliacao.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.cast.avaliacao.model.Curso;
import br.com.cast.avaliacao.repository.CursoRepository;
import br.com.cast.avaliacao.service.exception.DataInicioMenorDataAtualException;
import br.com.cast.avaliacao.service.exception.ExisteCursoPlanejadoNoPeriodoInformadoException;

@Service
public class CursoService {
	
	@Autowired
	private CursoRepository cursoRepository;

	public Curso atualizar(Long codigo, Curso curso) {
		  Curso cursoSalvo = this.cursoRepository.findById(codigo)
				  .orElseThrow(() -> new EmptyResultDataAccessException(1));
		  
			  BeanUtils.copyProperties(curso, cursoSalvo, "codigo");

			  return this.cursoRepository.save(cursoSalvo);
	}

	public Curso salvar(Curso curso) {		
		LocalDate dataAtual = LocalDate.now();
		if (curso.getDataInicio().isBefore(dataAtual)) {
			//System.out.println("Não é permitida a inclusão de cursos com a data de inicio menor que a data Atual");
			throw new DataInicioMenorDataAtualException();
		}else {
			List<Curso> cursosPlanejados = cursoRepository.findAllByDataInicioLessThanEqualAndDataTerminoGreaterThanEqual(curso.getDataTermino(), curso.getDataInicio());
			if (cursosPlanejados.size() > 0) {
				//System.out.println("Existe(m) curso(s) planejado(s) dentro do período informado!");
				throw new ExisteCursoPlanejadoNoPeriodoInformadoException();
			}
		}
		return this.cursoRepository.save(curso);
	}

}