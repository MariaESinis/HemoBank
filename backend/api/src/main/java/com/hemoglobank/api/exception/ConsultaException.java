package com.hemoglobank.api.exception;

/** Erro de acesso ao banco (envolve a SQLException para os services não declararem throws). */
public class ConsultaException extends RuntimeException {
    public ConsultaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
