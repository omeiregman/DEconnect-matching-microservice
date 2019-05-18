/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.team2hackathon.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.team2hackathon.model.FetchInterest;

import org.json.*;

/**
 *
 * @author Oladipo Samuel
 */
@RestController
@RequestMapping("/match")
public class DefaultController {
    
    @GetMapping(value = "getmatch/{id}/byrange/{range}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Object matchById(@PathVariable("id") Integer id, @PathVariable("range") Integer range)throws SQLException, IOException{
        return runTest(id, range, 30);
        //return runInterestDb(id, range, 30);
    }

    public String runInterestDb(int userId, int range, int maxInterest)throws SQLException, IOException {
        FetchInterest fetchUser = new FetchInterest(userId);
        String userInterestQuery = "select interests from user_interests where id = " + userId,
                allOtherInterestQuery = "select userId, interests from user_interests where id != " + userId;

        ArrayList <Integer> userInterest = fetchUser.fetchUserInterest(fetchUser.getQuery(userInterestQuery));
        Map<Integer, ArrayList<Integer>> allInterest = fetchUser.fetchAllUserInterests(fetchUser.getQuery(allOtherInterestQuery));

        InterestMatcher matcher = new InterestMatcher(userId, 0, 0, userInterest, new ArrayList<Integer>(new Integer(2)), allInterest, maxInterest);

        //Map<Integer, Double> scores = matcher.run(range);
        //scores = matcher.sortMapByValue(scores, "asc");
        JSONObject json = new JSONObject(matcher.run(range));

        return json.toString();
    }

    public String runTest(int userId, int range, int maxInterest){
        ArrayList<Integer> userInterest = new ArrayList<>();
        Map<Integer, ArrayList<Integer>> all = new HashMap<>();
        for(int i = 1; i <= 100; i++){
            ArrayList<Integer> interest = new ArrayList<>();
            int interestRand = (int)(Math.random() * maxInterest) + 1;

            for(int k = 1; k <= interestRand; k++){
                int rand = (int)(Math.random() * maxInterest) + 1;
                //System.out.println(rand);
                //System.out.println("rand: " + i + ": " + rand);
                if(i == userId) userInterest.add(rand);
                interest.add(rand);
            }
            all.put(i, interest);
        }
        InterestMatcher matcher = new InterestMatcher(userId, 0, 0, userInterest, new ArrayList<Integer>(new Integer(2)), all, maxInterest);
        //Map<Integer, Double> scores = matcher.run(range);
        //scores = matcher.sortMapByValue(scores, "asc");
        JSONObject json = new JSONObject(matcher.run(range));

        return json.toString();
    }
}















