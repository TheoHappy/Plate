/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.numberplate;


import kong.unirest.JsonNode;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Iterator;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.io.*;
import java.io.Reader;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import org.json.simple.parser.JSONParser;

import java.lang.String;

/**
 * @author uzair
 */
public class recognize {
    public static void main(String[] args) {
        // Get api key from https://app.platerecognizer.com/start/ and replace MY_API_KEY
        String token = "cb64169b1f0c23fe7b40ab50ddec27a321ceb3d0";
        String file = "/Users/theo/Documents/Java Apps/deep-license-plate-recognition/java/PlateRecognizer/number.jpg";

        try {
            HttpResponse<JsonNode> request = Unirest.post("https://api.platerecognizer.com/v1/plate-reader/")
                    .header("Authorization", "Token " + token)
                    .field("upload", new File(file))
                    .asJson();
            kong.unirest.json.JSONObject body = request.getBody().getObject();
            System.out.println("License plate:");
            try (FileWriter files = new FileWriter("/Users/theo/Documents/Java Apps/deep-license-plate-recognition/java/PlateRecognizer/Plates.json")) {
                files.write(body.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONParser parser = new JSONParser();

            try (Reader reader = new FileReader("/Users/theo/Documents/Java Apps/deep-license-plate-recognition/java/PlateRecognizer/Plates.json")) {

                JSONObject jsonObject = (JSONObject) parser.parse(reader);
                JSONArray res = (JSONArray) jsonObject.get("results");
                String plate = "";
                for (int i = 0; i < res.size(); i++) {
                    JSONObject o = (JSONObject) res.get(i);
                    System.out.println(o.get("plate"));
                    plate = (String) o.get("plate");
                }
                int lastDigit = (int) plate.charAt(plate.length() - 1) - 48;
                Calendar calendar = Calendar.getInstance();
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                //  System.out.println("Last digit:" + lastDigit);
                //  System.out.println("Current day:" + currentDay);
                if (currentDay % 2 == lastDigit % 2) {
                    System.out.println("Free to go");
                } else {
                    System.out.println("Don't use your car today, use public transport");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
