CREATE TABLE tasks (
   id UUID PRIMARY KEY,
   titulo VARCHAR(255) NOT NULL,
   descricao VARCHAR(1000),
   status VARCHAR(50) NOT NULL,
   data_criacao TIMESTAMP NOT NULL,
   data_conclusao TIMESTAMP NULL,
   usuario_id UUID NOT NULL
);

CREATE TABLE subtasks (
   id UUID PRIMARY KEY,
   titulo VARCHAR(255) NOT NULL,
   descricao VARCHAR(1000),
   status VARCHAR(50) NOT NULL,
   data_criacao TIMESTAMP NOT NULL,
   data_conclusao TIMESTAMP NULL,
   tarefa_id UUID NOT NULL
);

