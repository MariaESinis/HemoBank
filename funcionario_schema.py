from datetime import date
from decimal import Decimal
import re

from pydantic import BaseModel, EmailStr, Field, field_validator, model_validator


class FuncionarioCadastro(BaseModel):
    nome: str = Field(min_length=3, max_length=150)
    cpf: str
    cargo: str = Field(min_length=2, max_length=100)
    telefone: str | None = None
    email: EmailStr
    data_contratacao: date
    salario: Decimal = Field(ge=0)
    status_id: int = Field(ge=0)

    # O funcionário pode ser cadastrado sem receber acesso ao sistema.
    criar_acesso: bool = False
    senha: str | None = Field(default=None, min_length=8)
    nivel_acesso: str | None = None

    @field_validator("cpf")
    @classmethod
    def validar_cpf(cls, valor):
        cpf = re.sub(r"\\D", "", valor)

        if len(cpf) != 11 or cpf == cpf[0] * 11:
            raise ValueError("CPF inválido.")

        soma = sum(int(cpf[i]) * (10 - i) for i in range(9))
        primeiro_digito = (soma * 10) % 11
        if primeiro_digito == 10:
            primeiro_digito = 0
        if primeiro_digito != int(cpf[9]):
            raise ValueError("CPF inválido.")

        soma = sum(int(cpf[i]) * (11 - i) for i in range(10))
        segundo_digito = (soma * 10) % 11
        if segundo_digito == 10:
            segundo_digito = 0
        if segundo_digito != int(cpf[10]):
            raise ValueError("CPF inválido.")

        return cpf

    @model_validator(mode="after")
    def validar_dados_de_acesso(self):
        if self.criar_acesso:
            if not self.senha:
                raise ValueError("Informe uma senha para criar o acesso.")
            if not self.nivel_acesso:
                raise ValueError("Informe o nível de acesso do funcionário.")
        elif self.senha or self.nivel_acesso:
            raise ValueError(
                "Senha e nível de acesso só devem ser informados quando criar_acesso for true."
            )
        return self


class LoginRequest(BaseModel):
    email: EmailStr
    senha: str = Field(min_length=1)
