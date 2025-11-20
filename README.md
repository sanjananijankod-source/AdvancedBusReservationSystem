# AdvancedBusReservationSystem

Advanced Bus Reservation System built with **Java** and **JDBC** (MySQL).

## Features
- User registration & login (console)
- Bus listing and route management
- Seat availability and booking
- Ticket generation
- Admin CRUD for buses and routes
- Transaction safety using commit/rollback

## Tech Stack
- Java (SE)
- JDBC
- MySQL
- Maven (if used) / Plain Java

## How to run
1. Create MySQL database and run provided `schema.sql`.
2. Update DB credentials in `src/config/DBConfig.java`.
3. Build and run:
```bash
# if using Maven
mvn clean compile exec:java -Dexec.mainClass="com.yourpackage.MainApp"

# if plain java
javac -d bin src/**/*.java
java -cp bin com.yourpackage.MainApp
