/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idsia.scenarios;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.idsia.mario.engine.level.Level;
import com.idsia.mario.engine.level.LevelParser;
import com.reader.JsonReader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vv
 */
public class GANEvaluator {
    public static void main(String[] args){
        String filename = "sample_381.json";
        JsonReader reader = new JsonReader(filename);
        List<List<Integer>> input = reader.next();
        LevelParser parser = new LevelParser();
        Level level = parser.createLevelJson(input);
    }
    
  
}
