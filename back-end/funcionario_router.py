from fastapi import APIRouter, HTTPException, Depends
from psycopg import Error as PsycopgError

from services.funcionario_service import listar_funcionarios, cadastrar_funcionario
from schemas.funcionario_schema import FuncionarioCadastro
from security import administrador_atual

router = APIRouter(prefix="/funcionarios", tags=["Funcionários"])


@router.get("")
def listar(_administrador=Depends(administrador_atual)):
    try:
        funcionarios = listar_funcionarios()
        return {"total": len(funcionarios), "funcionarios": funcionarios}
    except PsycopgError:
        raise HTTPException(
            status_code=503,
            detail="Não foi possível consultar o banco de dados."
        )


@router.post("", status_code=201)
def cadastrar(
    funcionario: FuncionarioCadastro,
    _administrador=Depends(administrador_atual),
):
    try:
        resultado = cadastrar_funcionario(funcionario)
        return {
            "mensagem": "Funcionário cadastrado com sucesso!",
            **resultado,
        }
    except ValueError as erro:
        raise HTTPException(status_code=422, detail=str(erro))
    except RuntimeError as erro:
        raise HTTPException(status_code=503, detail=str(erro))
    except PsycopgError as erro:
        if erro.sqlstate == "23505":
            raise HTTPException(
                status_code=409,
                detail="CPF, e-mail ou login já cadastrado."
            )
        if erro.sqlstate in ("23503", "23514"):
            raise HTTPException(
                status_code=422,
                detail="Dados inválidos ou referência inexistente."
            )
        raise HTTPException(
            status_code=500,
            detail="Erro ao realizar o cadastro."
        )
