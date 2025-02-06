import java.sql.Connection;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Menu {
    private Scanner scanner = new Scanner(System.in);


    public Menu(){}

    public void OpretNummer() {
        while (true) {
            System.out.println("Velkommen til telefonbogen g");
            System.out.println("Tryk 1 for at tilføje et telefon nummer");
            System.out.println("Tryk 2 for at se numre");
            System.out.println("Tryk 3 for at søge");
            System.out.println("Tryk 4 for at afslutte");
            int valg = scanner.nextInt();
            scanner.nextLine();

            switch (valg) {
                case 1:
                    System.out.println("Indtast nummer: ");
                    int nummer = Integer.parseInt(scanner.nextLine());
                    System.out.println("Indtast navnet: ");
                    String name = scanner.nextLine();
                    System.out.println("Nummeret: " + nummer + " og navnet: " + name + " er tilføjet til telefonbogen!");
                    addToDatabase(nummer, name);
                    break;

                case 2:
                    System.out.println("Viser alle numre: ");
                    displayContacts();
                    break;

                case 3:
                    System.out.println("Søg efter navn eller nummer: ");
                    String searchQuery = scanner.nextLine();
                    soegContacts(searchQuery);
                    break;

                case 4:
                    System.out.println("Telefonboget lukkes...");
                    return;

                default:
                    System.out.println("Ugyldigt valg, prøv igen...");
            }
        }
    }
    private void addToDatabase(int nummer, String name) {
        String sql = "INSERT INTO kontakter (nummer, name) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nummer);
            stmt.setString(2, name);
            stmt.executeUpdate();

            System.out.println("Kontakt tilføjet: " + name);
        } catch (SQLException e) {
            System.out.println("Fejl: " + e.getMessage());
        }
    }

    private void displayContacts() {
        String sql = "SELECT * FROM kontakter";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\nTelefonbog Kontakter:");
            System.out.println("----------------------");

            while (rs.next()) {
                int nummer = rs.getInt("nummer");
                String name = rs.getString("name");
                System.out.println("Nummeret: " + nummer + " | Navn: " + name);
            }
            System.out.println("----------------------");

        } catch (SQLException e) {
            System.out.println("Fejl: " + e.getMessage());
        }
    }

    public void soegContacts(String searchQuery){
        String sql = "SELECT * FROM kontakter WHERE nummer = ? OR name LIKE ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            try {
                int nummer = Integer.parseInt(searchQuery);
                stmt.setInt(1, nummer);
            } catch (NumberFormatException e) {
                stmt.setInt(1, 0);
            }

            stmt.setString(2, "%" + searchQuery + "%");

            ResultSet rs = stmt.executeQuery();

            System.out.println("\nSøgeresultater:");
            System.out.println("----------------------");

            boolean found = false;
            while (rs.next()) {
                int nummer = rs.getInt("nummer");
                String name = rs.getString("name");
                System.out.println("Nummeret: " + nummer + " | Navn: " + name);
                found = true;
            }

            if (!found) {
                System.out.println("Ingen resultater fundet.");
            }

            System.out.println("----------------------");

        } catch (SQLException e) {
            System.out.println("Fejl: " + e.getMessage());
        }
    }

}
