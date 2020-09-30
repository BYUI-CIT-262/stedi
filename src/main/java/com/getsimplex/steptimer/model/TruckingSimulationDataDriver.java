package com.getsimplex.steptimer.model;

import com.getsimplex.steptimer.service.CreateNewCustomer;
import com.getsimplex.steptimer.service.MessageIntake;
import com.getsimplex.steptimer.utils.JedisData;
import com.google.gson.Gson;
import scala.concurrent.java8.FuturesConvertersImpl;

import java.util.*;

public class TruckingSimulationDataDriver {

    private static String[] lastNames = {"Jones", "Smith", "Ahmed", "Wu", "Doshi", "Anandh", "Clayton", "Harris", "Gonzalez", "Abram", "Khatib", "Clark", "Mitra", "Habschied", "Jackson", "Phillips", "Lincoln", "Spencer", "Anderson", "Hansen", "Davis", "Jones", "Fibonnaci", "Staples", "Jefferson", "Huey", "Olson", "Howard", "Sanchez", "Aristotle"};
    private static String[] firstNames = {"Sarah", "Bobby", "Frank", "Edward", "Danny", "Chris", "Spencer", "Ashley", "Santosh", "Senthil", "Christina", "Suresh", "Neeraj", "Angie", "Sean", "Lyn", "John", "Ben", "Travis", "David", "Larry", "Jerry", "Gail", "Craig", "Dan", "Jason", "Eric", "Trevor", "Jane", "Jacob", "Jaya", "Manoj", "Liz", "Christina"};
    private static String[] locations = {"Alabama", "Indiana", "New Mexico", "Georgia", "Illinois", "Nevada", "Colardo", "Maryland", "Canada", "South Dakota", "Iowa", "Tennessee", "Pennsylvania", "Texas", "Arizona", "Louisiana", "Florida", "Michigan", "Wisconsin", "Minnesota"};
    private static String[] gearPositions = {"Neutral", "Drive", "Reverse", "Park"};
    private static List<Customer> testCustomers = new ArrayList<Customer>();
    private static Map<String, Truck> testTrucks = new HashMap<String, Truck>();
    private static List<Reservation> testReservations = new ArrayList<Reservation>();
    private static Random random = new Random();
    private static Gson gson = new Gson();
    private static boolean simulationActive = false;


