/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.team2hackathon.controller;

/**
 *
 * @author Oladipo Samuel
 */
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.*;
import java.time.*;
import java.time.format.*;
import java.text.*;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

public class InterestMatcher {
    private int userId, userCountPrev, userCountAfter;
    private ArrayList<Integer> interests, connections;
    private Map<Integer, ArrayList<Integer>> userInterests;
    private Map<Integer, Double> calcScores;
    private final Logger interestMatcherLogger = Logger.getLogger("com.team2digitalexplorers.interestmatching");
    private final int maxInterest;

    /**
     * The default constructor for the InterestMatcher class
     * @param userId    the users Id
     * @param userCountPrev the total number of available users before update
     * @param userCountAfter the total number of available users after update
     * @param interests     the array list of interests of the user. Each interest is represented by their numerical values
     * @param connections   array list of users that this user has connected to
     * @param userInterests a map of all users containing their user_id as key and array list of integer values as values
     */
    public InterestMatcher(int userId, int userCountPrev, int userCountAfter, ArrayList<Integer> interests, ArrayList<Integer> connections, Map<Integer, ArrayList<Integer>> userInterests, int maxInterest){
        this.userId = userId;
        this.userCountPrev = userCountPrev;
        this.userCountAfter = userCountAfter;
        this.interests = interests;
        this.connections = connections;
        this.userInterests = userInterests;
        this.calcScores = new HashMap<>();
        this.maxInterest = maxInterest;


        //Initialize the logger
        this.interestMatcherLogger.setLevel(Level.ALL);
        this.interestMatcherLogger.setUseParentHandlers(false);
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        this.interestMatcherLogger.addHandler(handler);
    }

    public Map<Integer, Double> run(int needed){
        calcUserInterestScores();
        return getMatches(this.calcScores, needed);
    }

    public Map<Integer, Double> getMatches(Map<Integer, Double> scores, int needed){
        Map<Integer, Double> topScores = new LinkedHashMap<>();

        topScores = scores
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .limit(needed)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2)-> e2, LinkedHashMap::new));


        return topScores;
    }

    public /*Map<Integer, Integer>*/void calcUserInterestScores(){
        //Firstly check if user id is present in the map of other users, if exist, remove user information
        if(this.userInterests.containsKey(this.userId)) this.userInterests.remove(this.userId);

        int userInterestLength = this.interests.size();

        Map<Integer, ArrayList<Integer>> filteredUsers = filterConnections(this.userInterests);

        filteredUsers.forEach((userId, userInterest)->{
            int interestLength = userInterest.size();
            double score;

            if(userInterestLength > interestLength){
                score = calculateScore(this.interests, userInterest, "User");
            }else{
                score = calculateScore(userInterest, this.interests, "Global");
            }

            this.calcScores.put(userId, score);
        });

        //return this.calcScores;
    }

    public double calculateScore(ArrayList<Integer> arListL, ArrayList<Integer> arListS, String state){
        double score = 0;
        ArrayList<Integer> temp = new ArrayList<>();

        arListS.forEach(item->{
            if(arListL.contains(item)){
                temp.add(item);
            }
        });

        if(state.equalsIgnoreCase("User")) score = ((double)temp.size() / (double)arListL.size()) * (double)arListL.size();
        else if(state.equalsIgnoreCase("Global"))  score = ((double)temp.size() / (double)arListS.size()) * (double)arListL.size();

        score = (score/this.maxInterest) * 100;   //Convert to percent, where "maxInterest" is the maximum number of interest a person can have

        DecimalFormat format = new DecimalFormat("###.##");
        format.setRoundingMode(RoundingMode.UP);
        return Double.parseDouble(format.format(score));
    }

    /**
     * This function removes all previous connections of the user from the list of all users
     * @return true if connections were available and removed else returns false
     */
    public Map<Integer, ArrayList<Integer>> filterConnections(Map<Integer, ArrayList<Integer>> userInterests){
        ZonedDateTime now;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);

        if(this.connections.size() != 0){
            now = ZonedDateTime.now();
            this.interestMatcherLogger.info("A total number" + this.connections.size() + " of user connections were removed. Time: " + formatter.format(now));

            for (Integer connection : this.connections) {
                userInterests.remove(connection);
            }
        }else{
            now = ZonedDateTime.now();
            this.interestMatcherLogger.info("No user connections exist for this user. Time: " + formatter.format(now));
        }

        return userInterests;
    }
}