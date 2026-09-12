package com.atlas.bank.atlas.shared.exception;

import com.atlas.bank.atlas.account.exeption.AccountNotFoundException;
import com.atlas.bank.atlas.transaction.exeption.AccountNotActiveException;
import com.atlas.bank.atlas.transaction.exeption.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice => Clase que captura las excepciones del sistema de forma centralizada
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFound(AccountNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("Cuenta no encontrada");

        return problemDetail;
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ProblemDetail handleInsufficientFunds(InsufficientFundsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(422),
                ex.getMessage()
        );
        problemDetail.setTitle("Fondos insuficientes");

        return problemDetail;
    }

    @ExceptionHandler(AccountNotActiveException.class)
    public ProblemDetail handleInsufficientFunds(AccountNotActiveException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage()
        );
        problemDetail.setTitle("Cuenta no activa");

        return problemDetail;
    }
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleInsufficientFunds(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(422),
                "Error interno del servidor"
        );
        problemDetail.setTitle("Error interno");

        return problemDetail;
    }
}
