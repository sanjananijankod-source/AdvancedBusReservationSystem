package bus;

public class Bus {

    private int id;
    private String route;
    private String departureTime;
    private int totalSeats;
    private int availableSeats;
    private double price;

    // Constructor
    public Bus(int id, String route, String departureTime, int totalSeats, int availableSeats, double price) {
        this.id = id;
        this.route = route;
        this.departureTime = departureTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.price = price;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getRoute() {
        return route;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Bus ID: " + id +
                " | Route: " + route +
                " | Time: " + departureTime +
                " | Seats: " + availableSeats + "/" + totalSeats +
                " | Price: Rs " + price;
    }
}
