from database.conexao import conectar
from psycopg.rows import dict_row


def listar_funcionarios():
    conexao = conectar()
    try:
        with conexao.cursor(row_factory=dict_row) as cursor:
            cursor.execute("SELECT * FROM hr.fn_list_employees();")
            return cursor.fetchall()
    finally:
        conexao.close()


def buscar_funcionario_por_auth_id(auth_id: str):
    conexao = conectar()
    try:
        with conexao.cursor(row_factory=dict_row) as cursor:
            cursor.execute(
                "SELECT * FROM hr.fn_get_employee_by_auth_id(%s);",
                (auth_id,)
            )
            return cursor.fetchone()
    finally:
        conexao.close()


def buscar_funcionario_por_email(email: str):
    conexao = conectar()
    try:
        with conexao.cursor(row_factory=dict_row) as cursor:
            cursor.execute(
                "SELECT * FROM hr.fn_get_employee_by_email(%s);",
                (email,)
            )
            return cursor.fetchone()
    finally:
        conexao.close()


def cadastrar_funcionario(funcionario):
    from services.supabase_auth_service import criar_usuario, excluir_usuario

    conexao = conectar()
    auth_id = None

    try:
        # A conta Auth é criada antes do INSERT. Se o banco falhar, removemos
        # a conta Auth para evitar usuário órfão.
        if funcionario.criar_acesso:
            auth_id = criar_usuario(str(funcionario.email), funcionario.senha)

        with conexao.cursor() as cursor:
            cursor.execute(
                """
                CALL hr.sp_register_employee(
                    %s, %s, %s, %s, %s,
                    %s, %s, %s, %s, %s
                );
                """,
                (
                    funcionario.nome,
                    funcionario.cpf,
                    funcionario.cargo,
                    funcionario.telefone,
                    str(funcionario.email),
                    funcionario.data_contratacao,
                    funcionario.salario,
                    funcionario.status_id,
                    auth_id,
                    funcionario.nivel_acesso if funcionario.criar_acesso else None,
                )
            )

            cursor.execute(
                "SELECT emp_id FROM hr.employee WHERE emp_cpf = %s AND emp_deleted_at IS NULL ORDER BY emp_id DESC LIMIT 1;",
                (funcionario.cpf,)
            )
            registro = cursor.fetchone()
            emp_id = registro[0] if registro else None

        conexao.commit()
        return {"emp_id": emp_id, "auth_id": auth_id, "acesso_criado": bool(auth_id)}

    except Exception:
        conexao.rollback()
        if auth_id:
            try:
                excluir_usuario(auth_id)
            except Exception:
                # A falha de compensação deve ser investigada nos logs do servidor.
                pass
        raise
    finally:
        conexao.close()
