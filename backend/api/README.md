# HemogloBank

Sistema de gestão de doação de sangue (Spring Boot + Thymeleaf + Spring Security + JPA).

## Como rodar

```bash
./mvnw spring-boot:run        # Linux/macOS
mvnw.cmd spring-boot:run      # Windows
```

Acesse http://localhost:8080. O banco é um H2 em memória, recriado e populado (`data.sql`) a cada start.
Console do H2: http://localhost:8080/h2-console (somente logado como **gestor**;
JDBC URL `jdbc:h2:mem:hemoglobankdb`, usuário `sa`, senha `123`).

### Usuários de demonstração (senha de todos: `123456`)

| Perfil  | E-mail                     | Vai para           |
|---------|----------------------------|--------------------|
| Doador  | lucas@gmail.com            | `/doador/painel`   |
| Doador  | mariana@gmail.com          | `/doador/painel` (ainda inapta: doou há 30 dias) |
| Médico  | medico@hemoglobank.com     | `/medico/painel`   |
| Gestor  | gestor@hemoglobank.com     | `/gestor/painel`   |

## Perfis e permissões

| Rota                                   | Quem acessa |
|----------------------------------------|-------------|
| `/`, `/login`, `/cadastro`, `/api/cadastro/**`, `/api/estoque` | Público |
| `/doador/**`                           | DOADOR |
| `/medico/**`, `/api/bolsas/**`, `/api/doacoes/**` | MEDICO |
| `/gestor/**`, `/usuarios/**`, `/h2-console/**`    | GESTOR |
| `/api/relatorios/**` (views `reporting.*`) | GESTOR, MEDICO |
| `/api/funcionarios/**` (funções `hr.*`)      | GESTOR |

O perfil vem do campo `usr_tipo` (`DOADOR`, `MEDICO`, `GESTOR`) da tabela `usuario`.

## Regras de negócio

- **Intervalo entre doações:** 60 dias (homens) e 90 dias (mulheres). A data da última doação é atualizada
  quando o médico cadastra uma bolsa ou marca um agendamento como *Concluído*.
- **Agendamento:** data/horário futuros, respeitando o intervalo acima; um agendamento em aberto por doador;
  o doador só cancela os próprios agendamentos.
- **Cadastro:** idade entre 16 e 69 anos, e-mail e CPF únicos, CPF com 11 dígitos, senha de 6 a 72 caracteres.
- **Estoque:** calculado a partir das **bolsas** disponíveis e dentro da validade
  (menos de 3 = crítico, 3 a 9 = estável, 10 ou mais = abastecido).

## Produção

```bash
export DB_URL=jdbc:postgresql://host:5432/hemoglobank DB_USER=... DB_PASSWORD=...
java -jar target/api-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

O perfil `prod` desliga o console do H2 e **não carrega o `data.sql`** (que contém usuários com senha conhecida).

## Testes

```bash
./mvnw test
```

## Consultas JDBC (schemas `hr` e `reporting`)

Usam `DataSource` + `PreparedStatement` (try-with-resources) e **só funcionam com o banco do Supabase**
(perfil `prod`); no H2 de desenvolvimento esses schemas não existem e a página mostra "não foi possível carregar".

| Endpoint | Objeto no banco |
|----------|-----------------|
| `GET /api/funcionarios/por-email?email=` | `hr.fn_get_employee_by_email` |
| `GET /api/funcionarios/por-auth-id/{uuid}` | `hr.fn_get_employee_by_auth_id` |
| `GET /api/relatorios/doacoes?limite=200` | `reporting.vw_donation_detail` |
| `GET /api/relatorios/bolsas-a-vencer` | `reporting.vw_bags_expiring_soon` |

Página: `/gestor/relatorios`.
