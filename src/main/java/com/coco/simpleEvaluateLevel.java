/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coco;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.basic.map.Settings;
import com.cmatest.MarioEvalFunction;
import com.idsia.mario.engine.level.Level;
import com.idsia.mario.engine.level.LevelParser;
import com.reader.JsonReader;

import static com.reader.JsonReader.JsonToDoubleArray;
import static com.viewer.MarioRandomLevelViewer.randomUniformDoubleArray;

/**
 *
 * @author vv
 */
public class simpleEvaluateLevel {

	public static final int BLOCK_SIZE = 16;
	public static final int LEVEL_HEIGHT = 14;	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Settings.setPythonProgram();
	MarioEvalFunction eval = new MarioEvalFunction();
        int dim=32;
        
        double[] latentVector = randomUniformDoubleArray(dim);
        double result = eval.valueOf(latentVector);
        System.out.println(result);
        eval.exit();
        System.exit(0);
    }
    
}
