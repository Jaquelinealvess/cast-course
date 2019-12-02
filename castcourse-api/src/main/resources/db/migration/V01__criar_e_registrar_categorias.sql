CREATE TABLE categoria(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO categoria values(1, 'Comportamental');
INSERT INTO categoria values(2, 'Programação');
INSERT INTO categoria values(3, 'Qualidade');
INSERT INTO categoria values(4, 'Processos'); 