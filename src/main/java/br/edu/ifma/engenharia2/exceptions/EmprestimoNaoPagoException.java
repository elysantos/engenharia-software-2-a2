package br.edu.ifma.engenharia2.exceptions;

public class EmprestimoNaoPagoException extends Exception{
    public EmprestimoNaoPagoException() {
        super("Livro não pode ser devolvido sem pagamento");
    }
}
