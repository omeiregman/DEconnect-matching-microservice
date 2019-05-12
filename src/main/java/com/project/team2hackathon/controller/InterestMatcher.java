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
import java.util.*;
import java.util.logging.*;
import java.time.*;
import java.time.format.*;

public class InterestMatcher {
    private int userId, userCountPrev, userCountAfter;
    private ArrayList<Integer> interests, connections;
    private Map<Integer, ArrayList<Integer>> userInterests;
    private Map<Integer, Double> calcScores;
    private static Logger interestMatcherLogger = Logger.getLogger("com.team2digitalexplorers.interestmatching");
    
    /**
     * The default constructor for the InterestMatcher class
     * @param userId    the users Id    
     * @param userCountPrev the total number of available users before update
     * @param userCountAfter the total number of available users after update
     * @param interests     the array list of interests of the user. Each interest is represented by their numerical values
     * @param connections   array list of users that this user has connected to
     * @param userInterests a map of all users containing their user_id as key and array list of integer values as values
     */
    public InterestMatcher(int userId, int userCountPrev, int userCountAfter, ArrayList<Integer> interests, ArrayList<Integer> connections, Map<Integer, ArrayList<Integer>> userInterests){
        this.userId = userId;
        this.userCountPrev = userCountPrev;
        this.userCountAfter = userCountAfter;
        this.interests = interests;
        this.connections = connections; 
        this.userInterests = userInterests;
        this.calcScores = new TreeMap<>();
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
        Map<Integer, Double> topScores = new TreeMap<>();
        ArrayList<String> temp= new ArrayList<>();
        
        scores.forEach((userId, score)->{
            temp.add(String.valueOf(score).concat(" ").concat(String.valueOf(userId))); 
        });
        
        //temp.sort(Comparator.reverseOrder()); 
        temp.sort(Comparator.naturalOrder()); 
        for(int i = 0; i < needed; i++){
            String all = temp.get(i);
            String[] info = all.split("\\s");
            topScores.put(Integer.parseInt(info[1]), Double.parseDouble(info[0]));
        }
        
        return topScores;
    }
    
    public /*Map<Integer, Integer>*/void calcUserInterestScores(){
        //ArrayList<Integer> otherUsersInterests;
        int userInterestLength = this.interests.size();
        
        Map<Integer, ArrayList<Integer>> filteredUsers = filterConnections(this.userInterests);
        
        filteredUsers.forEach((userId, userInterest)->{
            int interestLength = userInterest.size();
            double score = 0.0;
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
        
        arListL.forEach(item->{
            if(arListS.contains(item)){
                temp.add(item);
            }
        });
        
        if(state.equalsIgnoreCase("User")){
            score = ((double)temp.size() / (double)arListL.size()) * (double)arListL.size(); 
        }else if(state.equalsIgnoreCase("Global")){
            score = ((double)temp.size() / (double)arListS.size()) * (double)arListL.size();
        }
        
        score = (score/(double)arListL.size()) * 100;   //Convert to percent
        return score;
    }
    
    /**
     * This function removes all previous connections of the user from the list of all users
     * @return true if connections were available and removed else returns false
     */
    public Map<Integer, ArrayList<Integer>> filterConnections(Map<Integer, ArrayList<Integer>> userInterests){
        ZonedDateTime now;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
        System.out.println(this.connections.size());
        if(this.connections.size() != 0){
            now = ZonedDateTime.now();
            this.interestMatcherLogger.info("A total number" + this.connections.size() + " of user connections were removed. Time: " + formatter.format(now));
            System.out.print("here");
            for(int i = 0; i < this.connections.size(); i++){
                userInterests.remove(this.connections.get(i)); 
            }
        }else{
            now = ZonedDateTime.now();
            this.interestMatcherLogger.info("No user connections exist for this user. Time: " + formatter.format(now));
        }
        
        return userInterests;
    }
    
    public Map<Integer, Double> sortMapByValue(Map<Integer, Double> map, String mode){
        Map<Integer, Double> finalM = new TreeMap<>();
        ArrayList<String> temp= new ArrayList<>();
        
        map.forEach((arg1, arg2)->{
            temp.add(String.valueOf(arg2).concat(" ").concat(String.valueOf(arg1))); 
        });
        
        //temp.sort(Comparator.reverseOrder()); 
        if(mode.equalsIgnoreCase("asc")){
            temp.sort(Comparator.reverseOrder()); 
        }else if(mode.equalsIgnoreCase("desc")){
            temp.sort(Comparator.naturalOrder()); 
        }else{
            temp.sort(Comparator.naturalOrder()); 
        }
        
        for(int i = 0; i < temp.size(); i++){
            String all = temp.get(i);
            String[] info = all.split("\\s");
            System.out.println(all);
            finalM.put(Integer.parseInt(info[1]), Double.parseDouble(info[0]));
        }
        
        return finalM;
    }
}




