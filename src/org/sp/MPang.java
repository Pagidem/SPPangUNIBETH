package org.sp;

import java.util.*;

/**
 *
 * @author Gj
 */


public class MPang {
    //Variables para el uso de teclas

    private int TEC_ESPACIO = 32;
    private int TEC_LEFT = 37;
    private int TEC_UP = 38;
    private int TEC_RIGHT = 39;
    private int TEC_ENTER = 10;
    //Variables que determinan el estado y la fase
    private int nEstado = 0;
    private int nFase = 0;
    private Hashtable htTeclas = new Hashtable();
    //Variables para determinar la ubicacion de dibujo
    private int y[] = new int[800];
    private float ang[] = new float[800];
    private int x = 400;
    private ArrayList<Bola> bolas = new ArrayList();
    //Variables para el uso del la linea que sera el disparo
    private int xDisparo = 0;
    private float angDisparo = 0;
    private int disDisparo = 0;

    //Getters
    public int getEstado() {
        return nEstado;
    }

    public int getFase() {
        return nFase;
    }

    public int[] getY() {
        return y;
    }

    public float[] getAng() {
        return ang;
    }

    public int getX() {
        return x;
    }

    public int getXDisparo() {
        return xDisparo;
    }

    public float getAngDisparo() {
        return angDisparo;
    }

    public int getDisDisparo() {
        return disDisparo;
    }
    
    public int[] getYSuelo() {
        return y;
    }
    
    public float[] getAnguloSuelo(){
        return ang;
    }
    
    

    //Metodo que se efectua al realizar la presion de una tecla
    public void pulsaTecla(int codigo) {
        try {
            htTeclas.put(codigo, 1);
        } catch (Exception e) {
        }
    }

    public void sueltaTecla(int codigo) {
        try {
            htTeclas.remove(codigo);
        } catch (Exception e) {
        }
    }

    //Condiciones que determinan las acciones del juego
    public void acciones() {
        switch (nEstado) {
            case 0:
                accionesEstado0();
                break;
            case 1:
                accionesEstado1();
                break;
            case 2:
                accionesEstado2();
                break;
        }
    }

    public void accionesEstado0() {
        if (htTeclas.containsKey(TEC_ENTER)) {
            nEstado = 1;
            nFase = 0;
            crearPantalla();
        }
    }

    public void accionesEstado1() {
        if (htTeclas.containsKey(TEC_LEFT)) {
            x = Math.max(x - 10, 20);
        }
        if (htTeclas.containsKey(TEC_RIGHT)) {
            x = Math.min(x + 10, 780);
        }
        if (htTeclas.containsKey(TEC_ENTER)) {
            nFase++;
            crearPantalla();
        }
        if (htTeclas.containsKey(TEC_ESPACIO) || htTeclas.containsKey(TEC_UP)) {
            if (disDisparo == 0) {
                disDisparo = 1;
                xDisparo = x;
                angDisparo = (float) (ang[x] - Math.PI / 2);
            }
        }
    }

    public void accionesEstado2() {
        if (htTeclas.containsKey(TEC_ESPACIO)) {
            nEstado = 0;
        }
    }

    public ArrayList<Bola> getBolas() {
        return bolas;
    }
    //Dibuja la pantalla con los puntos dados.

    public void crearPantalla() {
        double yI = Math.random() * 200 + 350;
        double angI = (float) (Math.random() * 0.5 - 0.25);
        for (int n = 0; n < 800; n++) {
            y[n] = (int) yI;
            ang[n] = (float) angI;
            angI = angI + Math.random() * 0.1 - 0.05;
            angI = Math.max(angI, -0.25);
            angI = Math.min(angI, 0.25);
            yI = yI + Math.sin(angI);
            yI = Math.min(yI, 600);
        }

        bolas.clear();
        for (int n = 0; n <= nFase; n++) {
            bolas.add(new Bola());
        }

    }

    public int distancia(int x, int y, int x2, int y2) {
        return (int) (Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2)));
    }
    //Metodo para que el disparo divida las pelotas.

    public int detectarColisiones() {
        int res = 0;
        if (nEstado != 1) {
            return res;
        }
        for (int n = bolas.size() - 1; n >= 0; n--) {
            Bola b = bolas.get(n);
            if (distancia(b.getX(), b.getY(), x, y[x]) < b.getTam() + 25) {
                nEstado = 2;
            }
            int xDis = xDisparo;
            int yDis = y[xDis];
            boolean bEliminarBola = false;
            for (int m = 0; m < disDisparo && !bEliminarBola; m += 5) {
                xDis = (int) (xDisparo + Math.cos(angDisparo) * m);
                yDis = (int) (y[xDisparo] + Math.sin(angDisparo) * m);
                if (distancia(b.getX(), b.getY(), xDis, yDis) < b.getTam() + 2) {
                    bEliminarBola = true;
                    disDisparo = 0;
                }
            }
            if (bEliminarBola) {
                int tam = b.getTam();
                if (tam > 10) {
                    tam = tam / 2;
                    bolas.add(new Bola(b.getX(), b.getY(), 5, tam));
                    bolas.add(new Bola(b.getX(), b.getY(), -5, tam));
                }
                bolas.remove(n);
                if (bolas.isEmpty()) {
                    nFase++;
                    crearPantalla();
                }
            }
        }
        return res;
    }

    //Por ultimo ejecutar todo en un solo formulario.
    public void ejecutarFrame() {
        acciones();
        if (nEstado == 1) {
            for (Bola b : bolas) {
                b.mover(y);
            }
            if (disDisparo > 0) {
                disDisparo += 100;
                if (Math.sin(angDisparo) * disDisparo + y[xDisparo] < 0) {
                    disDisparo = 0;
                }
            }
            detectarColisiones();
        }

    }

    
}
