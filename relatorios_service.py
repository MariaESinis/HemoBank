from database.conexao import conectar
from psycopg.rows import dict_row


def _consultar_view(nome_view: str):
    # Os nomes são constantes internas, nunca recebidos pela requisição.
    conexao = conectar()
    try:
        with conexao.cursor(row_factory=dict_row) as cursor:
            cursor.execute(f"SELECT * FROM {nome_view};")
            return cursor.fetchall()
    finally:
        conexao.close()


def listar_estoque_disponivel():
    return _consultar_view("reporting.vw_stock_available")


def listar_ultima_doacao():
    return _consultar_view("reporting.vw_donor_last_donation")
