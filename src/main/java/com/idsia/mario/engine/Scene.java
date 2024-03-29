package com.idsia.mario.engine;

import java.awt.Graphics;

import com.idsia.mario.engine.level.Level;


public abstract class Scene
{
    public static boolean[] keys = new boolean[16];
    public static final String[] keysStr = {"LEFT  ", "RIGHT ", " DOWN ", " JUMP ", " SPEED"};    

//    public void toggleKey(int key, boolean isPressed)
//    {
//        keys[key] = isPressed;
//    }

    public abstract void init();
    
    public abstract void init(Level level);

    public abstract void tick();

    public abstract void render(Graphics og, float alpha);
}