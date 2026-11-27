-- ====================================================================
-- DADOS DE DEMONSTRAÇÃO (somente desenvolvimento / H2)
-- Senha de TODOS os usuários: 123456
-- Hash BCrypt: $2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.
-- Datas relativas a "hoje" para a demo nunca ficar defasada.
-- No perfil "prod" este arquivo não é executado (spring.sql.init.mode=never).
-- ====================================================================

-- 1. USUÁRIOS (perfil derivado de usr_tipo: DOADOR, MEDICO, GESTOR)
INSERT INTO usuario (usr_nome, usr_cpf, usr_email, usr_senha, usr_telefone, usr_tipo) VALUES
('Dr. Carlos Andrade', '11122233344', 'medico@hemoglobank.com', '$2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.', '(11) 98888-1111', 'MEDICO'),
('Dra. Ana Souza', '22233344455', 'medico2@hemoglobank.com', '$2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.', '(11) 98888-2222', 'MEDICO'),
('Fernanda Gestora', '33344455566', 'gestor@hemoglobank.com', '$2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.', '(11) 97777-3333', 'GESTOR'),
('Lucas Silva', '44455566677', 'lucas@gmail.com', '$2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.', '(11) 96666-4444', 'DOADOR'),
('Mariana Oliveira', '55566677788', 'mariana@gmail.com', '$2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.', '(11) 96666-5555', 'DOADOR'),
('Roberto Santos', '66677788899', 'roberto@gmail.com', '$2a$10$iqWVsu2Rz/HNyxgpsW.EEOuoXtg6Cj5ALoZ9Bx6AYb/Ot8Hl/g02.', '(11) 96666-6666', 'DOADOR');

-- 2. DOADORES (vinculados aos usuários 4, 5 e 6)
INSERT INTO doador (dor_nome, dor_cpf, dor_data_nascimento, dor_sexo, dor_telefone, dor_email, dor_data_ultima_doacao, usr_id, dor_created_at, dor_updated_at) VALUES
('Lucas Silva', '44455566677', '1995-03-15', 'M', '(11) 96666-4444', 'lucas@gmail.com', NULL, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mariana Oliveira', '55566677788', '1998-07-22', 'F', '(11) 96666-5555', 'mariana@gmail.com', DATEADD(DAY, -30, CURRENT_DATE), 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Roberto Santos', '66677788899', '1990-11-05', 'M', '(11) 96666-6666', 'roberto@gmail.com', DATEADD(DAY, -90, CURRENT_DATE), 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3. BOLSAS (o estoque exibido na página inicial é calculado a partir daqui)
-- Limites: menos de 3 = CRÍTICO, menos de 10 = ESTÁVEL, 10 ou mais = ABASTECIDO
INSERT INTO bolsa (bol_tipagem, bol_componente, bol_quantidade, bol_status, bol_validade, dor_id) VALUES
('O+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 20, CURRENT_DATE), 1),
('O+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 24, CURRENT_DATE), 2),
('O+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 28, CURRENT_DATE), 3),
('O+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 32, CURRENT_DATE), 1),
('O+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 36, CURRENT_DATE), 2),
('O+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 40, CURRENT_DATE), 3),
('O+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 44, CURRENT_DATE), 1),
('O+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 48, CURRENT_DATE), 2),
('O+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 52, CURRENT_DATE), 3),
('O+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 56, CURRENT_DATE), 1),
('O-', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 20, CURRENT_DATE), 2),
('A+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 20, CURRENT_DATE), 3),
('A+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 24, CURRENT_DATE), 1),
('A+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 28, CURRENT_DATE), 2),
('A+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 32, CURRENT_DATE), 3),
('A+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 36, CURRENT_DATE), 1),
('A+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 40, CURRENT_DATE), 2),
('A-', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 20, CURRENT_DATE), 3),
('A-', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 24, CURRENT_DATE), 1),
('B+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 20, CURRENT_DATE), 2),
('B+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 24, CURRENT_DATE), 3),
('B+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 28, CURRENT_DATE), 1),
('B+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 32, CURRENT_DATE), 2),
('AB+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 20, CURRENT_DATE), 3),
('AB+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 24, CURRENT_DATE), 1),
('AB+', 'SANGUE_TOTAL', 450, 'DISPONIVEL', DATEADD(DAY, 28, CURRENT_DATE), 2),
('A+', 'PLAQUETAS', 250, 'DISPONIVEL', DATEADD(DAY, 3, CURRENT_DATE), 1),
('O+', 'PLASMA', 200, 'DISPONIVEL', DATEADD(DAY, -2, CURRENT_DATE), 2),
('B-', 'SANGUE_TOTAL', 450, 'UTILIZADA', DATEADD(DAY, 15, CURRENT_DATE), 3);

-- 4. AGENDAMENTOS
INSERT INTO agendamento (dor_id, age_data, age_horario, age_observacoes, age_status) VALUES
(1, DATEADD(DAY, 3, CURRENT_DATE), '09:00:00', 'Primeira doação de sangue', 'PENDENTE'),
(2, DATEADD(DAY, -30, CURRENT_DATE), '10:30:00', 'Coleta realizada com sucesso. Tipo O+', 'CONCLUIDO'),
(3, DATEADD(DAY, 5, CURRENT_DATE), '14:00:00', NULL, 'APROVADO');

-- 5. EXAMES
INSERT INTO exame (dor_id, exm_tipo, exm_status, exm_resultado, exm_data_solicitacao) VALUES
(1, 'Hemograma Completo', 'SOLICITADO', NULL, CURRENT_TIMESTAMP),
(2, 'Tipagem Sanguínea', 'CONCLUIDO', 'O positivo', CURRENT_TIMESTAMP);
