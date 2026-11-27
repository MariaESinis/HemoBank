from database.conexao import conectar

conexao = conectar()

try:
    cursor = conexao.cursor()

    
    cursor.execute("""
        SELECT COUNT(*)
        FROM hr.employee;
    """)

    total = cursor.fetchone()[0]
    print("Total na tabela employee:", total)

    
    cursor.execute("""
        SELECT COUNT(*)
        FROM hr.employee
        WHERE emp_deleted_at IS NULL;
    """)

    ativos = cursor.fetchone()[0]
    print("Não excluídos:", ativos)

    
    cursor.execute("""
        SELECT COUNT(*)
        FROM hr.fn_list_employees();
    """)

    resultado_funcao = cursor.fetchone()[0]
    print("Retornados pela função:", resultado_funcao)

finally:
    conexao.close()