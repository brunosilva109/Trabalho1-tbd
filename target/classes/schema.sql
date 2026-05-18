CREATE TABLE IF NOT EXISTS livro (
    id              SERIAL PRIMARY KEY,
    titulo          VARCHAR(255) NOT NULL,
    autor           VARCHAR(255) NOT NULL,
    ano_publicacao  INTEGER NOT NULL,
    categoria       VARCHAR(100) NOT NULL
);
