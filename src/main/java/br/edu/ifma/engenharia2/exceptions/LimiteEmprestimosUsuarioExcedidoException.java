package br.edu.ifma.engenharia2.exceptions;

public class LimiteEmprestimosUsuarioExcedidoException extends Exception {
    public LimiteEmprestimosUsuarioExcedidoException() {
        super("Usuário excede limite de emprestimos");
    }
}
