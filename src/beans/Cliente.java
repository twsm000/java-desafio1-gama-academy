package beans;

import util.TrataException;

public class Cliente {
    private static final int CPF_LENGTH = 11;
    private Integer id;
    private String nome;
    private String cpf;
    private String telefone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void validate() throws TrataException {
        TrataException.raiseExceptionIsNullOrBlank(nome, "Nome do cliente não informado.");
        TrataException.raiseExceptionIsNullOrBlank(cpf, "CPF do cliente não informado.");
        TrataException.raiseExceptionIsNullOrBlank(telefone, "Telefone do cliente não informado.");

        if (cpf.length() != CPF_LENGTH) {
            throw new TrataException("CPF com tamanho inválido.");
        }
        try  {
            //noinspection unused - only to validate if is a unsigned integer
            Long.parseUnsignedLong(cpf);
        } catch (NumberFormatException e) {
            throw new TrataException("CPF inválido.");
        }
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}
