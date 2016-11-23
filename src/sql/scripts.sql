/* 
 * TO CHANGE THIS LICENSE HEADER, CHOOSE LICENSE HEADERS IN PROJECT PROPERTIES.
 * TO CHANGE THIS TEMPLATE FILE, CHOOSE TOOLS | TEMPLATES
 * AND OPEN THE TEMPLATE IN THE EDITOR.
 */
/**
 * AUTHOR:  DOUGLLASFPS
 * CREATED: 10/04/2016
 */

CREATE DATABASE FRENTELITE;

CREATE SCHEMA ECF;
CREATE SCHEMA ACESSO;
CREATE SCHEMA CONFIGURACAO;

CREATE TABLE ECF.FOTO(
	ID 		  SERIAL NOT NULL PRIMARY KEY,
	NOME 	  CHARACTER VARYING(255),
	BYTES 	  BYTEA
);

CREATE TABLE ECF.CATEGORIA_PRODUTO(
	ID 			SERIAL NOT NULL PRIMARY KEY,
	DESCRICAO 	CHARACTER VARYING(255),
	ID_FOTO 	INT
);

CREATE TABLE ECF.PRODUTO(
  	ID 					SERIAL NOT NULL PRIMARY KEY,
  	DESCRICAO 			CHARACTER VARYING(255),
  	VALOR_CUSTO 		NUMERIC(16,2),
  	VALOR_VENDA 		NUMERIC(16,2),
  	QTD_ESTOQUE 		INTEGER,
  	QTD_ESTOQUE_MINIMO 	INTEGER,
 	MEDIDA 				CHARACTER VARYING(255),
  	CODIGO 				CHARACTER VARYING(255),
  	ID_FOTO 	 		INT,
  	ID_CATEGORIA 		INT NOT NULL
);

CREATE TABLE ECF.ITEM_VENDA(
	ID 						SERIAL NOT NULL PRIMARY KEY,
	ID_PRODUTO 				INTEGER NOT NULL,
	ID_VENDA 				INTEGER NOT NULL,
	QUANTIDADE 				NUMERIC(16,3),
	VALOR_CUSTO_UNITARIO 	NUMERIC(16,2),
	VALOR_CUSTO_TOTAL 		NUMERIC(16,2),
	VALOR_UNITARIO 			NUMERIC(16,2),
	VALOR_VENDA 			NUMERIC(16,2),
	VALOR_DESCONTO 			NUMERIC (16,2)
);

CREATE TABLE ECF.VENDA(
	ID 				SERIAL NOT NULL PRIMARY KEY,
	DATA_VENDA 		TIMESTAMP,
	DATA	 		DATE,
	VALOR_TOTAL 	NUMERIC(16,2),
	VALOR_DESCONTO 	NUMERIC(16,2),
	CANCELADA BOOL 	DEFAULT FALSE,
	ID_USUARIO 		INTEGER
);

CREATE TABLE ECF.PAGAMENTO (
    ID 				SERIAL NOT NULL PRIMARY KEY,
    ID_VENDA 		INTEGER NOT NULL,
    VALOR_DESCONTO 	NUMERIC(16,2),
    VALOR_CARTAO 	NUMERIC(16,2),
    VALOR_DINHEIRO 	NUMERIC(16,2),
    VALOR_DEBITO	NUMERIC(16,2)
);

CREATE TABLE ECF.TARA(
	ID 		  SERIAL NOT NULL PRIMARY KEY,
	DESCRICAO VARCHAR(100),
	PESO 	  NUMERIC(16,3)
);

CREATE TABLE ECF.CLIENTE(
	ID 		  SERIAL NOT NULL PRIMARY KEY,
	NOME 	  VARCHAR(100),
	TELEFONE  VARCHAR(20),
	ENDERECO  VARCHAR(255)
);

CREATE TABLE ECF.DELIVERY(
	ID 				SERIAL 	NOT NULL PRIMARY KEY,
	ID_VENDA 		INTEGER,
	ID_CLIENTE 		INTEGER,
	CANCELADO 		BOOL,
	DETALHES_PEDIDO TEXT,
	TAXA 			NUMERIC(16,2)
);

CREATE TABLE ACESSO.USUARIO(
    ID 				SERIAL NOT NULL PRIMARY KEY,
    NOME 			VARCHAR(70),
    LOGIN 			VARCHAR(50),
    SENHA 			VARCHAR(50),
    ADMINISTRADOR 	BOOLEAN DEFAULT FALSE
);

CREATE TABLE ACESSO.MODULO(
    ID 			SERIAL NOT NULL PRIMARY KEY,
    NOME 		VARCHAR(70)
);

CREATE TABLE ACESSO.MODULO_USUARIO(
	ID 			SERIAL NOT NULL PRIMARY KEY,
	ID_USUARIO 	INTEGER,
	ID_MODULO 	INTEGER
);

CREATE TABLE CONFIGURACAO.BALANCA(
	ID 			SERIAL NOT NULL PRIMARY KEY,
	SERIALPORT 	VARCHAR(20),
	BAUDRATE 	INTEGER,
	DATABITS 	INTEGER,
	STOPBITS 	INTEGER,
	PARITY 		INTEGER,
	BYTES 		INTEGER,
	ASCICODE 	INTEGER
);

