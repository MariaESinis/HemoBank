# ============ 1. IMPORTS ============
import os
import re
from datetime import date, time

from flask import (Flask, render_template, request, redirect,
                   url_for, flash, jsonify)
from werkzeug.security import generate_password_hash, check_password_hash

# ============ 2. CONFIGURAÇÃO ============
app = Flask(__name__)
app.secret_key = os.environ.get("SECRET_KEY", "troque-isto-em-producao")

# ============ 3. CONSTANTES (o que eram os enums do Java) ============
TIPAGENS = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"]

COMPONENTES = [
    {"valor": "SANGUE_TOTAL", "rotulo": "Sangue total"},
    {"valor": "PLASMA", "rotulo": "Plasma"},
    {"valor": "PLAQUETAS", "rotulo": "Plaquetas"},
    {"valor": "HEMACIAS", "rotulo": "Hemácias"},
]

STATUS_AGENDAMENTO = [
    {"valor": "PENDENTE", "rotulo": "Pendente", "css": "pendente"},
    {"valor": "CONFIRMADO", "rotulo": "Confirmado", "css": "confirmado"},
    {"valor": "CONCLUIDO", "rotulo": "Concluído", "css": "concluido"},
    {"valor": "CANCELADO", "rotulo": "Cancelado", "css": "cancelado"},
]

STATUS_EXAME = [
    {"valor": "SOLICITADO", "rotulo": "Solicitado", "css": "pendente"},
    {"valor": "CONCLUIDO", "rotulo": "Concluído", "css": "concluido"},
]

TIPOS_EXAME = ["Hemograma Completo", "Sorologia", "Tipagem Sanguínea"]

# ============ 4. FUNÇÕES AUXILIARES ============
def calcular_nivel(bolsas):
    if bolsas < 3:
        return {"css": "critico", "rotulo": "Crítico"}
    if bolsas < 10:
        return {"css": "estavel", "rotulo": "Estável"}
    return {"css": "abastecido", "rotulo": "Abastecido"}


def buscar_niveis_estoque():
    # provisório: depois vem do banco
    estoque = {"A+": 12, "A-": 2, "B+": 7, "B-": 1,
               "AB+": 4, "AB-": 0, "O+": 15, "O-": 3}
    return [{"tipagem": t, "bolsas": q, "nivel": calcular_nivel(q)}
            for t, q in estoque.items()]

# ============ 5. ROTAS ============
# ---- Públicas ----
@app.route("/")
def index():
    return render_template("index.html", niveis_estoque=buscar_niveis_estoque())

@app.route("/login", methods=["GET", "POST"])
def login(): ...

@app.route("/logout", methods=["POST"])
def logout(): ...

@app.route("/cadastro")
def cadastro():
    return render_template("cadastro.html")

@app.route("/api/cadastro/doador", methods=["POST"])
def api_cadastro_doador(): ...

# ---- Doador ----
@app.route("/doador/painel")
def painel_doador(): ...

@app.route("/doador/agendamento", methods=["GET",