package com.getsimplex.steptimer.service;

import com.getsimplex.steptimer.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationDataDriver {

    private static String[] lastNames = {"Jones", "Smith", "Ahmed", "Wu", "Doshi", "Anandh", "Clayton", "Harris", "Gonzalez", "Abram", "Khatib", "Clark", "Mitra", "Habschied", "Jackson", "Phillips", "Lincoln", "Spencer", "Anderson", "Hansen", "Davis", "Jones", "Fibonnaci", "Staples", "Jefferson", "Huey", "Olson", "Howard", "Sanchez", "Aristotle"};
    private static String[] firstNames = {"Sarah", "Bobby", "Frank", "Edward", "Danny", "Chris", "Spencer", "Ashley", "Santosh", "Senthil", "Christina", "Suresh", "Neeraj", "Angie", "Sean", "Lyn", "John", "Ben", "Travis", "David", "Larry", "Jerry", "Gail", "Craig", "Dan", "Jason", "Eric", "Trevor", "Jane", "Jacob", "Jaya", "Manoj", "Liz", "Christina"};
    private static List<User> testUsers = new ArrayList<User>();
    private static Random random = new Random();


    public static synchronized void generateTestCustomers(int numberOfUsers){
        for (int i=0;i<numberOfUsers-1){
            Customer testUser = new User();
            testUser.setLocked(false);
            testUser.setUserName(firstNames[random.nextInt(numberOfUsers)]+"."+lastNames[random.nextInt(numberOfUsers)]);
            testUser.setAccountType("personal");
            testUser.
            testUsers.add(testUser);

        }

    }
}
