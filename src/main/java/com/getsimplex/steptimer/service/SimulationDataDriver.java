package com.getsimplex.steptimer.service;

import com.getsimplex.steptimer.model.Customer;
import com.getsimplex.steptimer.model.DeviceMessage;
import com.getsimplex.steptimer.model.RapidStepTest;
import com.getsimplex.steptimer.model.User;
import com.getsimplex.steptimer.utils.JedisData;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationDataDriver {

    private static String[] lastNames = {"Jones", "Smith", "Ahmed", "Wu", "Doshi", "Anandh", "Clayton", "Harris", "Gonzalez", "Abram", "Khatib", "Clark", "Mitra", "Habschied", "Jackson", "Phillips", "Lincoln", "Spencer", "Anderson", "Hansen", "Davis", "Jones", "Fibonnaci", "Staples", "Jefferson", "Huey", "Olson", "Howard", "Sanchez", "Aristotle"};
    private static String[] firstNames = {"Sarah", "Bobby", "Frank", "Edward", "Danny", "Chris", "Spencer", "Ashley", "Santosh", "Senthil", "Christina", "Suresh", "Neeraj", "Angie", "Sean", "Lyn", "John", "Ben", "Travis", "David", "Larry", "Jerry", "Gail", "Craig", "Dan", "Jason", "Eric", "Trevor", "Jane", "Jacob", "Jaya", "Manoj", "Liz", "Christina"};
    private static List<Customer> testCustomers = new ArrayList<Customer>();
    private static Random random = new Random();
    private static Gson gson = new Gson();
    private static Session remoteSession;
    private static boolean simulationActive = false;

    public static synchronized void setSimulationActive(boolean active){
        simulationActive= active;
    }

    public static synchronized  boolean getSimulationActive(){
        return simulationActive;
    }

    public static synchronized void setRemoteSession(Session session){
        remoteSession=session;
    }

    public static synchronized void generateTestCustomers(int numberOfUsers) {
        if (testCustomers.size()==0) {//this should only happen once
            for (int i = 0; i < numberOfUsers - 1; i++) {
                try {
                    Customer customer = new Customer();
                    String firstName = firstNames[random.nextInt(numberOfUsers)];
                    String lastName = lastNames[random.nextInt(numberOfUsers)];
                    customer.setCustomerName(firstName + " " + lastName);
                    customer.setEmail(firstName + "." + lastName + "@test.com");
                    customer.setPhone("8015551212");
                    customer.setBirthDay("01/01/1920");
                    CreateNewCustomer.createCustomer(customer);
                    testCustomers.add(customer);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void createRapidStepTests() {
        for (Customer testCustomer:testCustomers){
             try {
                 long randomChange = random.nextInt(60);//negative offset (in seconds) from 2 minute test time
                 long testTime = (120 - randomChange) * 1000;//test time (in milliseconds)
                 RapidStepTest rapidStepTest = new RapidStepTest();
                 rapidStepTest.setCustomer(testCustomer);
                 rapidStepTest.setStopTime(System.currentTimeMillis());
                 rapidStepTest.setStartTime(rapidStepTest.getStopTime() - testTime);
                 rapidStepTest.setTestTime(testTime);
                 rapidStepTest.setTotalSteps(30);
                 JedisData.loadToJedis(rapidStepTest, RapidStepTest.class);
                 DeviceMessage deviceMessage = new DeviceMessage();
                 deviceMessage.setDate(System.currentTimeMillis());
                 deviceMessage.setDeviceId("1234");//this is just a device id used for testing
                 deviceMessage.setMessage(gson.toJson(rapidStepTest));
                 MessageIntake.route(deviceMessage);
                 Thread.sleep(2000);//2 seconds sleep time between each message makes a new message every minute for every customer assuming 30 test customers
             } catch (Exception e){
                 System.out.println(e.getMessage());
             }
        }
    }

}
