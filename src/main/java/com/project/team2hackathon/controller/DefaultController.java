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

import com.project.team2hackathon.InterestMatcher;
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
        return interestMatchRun(id, range);
    }
    
    public String interestMatchRun(int id, int needed){
        ArrayList<Integer> userInterest = new ArrayList<>(); 
           ArrayList<Integer> allInterest = new ArrayList<>(); 
           ArrayList<Integer> allInterest2 = new ArrayList<>(); 
           ArrayList<Integer> allInterest3 = new ArrayList<>(); 
           Map<Integer, ArrayList<Integer>> all = new HashMap<>();
           for(int i = 0; i < 97; i++){
               userInterest.add(new Integer(i));
               if(i >= 89){
                   allInterest.add(new Integer(i));
               }
               if(i >= 65){
                   allInterest2.add(new Integer(i));
               }
               if(i >= 64){
                   allInterest3.add(new Integer(i));
               }
           }
           all.put(new Integer(id), allInterest);
           all.put(new Integer(2), allInterest2);
           all.put(new Integer(44), allInterest3);
           InterestMatcher matcher = new InterestMatcher(0, 0, 0, userInterest, new ArrayList<Integer>(new Integer(2)), all);
           Map<Integer, Double> scores = matcher.run(needed);
           //scores = matcher.sortMapByValue(scores, "asc");
           JSONObject json = new JSONObject(matcher.run(needed));
           String str = "";
           scores.forEach((in, ins)->{
               json.put(String.valueOf(in), String.valueOf(ins));
           });
           //System.out.println("Final string: " + str);
           return json.toString();
    }
}











