from fastapi import APIRouter, Depends, HTTPException
from psycopg import Error as PsycopgError

from security import administrador_atual
from services.relatorios_service import (
    listar_estoque_disponivel,
    listar_ultima_doacao,
)

router = APIRouter(prefix="/relatorios", tags=["Relatórios e consultas"])


def _resposta(dados):
    return {"total": len(dados), "registros": dados}


@router.get("/estoque-disponivel")
def estoque_disponivel(_administrador=Depends(administrador_atual)):
    try:
        return _resposta(listar_estoque_disponivel())
    except PsycopgError:
        raise HTTPException(
            status_code=503,
            detail="Não foi possível consultar o estoque disponível. Verifique a view e a permissão SELECT.",
        )


@router.get("/ultima-doacao")
def ultima_doacao(_administrador=Depends(administrador_atual)):
    try:
        return _resposta(listar_ultima_doacao())
    except PsycopgError:
        raise HTTPException(
            status_code=503,
            detail="Não foi possível consultar a última doação. Verifique a view e a permissão SELECT.",
        )
