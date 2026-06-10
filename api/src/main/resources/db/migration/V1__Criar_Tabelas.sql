CREATE TABLE `usuario` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `cpf` varchar(14) UNIQUE NOT NULL,
  `nome` varchar(150) NOT NULL,
  `data_nascimento` date,
  `email` varchar(150) UNIQUE NOT NULL,
  `telefone` varchar(20),
  `senha` varchar(255) NOT NULL,
  `genero` varchar(30),
  `pontos_fidelidade` int DEFAULT 0
);

CREATE TABLE `consentimento_lgpd` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `tipo_consentimento` varchar(150) NOT NULL,
  `versao_consentimento` varchar(30),
  `aceito` boolean NOT NULL,
  `data_acao` datetime,
  `observacoes` varchar(255)
);

CREATE TABLE `perfil` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `descricao` varchar(255)
);

CREATE TABLE `perfil_usuario` (
  `usuario_id` int,
  `perfil_id` int,
  PRIMARY KEY (`usuario_id`, `perfil_id`)
);

CREATE TABLE `unidade` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `endereco_id` int UNIQUE NOT NULL,
  `regiao_id` int NOT NULL,
  `nome` varchar(150) NOT NULL,
  `cnpj` varchar(18) UNIQUE,
  `telefone` varchar(20)
);

CREATE TABLE `cardapio` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `unidade_id` int NOT NULL,
  `nome` varchar(150) NOT NULL,
  `descricao` varchar(255),
  `ativo` boolean DEFAULT true
);

CREATE TABLE `produto` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nome` varchar(150) NOT NULL,
  `descricao` varchar(255),
  `categoria` varchar(150),
  `preco_base` decimal(10,2) NOT NULL,
  `exige_preparo` boolean DEFAULT false,
  `ativo` boolean DEFAULT true
);

CREATE TABLE `produto_cardapio` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `cardapio_id` int NOT NULL,
  `produto_id` int NOT NULL,
  `disponivel` boolean DEFAULT true
);

CREATE TABLE `pedido` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `unidade_id` int NOT NULL,
  `data_pedido` datetime NOT NULL,
  `status_pedido` varchar(50),
  `canal_origem` varchar(50),
  `forma_entrega` varchar(50),
  `valor_entrega` decimal(10,2),
  `prazo_estimado` int,
  `desconto` decimal(10,2),
  `valor_total` decimal(10,2),
  `observacao` varchar(255)
);

CREATE TABLE `pagamento` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `pedido_id` int NOT NULL,
  `metodo_pagamento` varchar(50),
  `status_pagamento` varchar(50),
  `valor` decimal(10,2),
  `data_pagamento` datetime
);

CREATE TABLE `produto_pedido` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `pedido_id` int NOT NULL,
  `produto_id` int NOT NULL,
  `quantidade` int NOT NULL,
  `subtotal` decimal(10,2),
  `status_produto` varchar(50)
);

CREATE TABLE `ingrediente` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nome` varchar(150) NOT NULL,
  `unidade_medida` varchar(20)
);

CREATE TABLE `ingrediente_produto` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `ingrediente_id` int NOT NULL,
  `produto_id` int NOT NULL,
  `quantidade` decimal(10,3)
);

CREATE TABLE `estoque_produto` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `unidade_id` int NOT NULL,
  `produto_id` int NOT NULL,
  `quantidade_atual` int CHECK (quantidade_atual >= 0),
  `quantidade_minima` int
);

CREATE TABLE `estoque_ingrediente` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `unidade_id` int NOT NULL,
  `ingrediente_id` int NOT NULL,
  `quantidade_atual` decimal(10,3) CHECK (quantidade_atual >= 0),
  `quantidade_minima` decimal(10,3)
);

CREATE TABLE `endereco` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `usuario_id` int,
  `cep` varchar(10) NOT NULL,
  `logradouro` varchar(150),
  `numero` varchar(10),
  `complemento` varchar(100),
  `bairro` varchar(100),
  `cidade` varchar(100),
  `estado` char(2),
  `principal` boolean DEFAULT true
);

CREATE TABLE `regiao_comercial` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nome` varchar(100) UNIQUE NOT NULL
);

CREATE UNIQUE INDEX `produto_cardapio_index_0` ON `produto_cardapio` (`cardapio_id`, `produto_id`);

CREATE UNIQUE INDEX `produto_pedido_index_1` ON `produto_pedido` (`pedido_id`, `produto_id`);

CREATE UNIQUE INDEX `ingrediente_produto_index_2` ON `ingrediente_produto` (`ingrediente_id`, `produto_id`);

CREATE UNIQUE INDEX `estoque_produto_index_3` ON `estoque_produto` (`unidade_id`, `produto_id`);

CREATE UNIQUE INDEX `estoque_ingrediente_index_4` ON `estoque_ingrediente` (`unidade_id`, `ingrediente_id`);

ALTER TABLE `consentimento_lgpd` ADD FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

ALTER TABLE `perfil_usuario` ADD FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

ALTER TABLE `perfil_usuario` ADD FOREIGN KEY (`perfil_id`) REFERENCES `perfil` (`id`);

ALTER TABLE `unidade` ADD FOREIGN KEY (`endereco_id`) REFERENCES `endereco` (`id`);

ALTER TABLE `unidade` ADD FOREIGN KEY (`regiao_id`) REFERENCES `regiao_comercial` (`id`);

ALTER TABLE `cardapio` ADD FOREIGN KEY (`unidade_id`) REFERENCES `unidade` (`id`);

ALTER TABLE `produto_cardapio` ADD FOREIGN KEY (`cardapio_id`) REFERENCES `cardapio` (`id`);

ALTER TABLE `produto_cardapio` ADD FOREIGN KEY (`produto_id`) REFERENCES `produto` (`id`);

ALTER TABLE `pedido` ADD FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

ALTER TABLE `pedido` ADD FOREIGN KEY (`unidade_id`) REFERENCES `unidade` (`id`);

ALTER TABLE `pagamento` ADD FOREIGN KEY (`pedido_id`) REFERENCES `pedido` (`id`);

ALTER TABLE `produto_pedido` ADD FOREIGN KEY (`pedido_id`) REFERENCES `pedido` (`id`);

ALTER TABLE `produto_pedido` ADD FOREIGN KEY (`produto_id`) REFERENCES `produto` (`id`);

ALTER TABLE `ingrediente_produto` ADD FOREIGN KEY (`ingrediente_id`) REFERENCES `ingrediente` (`id`);

ALTER TABLE `ingrediente_produto` ADD FOREIGN KEY (`produto_id`) REFERENCES `produto` (`id`);

ALTER TABLE `estoque_produto` ADD FOREIGN KEY (`unidade_id`) REFERENCES `unidade` (`id`);

ALTER TABLE `estoque_produto` ADD FOREIGN KEY (`produto_id`) REFERENCES `produto` (`id`);

ALTER TABLE `estoque_ingrediente` ADD FOREIGN KEY (`unidade_id`) REFERENCES `unidade` (`id`);

ALTER TABLE `estoque_ingrediente` ADD FOREIGN KEY (`ingrediente_id`) REFERENCES `ingrediente` (`id`);

ALTER TABLE `endereco` ADD FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);