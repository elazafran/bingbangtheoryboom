package com.example.elaza.bingbangtheory;

import java.util.Random;

import static com.example.elaza.bingbangtheory.MainActivity.AMATEUR;
import static com.example.elaza.bingbangtheory.MainActivity.AVANZADO;
import static com.example.elaza.bingbangtheory.MainActivity.PRINCIPIANTE;

/**
 * Created by elaza on 23/11/2017.
 */

public class MotorJuego {
    // Declaración de variables.
    private int casillas;
    private int[][] matriz;
    private boolean[][] pulsadas;

    /**
     * Constructor que le pasamos la dificultad elegida, por defecto es principiante mediante variable en el MainActivity.
     *
     * @param dificultad Nivel de dificultad del juego.
     */
    public MotorJuego(int dificultad) {

        switch (dificultad) {
            case 0:
                casillas = PRINCIPIANTE;
                break;
            case 1:
                casillas = AMATEUR;
                break;
            case 2:
                casillas = AVANZADO;
                break;
        }

        // Creamos una matriz adicional para llevar el control de las celdas pulsadas.
        pulsadas = new boolean[casillas][casillas];
        for (int x = 0; x < casillas; x++) {
            for (int y = 0; y < casillas; y++) {
                pulsadas[x][y] = false;
            }
        }
    }

    /**
     * Distribuye las personajes y las pistas de su ubicación en el tablero.
     */
    public void jugar() {
        ponerPersonajes();
        ponerPistas();
    }

    /**
     * Distribuye las personajes en el tablero de forma aleatoria.
     */
    public void ponerPersonajes() {
        matriz = new int[casillas][casillas];
        int personajes = 0;
        Random rnd = new Random();

        while (personajes < 10) {
            int x = rnd.nextInt(casillas);
            int y = rnd.nextInt(casillas);
            if (matriz[x][y] != -1) {
                matriz[x][y] = -1;
                personajes++;
            }
        }
    }

    /**
     * Rellena las celdas adyacentes a los personajes con pistas de su ubicación.
     *
     */
    private void ponerPistas() {
        for (int x = 0; x < casillas; x++) {
            for (int y = 0; y < casillas; y++) {
                if (matriz[x][y] != -1) {
                    int contador = 0;
                    if ((x - 1 >= 0) && (y - 1 >= 0) && matriz[x - 1][y - 1] == -1) contador++;
                    if ((x - 1 >= 0) && matriz[x - 1][y] == -1) contador++;
                    if ((x - 1 >= 0) && (y + 1 < casillas) && matriz[x - 1][y + 1] == -1)
                        contador++;
                    if ((y - 1 >= 0) && matriz[x][y - 1] == -1) contador++;
                    if ((y + 1 < casillas) && matriz[x][y + 1] == -1) contador++;
                    if ((x + 1 < casillas) && (y - 1 >= 0) && matriz[x + 1][y - 1] == -1)
                        contador++;
                    if ((x + 1 < casillas) && matriz[x + 1][y] == -1) contador++;
                    if ((x + 1 < casillas) && (y + 1 < casillas) && matriz[x + 1][y + 1] == -1)
                        contador++;
                    matriz[x][y] = contador;
                }

            }

        }
    }

    /**
     * Comprueba si la celda existe y devuelve su valor.
     *
     * @param x Fila.
     * @param y Columna.
     * @return El valor de la celda o -2 si no existe.
     */
    public int compruebaCelda(int x, int y) {
        if (x >= 0 && x < casillas && y >= 0 && y < casillas) {
            return matriz[x][y];
        } else {
            // Si no existen las coordenadas devolvemos -2.
            return -2;
        }
    }

    /**
     * Devuelve si la celda está pulsada.
     *
     * @param x Fila.
     * @param y Columna.
     * @return
     */
    public boolean getPulsadas(int x, int y) {

        return pulsadas[x][y];
    }

    /**
     * Marcar una celda como pulsada.
     *
     * @param x Fila.
     * @param y Columna.
     */
    public void setPulsadas(int x, int y) {

        pulsadas[x][y] = true;
    }
}
