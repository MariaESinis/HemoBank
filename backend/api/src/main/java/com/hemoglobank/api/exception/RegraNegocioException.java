package com.hemoglobank.api.exception;

/** Erro previsto de regra de negócio; a mensagem é segura para exibir ao usuário. */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
