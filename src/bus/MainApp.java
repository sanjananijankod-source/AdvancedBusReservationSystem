package bus;

import java.util.List;
import java.util.Scanner;

public class MainApp {

    // COLORS
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BusDAO dao = new BusDAO();

        while (true) {
            System.out.println(BLUE + "\n===== BUS RESERVATION SYSTEM =====" + RESET);
            System.out.println("1. Add Bus");
            System.out.println("2. View All Buses");
            System.out.println("3. Book Ticket");
            System.out.println("4. Cancel Ticket");
            System.out.println("5. View All Tickets");
            System.out.println("6. Exit");
            System.out.print(YELLOW + "Enter choice: " + RESET);

            int ch = sc.nextInt();
            sc.nextLine(); // to clear buffer

            switch (ch) {

                case 1:
                    System.out.print("Enter Route: ");
                    String route = sc.nextLine();

                    System.out.print("Enter Departure Time: ");
                    String time = sc.nextLine();

                    System.out.print("Enter Total Seats: ");
                    int seats = sc.nextInt();

                    System.out.print("Enter Ticket Price: ");
                    double price = sc.nextDouble();
                    
                    Bus bus = new Bus(0, route, time, seats, seats, price); dao.addBus(bus);
                   // dao.addBus(route, time, seats, price);
                   // break;

                case 2:
                    List<Bus> buses = dao.getAllBuses();
                    System.out.println(BLUE + "\n---- All Buses ----" + RESET);
                    for (Bus b : buses)
                        System.out.println(GREEN + b + RESET);
                    break;

                case 3:
                    System.out.print("Enter Bus ID: ");
                    int busId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Passenger Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Seats to Book: ");
                    int bookSeats = sc.nextInt();

                    dao.bookTicket(busId, name, bookSeats);
                    break;

                case 4:
                    System.out.print("Enter Ticket ID to Cancel: ");
                    int tid = sc.nextInt();
                    dao.cancelTicket(tid);
                    break;

                case 5:
                    List<Ticket> tickets = dao.getAllTickets();
                    System.out.println(BLUE + "\n---- All Tickets ----" + RESET);
                    for (Ticket t : tickets)
                        System.out.println(GREEN + t + RESET);
                    break;

                case 6:
                    System.out.println(GREEN + "Thank you for using Bus Reservation System!" + RESET);
                    System.exit(0);
                    break;

                default:
                    System.out.println(RED + "Invalid Choice!" + RESET);
            }

        }

    }
}