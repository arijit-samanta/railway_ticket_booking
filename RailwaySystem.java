import java.util.ArrayList;

public class RailwaySystem {

    private ArrayList<Train> trains;
    private ArrayList<Booking> bookings;

    public RailwaySystem() {

        trains = new ArrayList<>();
        bookings = new ArrayList<>();

        addTrains();
    }

    // Add train data
    private void addTrains() {

        trains.add(new Train(
                12301,
                "Howrah Rajdhani Express",
                "Kolkata",
                "Delhi",
                "16:55",
                "10:00",
                1200
        ));

        trains.add(new Train(
                12314,
                "Sealdah Rajdhani Express",
                "Kolkata",
                "Delhi",
                "16:50",
                "10:00",
                1250
        ));

        trains.add(new Train(
                12841,
                "Coromandel Express",
                "Kolkata",
                "Chennai",
                "14:50",
                "17:00",
                950
        ));

        trains.add(new Train(
                12019,
                "Shatabdi Express",
                "Kolkata",
                "Ranchi",
                "06:05",
                "13:10",
                750
        ));
    }

    // Search train
    public void searchTrain(String source,
                            String destination) {

        boolean found = false;

        System.out.println("\n========== SEARCH RESULT ==========");

        for (Train train : trains) {

            if (train.getSource()
                    .equalsIgnoreCase(source)

                    &&

                    train.getDestination()
                    .equalsIgnoreCase(destination)) {

                train.displayTrain();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No train found.");
        }
    }

    // Get trains for booking
    public ArrayList<Train> getAvailableTrains(
            String source,
            String destination) {

        ArrayList<Train> result =
                new ArrayList<>();

        for (Train train : trains) {

            if (train.getSource()
                    .equalsIgnoreCase(source)

                    &&

                    train.getDestination()
                    .equalsIgnoreCase(destination)) {

                result.add(train);
            }
        }

        return result;
    }

    // Book ticket
    public Booking bookTicket(
            Passenger passenger,
            Train train,
            String date,
            String classType) {

        double fare = calculateFare(
                train.getFare(),
                classType
        );

        Booking booking =
                new Booking(
                        passenger,
                        train,
                        date,
                        classType,
                        fare
                );

        bookings.add(booking);

        return booking;
    }

    // Polymorphism / method behavior
    private double calculateFare(
            double basicFare,
            String classType) {

        switch (classType) {

            case "Sleeper":
                return basicFare;

            case "AC 3 Tier":
                return basicFare * 1.5;

            case "AC 2 Tier":
                return basicFare * 2;

            case "AC First Class":
                return basicFare * 2.5;

            default:
                return basicFare;
        }
    }

    // Search booking by PNR
    public Booking searchBooking(long pnr) {

        for (Booking booking : bookings) {

            if (booking.getPnr() == pnr) {
                return booking;
            }
        }

        return null;
    }
}