CREATE TABLE CONFIGURACAO.ESTABELECIMENTO (
	ID 			 SERIAL NOT NULL PRIMARY KEY,
	RAZAO_SOCIAL VARCHAR(70) NOT NULL,
	ENDERECO 	 VARCHAR(255),
	CIDADE       VARCHAR(100),
	TELEFONE 	 VARCHAR(20),
	CNPJ 		 VARCHAR(30)
);

CREATE TABLE CONFIGURACAO.IMPRESSORA (
	ID 			 SERIAL NOT NULL PRIMARY KEY,
	PORTA 		 VARCHAR(10) NOT NULL,
	VELOCIDADE 	 VARCHAR(10) NOT NULL
);

CREATE TABLE ECF.FLUXO_CAIXA(
	ID						SERIAL NOT NULL PRIMARY KEY,
	DATA_ABERTURA			TIMESTAMP NOT NULL,
	DATA 					DATE,
	TIPO					VARCHAR(30),
	FUNDO_CAIXA 			NUMERIC(16,2),
	ID_USUARIO				INTEGER,
	ID_FECHAMENTO			INTEGER
);

CREATE TABLE ECF.SANGRIA(
	ID 				SERIAL NOT NULL PRIMARY KEY,
	DATA 			DATE,
	DATA_SANGRIA 	TIMESTAMP NOT NULL DEFAULT NOW(),
	VALOR 			NUMERIC(16,2)
);

ALTER TABLE ECF.PAGAMENTO ADD CONSTRAINT FK_VENDA_PAGAMENTO FOREIGN KEY (ID_VENDA) REFERENCES ECF.VENDA(ID);
ALTER TABLE ECF.FLUXO_CAIXA ADD CONSTRAINT FK_USUARIO FOREIGN KEY (ID_USUARIO) REFERENCES ACESSO.USUARIO(ID);
ALTER TABLE ECF.FLUXO_CAIXA ADD CONSTRAINT FK_USUARIO_FECHAMENTO FOREIGN KEY (ID_USUARIO) REFERENCES ACESSO.USUARIO(ID);
ALTER TABLE ECF.FLUXO_CAIXA ADD CONSTRAINT FK_FECHAMENTO FOREIGN KEY (ID_FECHAMENTO) REFERENCES ECF.FLUXO_CAIXA(ID);

ALTER TABLE ECF.ITEM_VENDA ADD CONSTRAINT FK_PRODUTO_ITEM_VENDA FOREIGN KEY (ID_PRODUTO) REFERENCES ECF.PRODUTO(ID);
ALTER TABLE ECF.ITEM_VENDA ADD CONSTRAINT FK_VENDA_ITEM_VENDA FOREIGN KEY (ID_VENDA) REFERENCES ECF.VENDA(ID);

ALTER TABLE ECF.VENDA ADD CONSTRAINT FK_VENDA_USUARIO FOREIGN KEY (ID_USUARIO)  REFERENCES ACESSO.USUARIO(ID);

ALTER TABLE ECF.PRODUTO ADD CONSTRAINT FK_PRODUTO_FOTO FOREIGN KEY (ID_FOTO)  REFERENCES ECF.FOTO(ID);
ALTER TABLE ECF.PRODUTO ADD CONSTRAINT FK_PRODUTO_CATEGORIA_PRODUTO FOREIGN KEY (ID_CATEGORIA)  REFERENCES ECF.CATEGORIA_PRODUTO(ID);

ALTER TABLE ECF.CATEGORIA_PRODUTO ADD CONSTRAINT FK_CATEGORIA_PRODUTO_FOTO FOREIGN KEY (ID_FOTO)  REFERENCES ECF.FOTO(ID);

ALTER TABLE ECF.DELIVERY ADD CONSTRAINT FK_DELIVERY_VENDA FOREIGN KEY (ID_VENDA)  REFERENCES ECF.VENDA(ID);
ALTER TABLE ECF.DELIVERY ADD CONSTRAINT FK_DELIVERY_CLIENTE FOREIGN KEY (ID_CLIENTE)  REFERENCES ECF.CLIENTE(ID);

ALTER TABLE ACESSO.MODULO_USUARIO ADD CONSTRAINT FK_MODULO_USUARIO_USUARIO FOREIGN KEY (ID_USUARIO) REFERENCES ACESSO.USUARIO(ID);
ALTER TABLE ACESSO.MODULO_USUARIO ADD CONSTRAINT FK_MODULO_USUARIO_MODULO FOREIGN KEY (ID_MODULO) REFERENCES ACESSO.MODULO(ID);

INSERT INTO ACESSO.MODULO VALUES ( DEFAULT, 'VENDAS' );
INSERT INTO ACESSO.MODULO VALUES ( DEFAULT, 'CADASTRO PRODUTO' );
INSERT INTO ACESSO.MODULO VALUES ( DEFAULT, 'CADASTRO CATEGORIAS' );
INSERT INTO ACESSO.MODULO VALUES ( DEFAULT, 'USUARIOS' );
INSERT INTO ACESSO.MODULO VALUES ( DEFAULT, 'RELATORIOS' );

INSERT INTO ACESSO.USUARIO VALUES ( DEFAULT, 'DOUGLLAS', 'admin', '21232f297a57a5a743894a0e4a801fc3');