public class Train {

    private int trainNumber;
    private String trainName;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private double fare;

    public Train(int trainNumber,
                 String trainName,
                 String source,
                 String destination,
                 String departureTime,
                 String arrivalTime,
                 double fare) {

        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.fare = fare;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public double getFare() {
        return fare;
    }

    @Override
    public String toString() {
        return trainNumber + " - " + trainName + " (" + departureTime + ")";
    }

    public void displayTrain() {

        System.out.println("--------------------------------------");
        System.out.println("Train Number : " + trainNumber);
        System.out.println("Train Name   : " + trainName);
        System.out.println("From         : " + source);
        System.out.println("To           : " + destination);
        System.out.println("Departure    : " + departureTime);
        System.out.println("Arrival      : " + arrivalTime);
        System.out.println("Fare         : Rs. " + fare);
        System.out.println("--------------------------------------");
    }
}