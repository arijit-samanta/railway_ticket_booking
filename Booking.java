import java.util.Random;

// ===============================
// BOOKING CLASS
// ===============================
public class Booking {

    private long pnr;
    private Passenger passenger;
    private Train train;
    private String journeyDate;
    private String coach;
    private int seatNumber;
    private String classType;
    private double totalFare;
    private String status;

    public Booking(Passenger passenger,
                   Train train,
                   String journeyDate,
                   String classType,
                   double totalFare) {

        Random random = new Random();

        this.pnr = 1000000000L +
                   random.nextInt(900000000);

        this.passenger = passenger;
        this.train = train;
        this.journeyDate = journeyDate;
        this.classType = classType;
        this.totalFare = totalFare;

        this.coach = generateCoach(classType);

        this.seatNumber =
                1 + random.nextInt(72);

        this.status = "CONFIRMED";
    }

    private String generateCoach(String classType) {

        Random random = new Random();

        if (classType.equals("Sleeper")) {

            return "S" +
                    (1 + random.nextInt(10));

        } else {

            return "B" +
                    (1 + random.nextInt(5));
        }
    }

    public long getPnr() {
        return pnr;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Train getTrain() {
        return train;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public String getClassType() {
        return classType;
    }

    public String getCoach() {
        return coach;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public String getStatus() {
        return status;
    }

    public void displayTicket() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("          RAILWAY TICKET");
        System.out.println("========================================");

        System.out.println("PNR          : " + pnr);

        System.out.println("----------------------------------------");

        passenger.displayDetails();

        System.out.println("----------------------------------------");

        System.out.println("Train Number : "
                + train.getTrainNumber());

        System.out.println("Train Name   : "
                + train.getTrainName());

        System.out.println("From         : "
                + train.getSource());

        System.out.println("To           : "
                + train.getDestination());

        System.out.println("Departure    : "
                + train.getDepartureTime());

        System.out.println("Arrival      : "
                + train.getArrivalTime());

        System.out.println("Journey Date : "
                + journeyDate);

        System.out.println("----------------------------------------");

        System.out.println("Class        : " + classType);
        System.out.println("Coach        : " + coach);
        System.out.println("Seat Number  : " + seatNumber);

        System.out.println("----------------------------------------");

        System.out.println("Total Fare   : Rs. "
                + String.format("%.2f", totalFare));

        System.out.println("Status       : " + status);

        System.out.println("========================================");
        System.out.println("          HAPPY JOURNEY!");
        System.out.println("========================================");
    }
}
