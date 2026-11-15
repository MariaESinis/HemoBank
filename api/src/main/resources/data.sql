-- ====================================================================
-- SENHAS CRIPTOGRAFADAS COM BCRYPT (Senha para todos: 123456)
-- Hash Bcrypt: $2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.
-- ====================================================================

-- 1. INSERINDO USUÁRIOS DE SISTEMA (Tabela: usuario)
INSERT INTO usuario (usr_nome, usr_cpf, usr_email, usr_senha, usr_telefone, usr_tipo, role) VALUES
('Dr. Carlos Andrade', '11122233344', 'medico@hemoglobank.com', '$2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.', '(11) 98888-1111', 'MEDICO', 'ROLE_MEDICO'),
('Dra. Ana Souza', '22233344455', 'medico2@hemoglobank.com', '$2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.', '(11) 98888-2222', 'MEDICO', 'ROLE_MEDICO'),
('Fernanda Gestora', '33344455566', 'gestor@hemoglobank.com', '$2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.', '(11) 97777-3333', 'GESTOR', 'ROLE_GESTOR'),
('Lucas Silva (Doador)', '44455566677', 'lucas@gmail.com', '$2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.', '(11) 96666-4444', 'DOADOR', 'ROLE_DOADOR'),
('Mariana Oliveira (Doador)', '55566677788', 'mariana@gmail.com', '$2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.', '(11) 96666-5555', 'DOADOR', 'ROLE_DOADOR'),
('Roberto Santos (Doador)', '66677788899', 'roberto@gmail.com', '$2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.', '(11) 96666-6666', 'DOADOR', 'ROLE_DOADOR');
-- 2. INSERINDO DOADORES VINCULADOS AOS USUÁRIOS (Tabela: doador)
INSERT INTO doador (dor_nome, dor_cpf, dor_data_nascimento, dor_sexo, dor_telefone, dor_email, usr_id, dor_created_at, dor_updated_at) VALUES
('Lucas Silva', '44455566677', '1995-03-15', 'M', '(11) 96666-4444', 'lucas@gmail.com', 4, NOW(), NOW()),
('Mariana Oliveira', '55566677788', '1998-07-22', 'F', '(11) 96666-5555', 'mariana@gmail.com', 5, NOW(), NOW()),
('Roberto Santos', '66677788899', '1990-11-05', 'M', '(11) 96666-6666', 'roberto@gmail.com', 6, NOW(), NOW());

-- 3. INSERINDO DADOS NO ESTOQUE (Tabela: estoque com as colunas reais da entidade)
INSERT INTO estoque (est_tipagem, est_tipo, est_status, est_quantidade, est_created_at, est_updated_at) VALUES
('O+', 'SANGUE_TOTAL', 'DISPONIVEL', 25, NOW(), NOW()),
('O-', 'SANGUE_TOTAL', 'CRITICO', 5, NOW(), NOW()),
('A+', 'PLAQUETAS', 'DISPONIVEL', 18, NOW(), NOW()),
('A-', 'PLASMA', 'MODERADO', 8, NOW(), NOW()),
('B+', 'SANGUE_TOTAL', 'DISPONIVEL', 12, NOW(), NOW()),
('AB+', 'SANGUE_TOTAL', 'DISPONIVEL', 10, NOW(), NOW()),
('AB-', 'PLAQUETAS', 'CRITICO', 2, NOW(), NOW());
-- 4. INSERINDO AGENDAMENTOS (Tabela: agendamento com o padrão correto)
INSERT INTO agendamento (dor_id, age_data, age_horario, age_observacoes) VALUES
(1, '2026-11-10', '2026-11-10 09:00:00', 'Primeira doação de sangue'),
(2, '2026-11-10', '2026-11-10 10:30:00', 'Coleta realizada com sucesso. Tipo O+'),
(3, '2026-11-11', '2026-11-11 14:00:00', 'Doador relatou sintomas gripais');