package bus;

public class Ticket {

    private int ticketId;
    private int busId;
    private String passengerName;
    private int seatsBooked;
    private double amount;

    // Constructor
    public Ticket(int ticketId, int busId, String passengerName, int seatsBooked, double amount) {
        this.ticketId = ticketId;
        this.busId = busId;
        this.passengerName = passengerName;
        this.seatsBooked = seatsBooked;
        this.amount = amount;
    }

    // Getters
    public int getTicketId() {
        return ticketId;
    }

    public int getBusId() {
        return busId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Ticket ID: " + ticketId +
               " | Bus ID: " + busId +
               " | Passenger: " + passengerName +
               " | Seats: " + seatsBooked +
               " | Amount: Rs " + amount;
    }
}
