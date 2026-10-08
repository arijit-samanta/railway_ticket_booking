import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class main {

    private final RailwaySystem railway = new RailwaySystem();
    private final JFrame frame = new JFrame("Railway Booking System");
    private final JComboBox<String> searchSource = new JComboBox<>(stations());
    private final JComboBox<String> searchDestination = new JComboBox<>(stations());
    private final JComboBox<String> bookingSource = new JComboBox<>(stations());
    private final JComboBox<String> bookingDestination = new JComboBox<>(stations());
    private final JComboBox<Train> availableTrains = new JComboBox<>();
    private final JTextField passengerName = new JTextField(18);
    private final JTextField passengerAge = new JTextField(18);
    private final JTextField passengerGender = new JTextField(18);
    private final JTextField passengerMobile = new JTextField(18);
    private final JTextField journeyDate = new JTextField(18);
    private final JComboBox<String> travelClass = new JComboBox<>(
            new String[]{"Sleeper", "AC 3 Tier", "AC 2 Tier", "AC First Class"}
    );
    private final JTextField pnrField = new JTextField(18);
    private boolean updatingTrainOptions;
    private final DefaultTableModel searchResults = new DefaultTableModel(
            new String[]{"Train No.", "Train", "From", "To", "Departure", "Arrival", "Base Fare"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(
                        null,
                        "Unable to load the system appearance: " + exception.getMessage(),
                        "Appearance Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            new main().showWindow();
        });
    }

    private static String[] stations() {
        return new String[]{"Kolkata", "Delhi", "Chennai", "Ranchi"};
    }

    private void showWindow() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(760, 520));
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(12, 12));

        JLabel heading = new JLabel("RAILWAY BOOKING SYSTEM");
        heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 23));
        heading.setBorder(BorderFactory.createEmptyBorder(18, 20, 8, 20));
        frame.add(heading, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Search Trains", createSearchPanel());
        tabs.addTab("Book Ticket", createBookingPanel());
        tabs.addTab("Check PNR", createPnrPanel());
        frame.add(tabs, BorderLayout.CENTER);

        JLabel footer = new JLabel("Plan your journey with Railway Booking System");
        footer.setBorder(BorderFactory.createEmptyBorder(8, 20, 14, 20));
        frame.add(footer, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controls.add(new JLabel("From"));
        controls.add(searchSource);
        controls.add(new JLabel("To"));
        controls.add(searchDestination);
        JButton searchButton = new JButton("Search");
        controls.add(searchButton);
        panel.add(controls, BorderLayout.NORTH);

        JTable table = new JTable(searchResults);
        table.setFillsViewportHeight(true);
        table.setRowHeight(26);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        searchButton.addActionListener(event -> searchTrains());
        return panel;
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 20, 18, 20));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 8, 6, 8);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        int row = 0;
        addFormRow(form, constraints, row++, "From", bookingSource);
        addFormRow(form, constraints, row++, "To", bookingDestination);
        addFormRow(form, constraints, row++, "Train", availableTrains);
        addFormRow(form, constraints, row++, "Passenger name", passengerName);
        addFormRow(form, constraints, row++, "Age", passengerAge);
        addFormRow(form, constraints, row++, "Gender", passengerGender);
        addFormRow(form, constraints, row++, "Mobile number", passengerMobile);
        addFormRow(form, constraints, row++, "Journey date (DD-MM-YYYY)", journeyDate);
        addFormRow(form, constraints, row, "Class", travelClass);

        JButton bookButton = new JButton("Book Ticket");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(bookButton);
        panel.add(form, BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);

        bookingSource.addActionListener(event -> updateAvailableTrains());
        bookingDestination.addActionListener(event -> updateAvailableTrains());
        travelClass.addActionListener(event -> updateFarePreview());
        availableTrains.addActionListener(event -> updateFarePreview());
        bookButton.addActionListener(event -> bookTicket());

        updateAvailableTrains();
        return panel;
    }

    private JPanel createPnrPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controls.add(new JLabel("PNR number"));
        controls.add(pnrField);
        JButton checkButton = new JButton("Find Ticket");
        controls.add(checkButton);
        panel.add(controls, BorderLayout.NORTH);

        JLabel hint = new JLabel("Enter the PNR from your booking confirmation.");
        panel.add(hint, BorderLayout.CENTER);

        checkButton.addActionListener(event -> checkPnr());
        pnrField.addActionListener(event -> checkPnr());
        return panel;
    }

    private void addFormRow(
            JPanel panel,
            GridBagConstraints constraints,
            int row,
            String label,
            java.awt.Component input) {

        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0;
        panel.add(new JLabel(label), constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        panel.add(input, constraints);
    }

    private void searchTrains() {
        String source = (String) searchSource.getSelectedItem();
        String destination = (String) searchDestination.getSelectedItem();

        if (source.equals(destination)) {
            showError("Choose different origin and destination stations.");
            return;
        }

        searchResults.setRowCount(0);
        for (Train train : railway.getAvailableTrains(source, destination)) {
            searchResults.addRow(new Object[]{
                    train.getTrainNumber(),
                    train.getTrainName(),
                    train.getSource(),
                    train.getDestination(),
                    train.getDepartureTime(),
                    train.getArrivalTime(),
                    String.format("Rs. %.2f", train.getFare())
            });
        }

        if (searchResults.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    frame,
                    "No trains are available for this route.",
                    "No trains found",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void updateAvailableTrains() {
        if (updatingTrainOptions) {
            return;
        }

        String source = (String) bookingSource.getSelectedItem();
        String destination = (String) bookingDestination.getSelectedItem();
        Train selected = (Train) availableTrains.getSelectedItem();

        updatingTrainOptions = true;
        try {
            availableTrains.removeAllItems();
            if (source != null && destination != null && !source.equals(destination)) {
                ArrayList<Train> trains = railway.getAvailableTrains(source, destination);
                for (Train train : trains) {
                    availableTrains.addItem(train);
                }
            }

            if (selected != null) {
                for (int index = 0; index < availableTrains.getItemCount(); index++) {
                    if (availableTrains.getItemAt(index).getTrainNumber() == selected.getTrainNumber()) {
                        availableTrains.setSelectedIndex(index);
                        break;
                    }
                }
            }
        } finally {
            updatingTrainOptions = false;
        }
        updateFarePreview();
    }

    private void updateFarePreview() {
        Train train = (Train) availableTrains.getSelectedItem();
        if (train == null) {
            availableTrains.setToolTipText("No trains available for this route.");
            return;
        }

        String selectedClass = (String) travelClass.getSelectedItem();
        double multiplier;
        switch (selectedClass) {
            case "AC 3 Tier":
                multiplier = 1.5;
                break;
            case "AC 2 Tier":
                multiplier = 2;
                break;
            case "AC First Class":
                multiplier = 2.5;
                break;
            default:
                multiplier = 1;
        }
        availableTrains.setToolTipText(
                "Estimated fare: Rs. " + String.format("%.2f", train.getFare() * multiplier)
        );
    }

    private void bookTicket() {
        Train train = (Train) availableTrains.getSelectedItem();
        if (train == null) {
            showError("No train is available for the selected route.");
            return;
        }

        String name = passengerName.getText().trim();
        String ageText = passengerAge.getText().trim();
        String gender = passengerGender.getText().trim();
        String mobile = passengerMobile.getText().trim();
        String date = journeyDate.getText().trim();

        if (name.isEmpty() || gender.isEmpty() || mobile.isEmpty()) {
            showError("Enter the passenger name, gender, and mobile number.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException exception) {
            showError("Age must be a whole number.");
            return;
        }
        if (age < 1 || age > 120) {
            showError("Enter an age between 1 and 120.");
            return;
        }

        try {
            DateTimeFormatter dateFormat =
                    DateTimeFormatter.ofPattern("dd-MM-uuuu")
                            .withResolverStyle(java.time.format.ResolverStyle.STRICT);
            LocalDate.parse(date, dateFormat);
        } catch (DateTimeParseException exception) {
            showError("Enter a valid journey date in DD-MM-YYYY format.");
            return;
        }

        Passenger passenger = new Passenger(name, mobile, age, gender);
        Booking booking = railway.bookTicket(
                passenger,
                train,
                date,
                (String) travelClass.getSelectedItem()
        );
        showTicket(booking, "Ticket booked successfully");
        clearPassengerFields();
    }

    private void checkPnr() {
        String pnrText = pnrField.getText().trim();
        long pnr;
        try {
            pnr = Long.parseLong(pnrText);
        } catch (NumberFormatException exception) {
            showError("Enter a valid numeric PNR.");
            return;
        }

        Booking booking = railway.searchBooking(pnr);
        if (booking == null) {
            JOptionPane.showMessageDialog(
                    frame,
                    "No booking was found for PNR " + pnr + ".",
                    "PNR not found",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        showTicket(booking, "Booking details");
    }

    private void showTicket(Booking booking, String title) {
        Passenger passenger = booking.getPassenger();
        Train train = booking.getTrain();
        String details = String.format(
                "RAILWAY TICKET%n%n" +
                "PNR: %d%n%n" +
                "Passenger: %s%nAge: %d%nGender: %s%nMobile: %s%n%n" +
                "Train: %d - %s%nFrom: %s%nTo: %s%n" +
                "Departure: %s%nArrival: %s%nJourney date: %s%n%n" +
                "Class: %s%nCoach: %s%nSeat: %d%n" +
                "Total fare: Rs. %.2f%nStatus: %s",
                booking.getPnr(),
                passenger.getName(),
                passenger.getAge(),
                passenger.getGender(),
                passenger.getMobile(),
                train.getTrainNumber(),
                train.getTrainName(),
                train.getSource(),
                train.getDestination(),
                train.getDepartureTime(),
                train.getArrivalTime(),
                booking.getJourneyDate(),
                booking.getClassType(),
                booking.getCoach(),
                booking.getSeatNumber(),
                booking.getTotalFare(),
                booking.getStatus()
        );

        JTextArea ticket = new JTextArea(details);
        ticket.setEditable(false);
        ticket.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        ticket.setCaretPosition(0);
        JScrollPane scrollPane = new JScrollPane(ticket);
        scrollPane.setPreferredSize(new Dimension(430, 390));
        JOptionPane.showMessageDialog(
                frame,
                scrollPane,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void clearPassengerFields() {
        passengerName.setText("");
        passengerAge.setText("");
        passengerGender.setText("");
        passengerMobile.setText("");
        journeyDate.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                frame,
                message,
                "Invalid input",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
