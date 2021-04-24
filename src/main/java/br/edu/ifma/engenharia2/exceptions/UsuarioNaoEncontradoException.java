package br.edu.ifma.engenharia2.exceptions;

public class UsuarioNaoEncontradoException extends Exception{
    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado");
    }
}
