package br.edu.ifma.engenharia2.exceptions;

public class LivroReservadoException extends Exception{
    public LivroReservadoException() {
        super("Livro está reservado para outro usuário");
    }
}
