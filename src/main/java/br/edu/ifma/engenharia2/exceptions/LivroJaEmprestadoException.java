package br.edu.ifma.engenharia2.exceptions;

public class LivroJaEmprestadoException extends Exception{
    public LivroJaEmprestadoException() {
        super("Livro já está emprestado");
    }
}
