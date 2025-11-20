package bus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {

    // Console Colors
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    //--------------------------------------
    // 1) ADD BUS
    //--------------------------------------
    public boolean addBus(Bus bus) {
        String sql = "INSERT INTO buses(route, departure_time, total_seats, available_seats, price) "
                   + "VALUES(?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bus.getRoute());
            ps.setString(2, bus.getDepartureTime());
            ps.setInt(3, bus.getTotalSeats());
            ps.setInt(4, bus.getAvailableSeats());
            ps.setDouble(5, bus.getPrice());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(GREEN + "✔ Bus added successfully!" + RESET);
                return true;
            }

        } catch (Exception e) {
            System.out.println(RED + "❌ Error adding bus: " + e.getMessage() + RESET);
        }
        return false;
    }

    //--------------------------------------
    // 2) GET ALL BUSES
    //--------------------------------------
    public List<Bus> getAllBuses() {
        List<Bus> list = new ArrayList<>();
        String sql = "SELECT * FROM buses";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Bus b = new Bus(
                        rs.getInt("id"),
                        rs.getString("route"),
                        rs.getString("departure_time"),
                        rs.getInt("total_seats"),
                        rs.getInt("available_seats"),
                        rs.getDouble("price")
                );
                list.add(b);
            }

        } catch (Exception e) {
            System.out.println(RED + "❌ Error fetching buses: " + e.getMessage() + RESET);
        }

        return list;
    }

    //--------------------------------------
    // 3) BOOK TICKET
    //--------------------------------------
    public int bookTicket(int busId, String passengerName, int seatsToBook) {

        String checkSql = "SELECT available_seats, price FROM buses WHERE id = ?";
        String insertSql = "INSERT INTO tickets(bus_id, passenger_name, seats_booked, amount) VALUES (?, ?, ?, ?)";
        String updateBusSql = "UPDATE buses SET available_seats = available_seats - ? WHERE id = ?";

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);  // Start transaction

            int availableSeats = 0;
            double pricePerSeat = 0;

            // 1) Check bus & seat availability
            try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                ps.setInt(1, busId);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    System.out.println(RED + "❌ Bus not found!" + RESET);
                    con.rollback();
                    return -1;
                }

                availableSeats = rs.getInt("available_seats");
                pricePerSeat = rs.getDouble("price");

                if (seatsToBook > availableSeats) {
                    System.out.println(YELLOW + "⚠ Only " + availableSeats + " seats available!" + RESET);
                    con.rollback();
                    return -1;
                }
            }

            double totalAmount = seatsToBook * pricePerSeat;

            int ticketId = -1;

            // 2) Insert ticket & get generated ticket ID
            try (PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, busId);
                ps.setString(2, passengerName);
                ps.setInt(3, seatsToBook);
                ps.setDouble(4, totalAmount);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    ticketId = rs.getInt(1);
                }
            }

            // 3) Update available seats
            try (PreparedStatement ps = con.prepareStatement(updateBusSql)) {
                ps.setInt(1, seatsToBook);
                ps.setInt(2, busId);
                ps.executeUpdate();
            }

            con.commit();
            System.out.println(GREEN + "✔ Ticket booked! Ticket ID: " + ticketId + RESET);
            return ticketId;

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            System.out.println(RED + "❌ Error booking ticket: " + e.getMessage() + RESET);
            return -1;

        } finally {
            try { if (con != null) con.setAutoCommit(true); con.close(); } catch (Exception ex) {}
        }
    }

    //--------------------------------------
    // 4) CANCEL TICKET
    //--------------------------------------
    public boolean cancelTicket(int ticketId) {

        String selectSql = "SELECT bus_id, seats_booked FROM tickets WHERE ticket_id = ?";
        String deleteSql = "DELETE FROM tickets WHERE ticket_id = ?";
        String updateBusSql = "UPDATE buses SET available_seats = available_seats + ? WHERE id = ?";

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            int busId = 0, seatsBooked = 0;

            // 1) Get ticket details
            try (PreparedStatement ps = con.prepareStatement(selectSql)) {
                ps.setInt(1, ticketId);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    System.out.println(RED + "❌ Ticket not found!" + RESET);
                    con.rollback();
                    return false;
                }

                busId = rs.getInt("bus_id");
                seatsBooked = rs.getInt("seats_booked");
            }

            // 2) Delete ticket
            try (PreparedStatement ps = con.prepareStatement(deleteSql)) {
                ps.setInt(1, ticketId);
                ps.executeUpdate();
            }

            // 3) Restore seats
            try (PreparedStatement ps = con.prepareStatement(updateBusSql)) {
                ps.setInt(1, seatsBooked);
                ps.setInt(2, busId);
                ps.executeUpdate();
            }

            con.commit();
            System.out.println(GREEN + "✔ Ticket cancelled & seats restored!" + RESET);
            return true;

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            System.out.println(RED + "❌ Error cancelling ticket: " + e.getMessage() + RESET);
            return false;

        } finally {
            try { if (con != null) con.setAutoCommit(true); con.close(); } catch (Exception ex) {}
        }
    }

    //--------------------------------------
    // 5) GET ALL TICKETS
    //--------------------------------------
    public List<Ticket> getAllTickets() {
        List<Ticket> list = new ArrayList<>();
        String sql = "SELECT * FROM tickets";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Ticket t = new Ticket(
                        rs.getInt("ticket_id"),
                        rs.getInt("bus_id"),
                        rs.getString("passenger_name"),
                        rs.getInt("seats_booked"),
                        rs.getDouble("amount")
                );
                list.add(t);
            }

        } catch (Exception e) {
            System.out.println(RED + "❌ Error fetching tickets: " + e.getMessage() + RESET);
        }

        return list;
    }

    //--------------------------------------
    // 6) TOTAL REVENUE
    //--------------------------------------
    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(amount),0) AS total FROM tickets";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (Exception e) {
            System.out.println(RED + "❌ Error calculating revenue: " + e.getMessage() + RESET);
        }

        return 0;
    }
}