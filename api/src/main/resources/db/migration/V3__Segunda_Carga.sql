INSERT INTO endereco (cep, logradouro, numero, bairro, cidade, estado, principal) VALUES 
('45600-000', 'Rua Principal', '50', 'Santo Antônio', 'Itabuna', 'BA', true);

INSERT INTO unidade (endereco_id, regiao_id, nome, cnpj) VALUES 
(17, 1, 'Unidade Itabuna Santo Antônio', '12.345.678/0002-99');

INSERT INTO cardapio (unidade_id, nome, ativo) VALUES 
(2, 'Cardápio Principal', true);

INSERT INTO produto_cardapio (cardapio_id, produto_id, disponivel) VALUES 
(2, 1, true),
(2, 3, true),
(2, 4, true);

INSERT INTO estoque_produto (unidade_id, produto_id, quantidade_atual, quantidade_minima) VALUES 
(2, 1, 10, 10);

INSERT INTO estoque_ingrediente (unidade_id, ingrediente_id, quantidade_atual, quantidade_minima) VALUES 
(2, 1, 200, 40),
(2, 2, 100, 30),
(2, 3, 10, 60),
(2, 4, 250, 50),
(2, 5, 100, 30);