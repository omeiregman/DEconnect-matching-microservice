/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.team2hackathon.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.json.*;

/**
 *
 * @author Oladipo Samuel
 */
@RestController
@RequestMapping("/match")
public class DefaultController {
    
    @GetMapping(value = "getmatch/{id}/byrange/{range}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Object matchById(@PathVariable("id") Integer id, @PathVariable("range") Integer range){
        return runTest(id, range);
    }
    
    public void runInterestDb(int id, int needed){

    }
	
	public String runTest(int userId, int range){
        ArrayList<Integer> userInterest = new ArrayList<>();
           Map<Integer, ArrayList<Integer>> all = new HashMap<>();
           for(int i = 1; i <= 100; i++){
               ArrayList<Integer> interest = new ArrayList<>();
               int rand = (int)(Math.random() * 100) + 1;
               for(int k = 1; k <= rand; k++){
                   if(i == userId) userInterest.add(k);
                   interest.add(k);
               }
               all.put(i, interest);
           }
           InterestMatcher matcher = new InterestMatcher(0, 0, 0, userInterest, new ArrayList<Integer>(new Integer(2)), all);
           Map<Integer, Double> scores = matcher.run(range);
           //scores = matcher.sortMapByValue(scores, "asc");
           JSONObject json = new JSONObject(matcher.run(range));

           scores.forEach((in, ins)->{
               json.put(String.valueOf(in), String.valueOf(ins));
           });
           //System.out.println("
           return json.toString();
	}
}











