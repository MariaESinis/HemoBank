import os

from fastapi import Depends, HTTPException
from fastapi.security import HTTPAuthorizationCredentials, HTTPBearer

from services.funcionario_service import buscar_funcionario_por_auth_id
from services.supabase_auth_service import obter_usuario_por_token

bearer = HTTPBearer(auto_error=False)


def usuario_atual(
    credenciais: HTTPAuthorizationCredentials | None = Depends(bearer),
):
    if credenciais is None:
        raise HTTPException(status_code=401, detail="Autenticação necessária.")

    try:
        usuario_auth = obter_usuario_por_token(credenciais.credentials)
        funcionario = buscar_funcionario_por_auth_id(usuario_auth["id"])
    except ValueError as erro:
        raise HTTPException(status_code=401, detail=str(erro))
    except RuntimeError as erro:
        raise HTTPException(status_code=503, detail=str(erro))

    if not funcionario:
        raise HTTPException(
            status_code=403,
            detail="Esta conta não está vinculada a um funcionário do HemoBank."
        )


    if funcionario["emp_ems_id"] != 0:
        raise HTTPException(status_code=403, detail="Funcionário não está ativo.")

    return funcionario


def administrador_atual(funcionario=Depends(usuario_atual)):
    niveis_admin = {
        nivel.strip().lower()
        for nivel in os.getenv(
            "ADMIN_ACCESS_LEVELS",
            "admin,administrator,administrador"
        ).split(",")
        if nivel.strip()
    }
    nivel = (funcionario.get("emp_access_level") or "").strip().lower()

    if nivel not in niveis_admin:
        raise HTTPException(status_code=403, detail="Permissão de administrador necessária.")

    return funcionario