    public static synchronized void generateTestCustomers(int numberOfUsers) {
        testCustomers.clear();
        int nextCustomerAge = 55;
        for (int i = 0; i < numberOfUsers - 1; i++) {
            try {
                Customer customer = new Customer();
                String firstName = firstNames[random.nextInt(numberOfUsers)];
                String lastName = lastNames[random.nextInt(numberOfUsers)];
                customer.setCustomerName(firstName + " " + lastName);
                customer.setEmail(firstName + "." + lastName + "@test.com");
                customer.setPhone(String.valueOf(random.nextInt(9)+"015551212"));
                customer.setAccountNumber(String.valueOf(String.valueOf(random.nextInt(999999999))));
                customer.setBirthDay((2020-nextCustomerAge++)+"-01-01");//spread age out evenly
                customer.setLocation(locations[random.nextInt(30)]);
                CreateNewCustomer.createCustomer(customer);
                testCustomers.add(customer);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static synchronized void generateTestTrucks(){
        for (Customer testCustomer:testCustomers) {
            Truck testTruck = new Truck();
            testTruck.setTruckNumber(String.valueOf(random.nextInt(10000)));
            testTruck.setFillPercent(random.nextInt(95));
            testTruck.setGearPosition(gearPositions[random.nextInt(3)]);
            testTruck.setMilesFromShop(random.nextInt(1000));
            testTrucks.put(testTruck.getTruckNumber(),testTruck);
        }
    }

    public static void generateTestReservations() throws Exception {
        for (Customer testCustomer:testCustomers){
            Reservation reservation = createNewReservation(testCustomer.getAccountNumber());
            testReservations.add(reservation);
            JedisData.loadToJedis(reservation, Reservation.class);
        }

    }


    public static void createFuelLevelUpdates() throws Exception{
        for(Truck testTruck:testTrucks.values()){
            testTruck.setFillPercent(testTruck.getFillPercent()-random.nextInt(3));
            KafkaTopicMessage fuelFillLevelMesage = new KafkaTopicMessage();
            fuelFillLevelMesage.setTopic("fuel-level");
            fuelFillLevelMesage.setKey(Long.valueOf(testTruck.getTruckNumber()));
            fuelFillLevelMesage.setMessage(String.valueOf(testTruck.getFillPercent()));
            MessageIntake.route(fuelFillLevelMesage);
            Thread.sleep(2000);
        }
    }

    public static void createGearPositionUpdates() throws Exception{
        for (Truck testTruck:testTrucks.values()){
            testTruck.setGearPosition(gearPositions[random.nextInt(3)]);
            KafkaTopicMessage gearPositionMessage = new KafkaTopicMessage();
            gearPositionMessage.setTopic("gear-position");
            gearPositionMessage.setKey(Long.valueOf(testTruck.getTruckNumber()));
            gearPositionMessage.setMessage(String.valueOf(testTruck.getGearPosition()));
            MessageIntake.route(gearPositionMessage);
            Thread.sleep(2000);
        }
    }

    public static void createVehicleStatusUpdates() throws Exception{
        for (Reservation reservation:testReservations){

            Truck testTruck = testTrucks.get(reservation.getTruckNumber());
            VehicleStatus vehicleStatus = new VehicleStatus();
            vehicleStatus.setTruckNumber(reservation.getTruckNumber());
            vehicleStatus.setDestination(reservation.getDestination());
            testTruck.setMilesFromShop(testTruck.getMilesFromShop()-random.nextInt(3));//always decreasing
            vehicleStatus.setMilesFromShop(testTruck.getMilesFromShop());
            testTruck.setOdometerReading(testTruck.getOdometerReading()+random.nextInt(100));//always increasing
            vehicleStatus.setOdomoterReading(testTruck.getOdometerReading());

            KafkaTopicMessage vehicleStatusMessage = new KafkaTopicMessage();
            vehicleStatusMessage.setTopic("vehicle-status");
            vehicleStatusMessage.setKey(Long.valueOf(testTruck.getTruckNumber()));
            vehicleStatusMessage.setMessage(gson.toJson(vehicleStatusMessage));

            MessageIntake.route(vehicleStatusMessage);
            Thread.sleep(2000);
        }

    }

    public static void createCheckInStatusUpdates() throws Exception{
        Integer randomReservationIndex = random.nextInt(testReservations.size()-1);
        Reservation changingReservation = testReservations.get(randomReservationIndex);
        changingReservation.setCheckInStatus("In");

        testReservations.set(randomReservationIndex, createNewReservation(changingReservation.getCustomerId()));//create new reservation

        CheckInStatus checkInStatus = new CheckInStatus();
        checkInStatus.setReservationId(changingReservation.getReservationId());
        checkInStatus.setLocationName(changingReservation.getDestination());
        checkInStatus.setTruckNumber(changingReservation.getTruckNumber());
        checkInStatus.setStatus("In");

        KafkaTopicMessage checkInStatusMessage = new KafkaTopicMessage();
        checkInStatusMessage.setTopic("check-in");
        checkInStatusMessage.setKey(Long.valueOf(changingReservation.getReservationId()));
        checkInStatusMessage.setMessage(gson.toJson(checkInStatus));

        MessageIntake.route(checkInStatusMessage);
        Thread.sleep(2000);
    }

    public static synchronized void createPayments() throws Exception{
        Integer randomReservationIndex = random.nextInt(testReservations.size()-1);
        Reservation payingReservation = testReservations.get(randomReservationIndex);
        Payment payment = new Payment();
        payment.setAmount(Float.valueOf(String.valueOf(random.nextInt(1000)) + "." + String.valueOf(random.nextInt(99))));
        payment.setReservationId(payingReservation.getReservationId());
        payment.setCustomerId(payingReservation.getCustomerId());
        payment.setCustomerName(payingReservation.getCustomerName());

        JedisData.loadToJedis(payment, Payment.class);
    }

    public static Reservation createNewReservation(String accountNumber){
        Reservation reservation = new Reservation();
        reservation.setCheckInStatus("CheckedOut");
        reservation.setCustomerId(accountNumber);
        reservation.setCustomerName(accountNumber);
        reservation.setTruckNumber(testTrucks.get(random.nextInt(29)).getTruckNumber());
        reservation.setReservationDate(new Date());
        reservation.setReservationId(String.valueOf(reservation.getReservationDate().getTime()));
        reservation.setOrigin(locations[random.nextInt(locations.length-1)]);
        reservation.setDestination(locations[random.nextInt(locations.length-1)]);

        return reservation;
    }

}
