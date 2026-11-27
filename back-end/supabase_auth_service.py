import os
import httpx
from urllib.parse import quote

from pathlib import Path
from dotenv import load_dotenv

BASE_DIR = Path(__file__).resolve().parents[1]
load_dotenv(BASE_DIR / ".env")

BASE_DIR = Path(__file__).resolve().parents[1]
load_dotenv(BASE_DIR / ".env")


def _config():
    url = os.getenv("SUPABASE_URL", "").rstrip("/")
    anon_key = os.getenv("SUPABASE_ANON_KEY", "")
    service_key = os.getenv("SUPABASE_SERVICE_ROLE_KEY", "")
    if not url:
        raise RuntimeError("SUPABASE_URL não configurada no .env.")
    return url, anon_key, service_key


def criar_usuario(email: str, senha: str) -> str:
    url, _, service_key = _config()
    if not service_key:
        raise RuntimeError("SUPABASE_SERVICE_ROLE_KEY não configurada no .env.")

    resposta = httpx.post(
        f"{url}/auth/v1/admin/users",
        headers={
            "apikey": service_key,
            "Authorization": f"Bearer {service_key}",
            "Content-Type": "application/json",
        },
        json={"email": str(email), "password": senha, "email_confirm": True},
        timeout=20.0,
    )
    if resposta.is_error:
        detalhe = resposta.json().get("msg") or resposta.json().get("message") or "Não foi possível criar o usuário."
        raise ValueError(detalhe)
    return resposta.json()["id"]


def excluir_usuario(auth_id: str) -> None:
    url, _, service_key = _config()
    if not service_key:
        raise RuntimeError("SUPABASE_SERVICE_ROLE_KEY não configurada no .env.")

    resposta = httpx.delete(
        f"{url}/auth/v1/admin/users/{quote(auth_id, safe='')}",
        headers={
            "apikey": service_key,
            "Authorization": f"Bearer {service_key}",
        },
        timeout=20.0,
    )
    if resposta.is_error:
        raise RuntimeError("Não foi possível desfazer a criação do usuário no Supabase Auth.")


def autenticar_usuario(email: str, senha: str) -> dict:
    url, anon_key, _ = _config()
    if not anon_key:
        raise RuntimeError("SUPABASE_ANON_KEY não configurada no .env.")

    resposta = httpx.post(
        f"{url}/auth/v1/token?grant_type=password",
        headers={"apikey": anon_key, "Content-Type": "application/json"},
        json={"email": str(email), "password": senha},
        timeout=20.0,
    )
    if resposta.is_error:
        raise ValueError("E-mail ou senha inválidos.")
    return resposta.json()


def obter_usuario_por_token(token: str) -> dict:
    url, anon_key, _ = _config()
    if not anon_key:
        raise RuntimeError("SUPABASE_ANON_KEY não configurada no .env.")

    resposta = httpx.get(
        f"{url}/auth/v1/user",
        headers={
            "apikey": anon_key,
            "Authorization": f"Bearer {token}",
        },
        timeout=20.0,
    )
    if resposta.is_error:
        raise ValueError("Token inválido ou expirado.")
    return resposta.json()
