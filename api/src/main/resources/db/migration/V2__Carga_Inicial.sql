INSERT INTO regiao_comercial (nome) VALUES 
('Costa do Cacau - Bahia');

INSERT INTO perfil (nome) VALUES 
('CLIENTE'), 
('ATENDENTE'), 
('COZINHA'), 
('GERENTE');

INSERT INTO produto (nome, preco_base, exige_preparo) VALUES 
('Refrigerante Lata', 10.00, false),          	-- ID 1
('Água Mineral com Gás', 5.00, false),      	-- ID 2
('Tapioca com Queijo e Ovo', 12.00, true),   	-- ID 3
('Cuscuz com Carne Seca', 15.00, true);      	-- ID 4

INSERT INTO ingrediente (nome, unidade_medida) VALUES 
('Goma de Tapioca', 'kg'),  -- ID 1
('Queijo', 'kg'),           -- ID 2
('Ovo', 'un'),              -- ID 3
('Flocão de Milho', 'kg'),  -- ID 4
('Carne Seca', 'kg');       -- ID 5

INSERT INTO endereco (cep, logradouro, numero, bairro, cidade, estado, principal) VALUES 
('45600-000', 'Rua Comercial', '100', 'Centro', 'Itabuna', 'BA', true);

INSERT INTO unidade (endereco_id, regiao_id, nome, cnpj) VALUES 
(1, 1, 'Unidade Itabuna Centro', '12.345.678/0001-99');

INSERT INTO cardapio (unidade_id, nome, ativo) VALUES 
(1, 'Cardápio Principal', true);

INSERT INTO produto_cardapio (cardapio_id, produto_id, disponivel) VALUES 
(1, 1, true),
(1, 2, true),
(1, 3, true),
(1, 4, true);

INSERT INTO ingrediente_produto (ingrediente_id, produto_id, quantidade) VALUES 
(1, 3, 0.200), 
(2, 3, 0.100), 
(3, 3, 1.000), 
(4, 4, 0.250), 
(5, 4, 0.100);

INSERT INTO estoque_produto (unidade_id, produto_id, quantidade_atual, quantidade_minima) VALUES 
(1, 1, 100, 10),
(1, 2, 100, 10);

INSERT INTO estoque_ingrediente (unidade_id, ingrediente_id, quantidade_atual, quantidade_minima) VALUES 
(1, 1, 200, 40),
(1, 2, 100, 30),
(1, 3, 300, 60),
(1, 4, 250, 50),
(1, 5, 100, 30);