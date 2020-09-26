package com.getsimplex.steptimer.service;

import com.getsimplex.steptimer.model.Customer;
import com.getsimplex.steptimer.model.KafkaTopicMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BankingSimulationDataDriver {

    private static String[] lastNames = {"Jones", "Smith", "Ahmed", "Wu", "Doshi", "Anandh", "Clayton", "Harris", "Gonzalez", "Abram", "Khatib", "Clark", "Mitra", "Habschied", "Jackson", "Phillips", "Lincoln", "Spencer", "Anderson", "Hansen", "Davis", "Jones", "Fibonnaci", "Staples", "Jefferson", "Huey", "Olson", "Howard", "Sanchez", "Aristotle"};
    private static String[] firstNames = {"Sarah", "Bobby", "Frank", "Edward", "Danny", "Chris", "Spencer", "Ashley", "Santosh", "Senthil", "Christina", "Suresh", "Neeraj", "Angie", "Sean", "Lyn", "John", "Ben", "Travis", "David", "Larry", "Jerry", "Gail", "Craig", "Dan", "Jason", "Eric", "Trevor", "Jane", "Jacob", "Jaya", "Manoj", "Liz", "Christina"};
    private static List<Customer> testCustomers = new ArrayList<Customer>();
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
                CreateNewCustomer.createCustomer(customer);
                testCustomers.add(customer);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static void createBalanceUpdates(){
        for (Customer testCustomer:testCustomers){
            try {
                KafkaTopicMessage balanceMessage = new KafkaTopicMessage();
                balanceMessage.setTopic("balance-updates");
                balanceMessage.setKey(Long.valueOf(testCustomer.getAccountNumber()));
                balanceMessage.setMessage(String.valueOf(random.nextInt(100000)) + "." + String.valueOf(random.nextInt(99)));
                MessageIntake.route(balanceMessage);
                Thread.sleep(2000);
            } catch (Exception e){
                System.out.println("Error sending balance update for customer: "+testCustomer.getCustomerName()+" "+e.getMessage());
            }
        }
    }

}
