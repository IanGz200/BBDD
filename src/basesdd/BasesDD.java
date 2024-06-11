/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package basesdd;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

/**
 * -Comenta la clase-
 *
 * @author IanGz
 */
public class BasesDD {

    /**
     * Mostra o menú principal da app
     *
     * @param scanner Scanner para ler do teclado a opción
     * @return Opción introducida polo usuario, entre as válidas permitidas
     */
    private int showMenu(Scanner scanner) {
        int option;
        do {
            System.out.println();
            System.out.println();
            System.out.println("App de xestión de películas");
            System.out.println("Escolle unha opción:");
            System.out.println("1. Insertar unha nova película");
            System.out.println("2. Mostrar todas as películas");
            System.out.println("3. Buscar unha película");
            System.out.println("4. Pechar a aplicación");
            option = scanner.nextInt();
            scanner.nextLine();
            if (option < 1 || option > 4) {
                System.out.println("Debes introducir un número entre o 1 e o 4!");
            }
        } while (option < 1 || option > 4);
        return option;
    }

    /**
     * Almacena unha nova película na BD
     *
     * @param c Conexión aberta coa BD
     * @param title Título da nova película
     * @param year Ano da nova película
     * @throws SQLException En caso de producirse algún erro no acceso á DB
     */
    private void newFilm(Connection c, String title, int year) throws SQLException {
        PreparedStatement pst = c.prepareStatement(
                "INSERT INTO films (title,year) VALUES(?,?)");
        pst.setString(1, title);
        pst.setInt(2, year);
        pst.execute();
        pst.close();
    }

    /**
     * Mostra por pantalla os datos de todas as películas
     *
     * @param c Conexión aberta coa BD
     * @throws SQLException En caso de producirse algún erro no acceso á DB
     */
    private void showAllFilms(Connection c) throws SQLException {
        // Executamos a consulta
        Statement st = c.createStatement();
        ResultSet rst = st.executeQuery("SELECT * FROM films");
        // Mostramos todos os datos do resultado
        System.out.println("Listaxe das películas");
        while (rst.next()) {
            System.out.println(rst.getInt("id") + ". " + rst.getString("title")
                    + " - Ano " + rst.getInt("year"));
        }
    }

    /**
     * Mostra por pantalla os datos dunha película
     *
     * @param c Conexión aberta coa BD
     * @param id Id da película a buscar
     * @throws SQLException En caso de producirse algún erro no acceso á DB
     */
    private void showFilm(Connection c, int id) throws SQLException {
        // Executamos a consulta
        PreparedStatement pst = c.prepareStatement(
                "SELECT * FROM films WHERE id=?");
        pst.setInt(1, id);
        ResultSet rst = pst.executeQuery();
        // Se se atopa algún resultado mostramos os seus datos
        if (rst.next()) {
            System.out.println("Película atopada");
            System.out.println("Datos da película:");
            System.out.println("Id: " + rst.getInt("id"));
            System.out.println("Título: " + rst.getString("title"));
            System.out.println("Ano: " + rst.getInt("year"));
        } else {
            System.out.println("Non se atopou unha película con ese id!");
        }
    }

    /**
     * Método principal, que mostra o menú e executa as accións correspondentes
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Obtemos unha conexión coa base de datos
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/peliculas",
                    "peliculas", "abc123.");
            // Inicializamos as variables
            BasesDD app = new BasesDD();
            Scanner scanner = new Scanner(System.in);
            int option;
            // Mostramos o menu e realizamos as accións correspondentes ata que
            // o usuario escolla a opción de saír
            do {
                option = app.showMenu(scanner);
                switch (option) {
                    case 1:
                        System.out.println("Introduce o título da película:");
                        String title = scanner.nextLine();
                        System.out.println("Introduce o ano da película:");
                        int year = scanner.nextInt();
                        app.newFilm(connection, title, year);
                        break;
                    case 2:
                        app.showAllFilms(connection);
                        break;
                    case 3:
                        System.out.println("Introduce o id da película a buscar:");
                        int id = scanner.nextInt();
                        app.showFilm(connection, id);
                }
            } while (option != 4);
            // Pechamos a conexion coa BD
            connection.close();
        } catch (SQLException e) {
            System.out.println("Erro nas operacións coa base de datos!");
        }
    }

}
