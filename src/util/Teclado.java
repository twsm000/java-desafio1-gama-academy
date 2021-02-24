package util;

import java.util.Scanner;

public class Teclado implements AutoCloseable {
    private static Teclado instance;
    private final Scanner scan;

    private Teclado() {
        System.out.println("Inicializando leitura de dados do teclado...");
        scan = new Scanner(System.in);
    }

    public static Teclado getInstance() {
        if (instance == null) {
            instance = new Teclado();
        }

        return instance;
    }

    @Override
    public void close() {
        System.out.println("Finalizando leitura de dados do teclado...");
        scan.close();
    }

    public String ler() {
        try {
            return scan.nextLine();
        } finally {
            scan.reset();
        }
    }
}
