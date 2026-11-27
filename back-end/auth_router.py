from fastapi import APIRouter, HTTPException

from schemas.funcionario_schema import LoginRequest
from services.supabase_auth_service import autenticar_usuario
from services.funcionario_service import buscar_funcionario_por_auth_id
from fastapi import Depends
from security import usuario_atual

router = APIRouter(prefix="/auth", tags=["Autenticação"])


@router.post("/login")
def login(dados: LoginRequest):
    try:
        sessao = autenticar_usuario(str(dados.email), dados.senha)
        funcionario = buscar_funcionario_por_auth_id(sessao["user"]["id"])
    except ValueError as erro:
        raise HTTPException(status_code=401, detail=str(erro))
    except RuntimeError as erro:
        raise HTTPException(status_code=503, detail=str(erro))

    if not funcionario:
        raise HTTPException(
            status_code=403,
            detail="Usuário autenticado, mas não possui vínculo com um funcionário do HemoBank."
        )

    if funcionario["emp_ems_id"] != 0:
        raise HTTPException(status_code=403, detail="Funcionário não está ativo.")

    return {
        "mensagem": "Login realizado com sucesso.",
        "access_token": sessao["access_token"],
        "refresh_token": sessao.get("refresh_token"),
        "token_type": sessao.get("token_type", "bearer"),
        "expires_in": sessao.get("expires_in"),
        "funcionario": {
            "id": funcionario["emp_id"],
            "nome": funcionario["emp_name"],
            "nivel_acesso": funcionario["emp_access_level"],
        },
    }


@router.get("/me")
def meus_dados(funcionario=Depends(usuario_atual)):
    return {
        "id": funcionario["emp_id"],
        "nome": funcionario["emp_name"],
        "nivel_acesso": funcionario["emp_access_level"],
    }
