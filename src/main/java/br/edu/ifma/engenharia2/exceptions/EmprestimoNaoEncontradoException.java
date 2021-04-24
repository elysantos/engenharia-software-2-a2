package br.edu.ifma.engenharia2.exceptions;

public class EmprestimoNaoEncontradoException  extends Exception{
    public EmprestimoNaoEncontradoException() {
        super("Empréstimo não encontrado");
    }
}
