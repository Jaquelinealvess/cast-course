CREATE TABLE curso (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	descricao_assunto VARCHAR(50) NOT NULL,
	data_inicio DATE NOT NULL,
	data_termino DATE NOT NULL,
	quantidade_alunos BIGINT(20),
	codigo_categoria BIGINT(20),
	FOREIGN KEY (codigo_categoria) REFERENCES categoria(codigo) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
