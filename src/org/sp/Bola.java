package org.sp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hp
 */
public class Bola {

    private int x;
    private int y;
    private double dx;
    private double dy;
    private int tam;

    public Bola() {
        x = (int) (Math.random() * 700 + 50);
        y = 30;
        dx = 5;
        dy = 0;
        tam = 40;
    }

    public Bola(int xI, int yI, double dxI, int tamI) {
        x = xI;
        y = yI;
        dx = dxI;
        tam = tamI;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public int getTam() {
        return tam;
    }

    public void mover(int suelo[]) {
        int dif;
        x += dx;
        y += dy;
        dy += 0.8;
        dif = x + tam - 799;
        if (dif >= 0&& dx>0) {
            //x = 799 - dif;
            x =799-tam;  
            dx=-dx;
        }
        dif = x - tam;
        if (dif < 0&&dx<=0) {
//            x = -dif;
            x=tam;
            dx=-dx;
        }
        dif = y + tam - suelo[x];

        if (dif > 0&& dy>0) {
//            y = suelo[x] - dif;
            y=suelo[x]-tam;
            if (tam > 20) {
                dy = -20;
            } else if (tam > 10) {
                dy = -18;
            } else {
                dy = -15;
            }
        }

    }
}
