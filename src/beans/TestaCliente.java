package beans;

import dao.ClientesDAO;
import dao.ClientesDB;
import dao.ConnectionManager;
import util.Teclado;
import util.TrataException;

import java.util.List;

public class TestaCliente {
    private static ConnectionManager manager;
    private static Teclado teclado;
    private static ClientesDAO dao;
    private static final String MSG_NAME = "Informe o nome:";
    private static final String MSG_CPF = "Informe o cpf:";
    private static final String MSG_TELF = "Informe o telefone:";

    public static void main(String[] args) throws Exception {
        try {
            carregarDependencias();
            inicializar();
        } catch (TrataException e) {
            e.printStackTrace();
        } finally {
            teclado.close();
            manager.close();
        }
    }

    private static void carregarDependencias() throws TrataException {
        manager = ConnectionManager.getInstance();
        teclado = Teclado.getInstance();
        dao = new ClientesDB(manager);
    }

    private static void inicializar() {
        for (int opt = getMenuOption(); opt != 0; opt = getMenuOption()) {
            switch (opt) {
                case 1:
                    inserirCliente();
                    break;
                case 2:
                    excluirClientePorCPF();
                    break;
                case 3:
                    listarTodosClientes();
                    break;
                case 4:
                    filtrarClientePorCPF();
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }

            System.out.println("Pressione qualquer tecla para continuar!");
            teclado.ler();
        }
    }

    private static int getMenuOption() {
        exibirMenu();
        try {
            return Integer.parseInt(teclado.ler());
        } catch (NumberFormatException e) {
            System.err.println("Opção inválida informada.");
            return getMenuOption();
        }
    }

    private static void exibirMenu() {
        System.out.println("-------------------- MENU --------------------");
        System.out.println("1: Inserir novo cliente");
        System.out.println("2: Excluir um cliente específico");
        System.out.println("3: Listar todos os clientes");
        System.out.println("4: Localizar o cliente por CPF");
        System.out.println("0: Sair");
        System.out.println();
    }

    private static void inserirCliente() {
        System.out.println("-------------------- INSERINDO NOVO CLIENTE --------------------");

        Cliente c = new Cliente();
        c.setNome(getUserString(MSG_NAME));
        c.setCpf(getUserString(MSG_CPF));
        c.setTelefone(getUserString(MSG_TELF));

        TrataException.tryAndDisplayError(() -> {
            dao.inserir(c);
            System.out.printf("Cliente inserido. [ID - %d]\n", c.getId());
        });
    }

    private static void excluirClientePorCPF() {
        System.out.println("-------------------- EXCLUINDO CLIENTE --------------------");
        TrataException.tryAndDisplayError(() -> {
            dao.deletarPorCPF(getUserString(MSG_CPF));
            System.out.println("Cliente excluído com sucesso.");
        });
    }

    private static void listarTodosClientes() {
        System.out.println("-------------------- LISTA DE CLIENTES --------------------");
        TrataException.tryAndDisplayError(() -> {
            List<Cliente> list = dao.listarTodos();
            list.forEach(System.out::println);            
        });
    }

    private static void filtrarClientePorCPF() {
        System.out.println("-------------------- FILTRAR CLIENTE --------------------");
        TrataException.tryAndDisplayError(() -> {
            Cliente c = dao.filtrarPorCPF(getUserString(MSG_CPF));
            System.out.println("Cliente localizado:");
            System.out.println(c);
        });
    }

    private static String getUserString(String message) {
        System.out.println(message);
        return teclado.ler();
    }
}
