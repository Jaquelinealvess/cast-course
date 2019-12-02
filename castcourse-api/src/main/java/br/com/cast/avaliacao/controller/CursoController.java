package br.com.cast.avaliacao.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.cast.avaliacao.event.RecursoCriadoEvent;
import br.com.cast.avaliacao.exceptionhandler.AvaliacaoExceptionHandler.Erro;
import br.com.cast.avaliacao.model.Curso;
import br.com.cast.avaliacao.repository.CursoRepository;
import br.com.cast.avaliacao.repository.filter.CursoFilter;
import br.com.cast.avaliacao.service.CursoService;
import br.com.cast.avaliacao.service.exception.DataInicioMenorDataAtualException;
import br.com.cast.avaliacao.service.exception.ExisteCursoPlanejadoNoPeriodoInformadoException;

@RestController
@RequestMapping("/cursos")
public class CursoController {
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@Autowired
	CursoService cursoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	public List<Curso> pesquisar(CursoFilter cursoFilter) {
		return cursoRepository.filtrar(cursoFilter);
	}

	@PostMapping	
	public ResponseEntity<Curso> criar(@Valid @RequestBody Curso curso, HttpServletResponse response) {
		Curso cursoSalvo = cursoService.salvar(curso);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, cursoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(cursoSalvo);
	}	
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
	    Optional curso = this.cursoRepository.findById(codigo);
	    return curso.isPresent() ? 
	            ResponseEntity.ok(curso.get()) : ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
	  this.cursoRepository.deleteById(codigo);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Curso> atualizar(@PathVariable Long codigo,@Valid @RequestBody Curso curso) {
		  Curso cursoSalvo = cursoService.atualizar(codigo, curso);
		  return ResponseEntity.ok(cursoSalvo);
		}
	
	
	// Poderia tratar as exceções abaixo em AvaliacaoExceptionHandler, contudo irei mante-la mais generica possivel, tratando as exceções de todos os controladores 
	@ExceptionHandler({ DataInicioMenorDataAtualException.class })
	public ResponseEntity<Object> handleDataInicioMenorDataAtualException(DataInicioMenorDataAtualException ex) {
		String mensagemUsuario = messageSource.getMessage("curso.dataInicial-menor-dataFinal", null,  LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro( mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);		
	}
	
	@ExceptionHandler({ ExisteCursoPlanejadoNoPeriodoInformadoException.class })
	public ResponseEntity<Object> handleExisteCursoPlanejadoNoPeriodoInformadoException(ExisteCursoPlanejadoNoPeriodoInformadoException ex) {
		String mensagemUsuario = messageSource.getMessage("curso.periodo-ja-planejado", null,  LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro( mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);		
	}
	
}
