// ===============================
// PASSENGER CLASS
// INHERITANCE
// ===============================
public class Passenger extends User {

    private int age;
    private String gender;

    public Passenger(String name, String mobile,
                     int age, String gender) {

        super(name, mobile);
        this.age = age;
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    @Override
    void displayDetails() {

        System.out.println("Passenger Name : " + getName());
        System.out.println("Age            : " + age);
        System.out.println("Gender         : " + gender);
        System.out.println("Mobile         : " + getMobile());
    }
}
