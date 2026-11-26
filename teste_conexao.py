from database.conexao import conectar

try:
    conexao = conectar()

    cursor = conexao.cursor()

    cursor.execute("SELECT current_database(), current_user;")

    resultado = cursor.fetchone()

    print("Conexão realizada com sucesso!")
    print("Banco:", resultado[0])
    print("Usuário:", resultado[1])

    cursor.close()
    conexao.close()

except Exception as erro:
    print("Falha na conexão:")
    print(erro)