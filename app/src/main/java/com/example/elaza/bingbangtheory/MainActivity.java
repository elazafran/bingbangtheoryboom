package com.example.elaza.bingbangtheory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements SelecPersonajeDialogFragment.RespSeleccPersonaje,
        SelecDificultadDialogFragment.RespuestaDificultad, View.OnClickListener, View.OnLongClickListener {

    // Declaración de variables
    public static final int PRINCIPIANTE = 8;
    public static final int AMATEUR = 10;
    public static final int AVANZADO = 12;
    Button tiledBoton;
    TableLayout tableLayout;
    LinearLayout main;
    MotorJuego motorJuego;
    boolean jugando = false;
    TypedArray arrayImagenes;
    int encontradas = 0;
    private int personaje = 0;
    private int dificultad = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        arrayImagenes = getResources().obtainTypedArray(R.array.imagenes);
        main = (LinearLayout) findViewById(R.id.content_main);

        //opción por defecto
        rellenaBotones(PRINCIPIANTE);
    }

    /**
     * Genera la matriz de botones inicial.
     *
     * @param botones Número de botones de acuerdo al nivel de dificultad.
     */

    private void rellenaBotones(int botones) {

        tableLayout = new TableLayout(this);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        tableLayout.setWeightSum(botones);

        for (int i = 0; i < botones; i++) {
            TableRow tr = new TableRow(this);
            tr.setGravity(Gravity.CENTER);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));

            for (int j = 0; j < botones; j++) {
                tiledBoton = new Button(this);
                tiledBoton.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                tiledBoton.setId(View.generateViewId());
                tiledBoton.setText(i + "," + j);
                tiledBoton.setTextSize(0);
                tiledBoton.setOnClickListener(this);
                tiledBoton.setOnLongClickListener(this);
                tr.addView(tiledBoton);
            }
            tableLayout.addView(tr);
        }
        main.removeAllViews();
        main.addView(tableLayout);

        if (!jugando) deshabilitaTablero(tableLayout);
    }

    /**
     * Inflamos el menú; esto agregará los elementos a la barra de acción
     * 
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Manejamos los clicks de cada elemento que se pulsa
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar los clics del elemento de la barra de acción aquí.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Muestra el cuadro de diálogo con las instrucciones del juego.
     *
     * @param item Elemento del menú asociado al método.
     */
    public void showInstrucciones(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.texto_instrucciones)
                .setTitle(R.string.titulo_instrucciones)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Vamos a Juagar entonces!!", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Muestra el cuadro de diálogo para la selección del personaje.
     *
     * @param item Elemento del menú asociado al método.
     */
    public void showSeleccionPersonaje(MenuItem item) {
        SelecPersonajeDialogFragment selecPersonaje = new SelecPersonajeDialogFragment();
        selecPersonaje.show(getFragmentManager(), null);
    }

    /**
     * Método que implementa la interfaz para el diálogo de selección de personaje.
     *
     * @param i Entero asociado al recurso de imagen del personaje.
     */
    @Override
    public void onPersonajeSeleccionado(int i) {
        personaje = i;
    }

    /**
     * Muestra el cuadro de diálogo de selección de dificultad.
     *
     * @param item Elemento del menú asociado al método.
     */

    public void showSeleccionDificultad(MenuItem item) {
        SelecDificultadDialogFragment selecDificultad = new SelecDificultadDialogFragment();
        selecDificultad.show(getFragmentManager(), null);
    }

    /**
     * Método que implementa la interfaz para el diálogo de elección de la dificultad. <br>
     * <ul>
     *     <li>PRINCIPIANTE - 8 casillas.</li>
     *     <li>AMATEUR - 10 casillas.</li>
     *     <li>AVANZADO - 12 casillas.</li>
     * </ul>
     *
     * @param i Entero asociado al nivel de dificultad.
     */
    @Override
    public void onRespuestaDificultad(int i) {
        dificultad = i;
        switch (dificultad) {
            case 0:
                rellenaBotones(PRINCIPIANTE);
                jugando = false;
                break;
            case 1:
                rellenaBotones(AMATEUR);
                jugando = false;
                break;
            case 2:
                rellenaBotones(AVANZADO);
                jugando = false;
                break;
        }
    }

    /**
     * Controloamos através de la interfaz OnClickListener los eventos del activity
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        // Obtenemos las coordenadas de la celda del texto del botón
        int x = Integer.parseInt(((Button) view).getText().toString().split(",")[0]);
        int y = Integer.parseInt(((Button) view).getText().toString().split(",")[1]);

        int resultado = motorJuego.compruebaCelda(x, y);

        if (resultado == -1) { // Hay personaje

            // Mostrar personaje muerto
            Button b = (Button) view;
            b.setPadding(0, 0, 0, 0);
            b.setBackgroundResource(R.drawable.bomba);
            mostrarAlerta(R.string.hayBomba);

            // Fin juego
            TableLayout tl = (TableLayout) view.getParent().getParent();
            jugando = false;
            encontradas = 0;
            deshabilitaTablero(tl);
        }
        if (resultado == 0) { // No hay personajes adyacentes
            Button b = (Button) view;
            b.setPadding(0, 0, 0, 0);
            b.setBackgroundColor(Color.GRAY);
            // Despejar adyacentes con 0
            despejaAdyacentes(view, x, y);
        }
        if (resultado > 0) { // Hay personajes adyacentes
            Button b = (Button) view;
            b.setPadding(0, 0, 0, 0);
            b.setText(String.valueOf(resultado));
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            b.setTextColor(Color.WHITE);
            b.setBackgroundColor(Color.GRAY);
            view.setEnabled(false);
        }
    }

    /**
     * Método que descubre automáticamente todas las casillas adyacentes que no tienen personajes
     * alrededor.
     *
     * @param view El botón a descubrir.
     * @param x    Fila.
     * @param y    Columna.
     */
    private void despejaAdyacentes(View view, int x, int y) {
        // Recorremos los botones adyacentes y si también están a cero los despejamos
        for (int xt = -1; xt <= 1; xt++) {
            for (int yt = -1; yt <= 1; yt++) {
                if (xt != yt) {
                    if (motorJuego.compruebaCelda(x + xt, y + yt) == 0 && !motorJuego.getPulsadas(x + xt, y + yt)) {
                        Button b = (Button) traerBoton(x + xt, y + yt);
                        b.setBackgroundColor(Color.GRAY);
                        b.setClickable(false);
                        motorJuego.setPulsadas(x + xt, y + yt);
                        String[] coordenadas = b.getText().toString().split(",");
                        despejaAdyacentes(b, Integer.parseInt(coordenadas[0]),
                                Integer.parseInt(coordenadas[1]));
                    }
                }
            }
        }

    }

    /**
     * Recupera una vista de botón desde las coordenadas indicadas.
     *
     * @param x Fila.
     * @param y Columna.
     * @return Devuelve el botón buscado o null si no lo encuentra.
     */
    private View traerBoton(int x, int y) {
        Button b = null;
        // Recorremos la matriz de botones hasta encontrar una coincidencia con las coordenadas
        // buscadas.
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow tr = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < tr.getChildCount(); j++) {
                b = (Button) tr.getChildAt(j);
                if (b.getText().toString().equals(x + "," + y)) {
                    return b;
                }
            }
        }
        return null;
    }

    /**
     * Deshabilita todos los botones de la matriz cuando no estamos jugando, cuando haya errado o el juego ha terminado.
     *
     * @param view El elemento a deshabilitar.
     */
    private void deshabilitaTablero(View view) {
        TableLayout tl = (TableLayout) view;
        // Recorremos la matriz de botones deshabilitando todos.
        for (int i = 0; i < tl.getChildCount(); i++) {
            TableRow tr = (TableRow) tl.getChildAt(i);
            for (int j = 0; j < tr.getChildCount(); j++) {
                Button b = (Button) tr.getChildAt(j);
                b.setEnabled(false);
            }
        }
    }

    /**
     * Inicia el juego desde el menú correspondiente.
     *
     * @param item Elemento del menú asociado al método.
     */
    public void empezarJuego(MenuItem item) {
        jugando = true;
        onRespuestaDificultad(dificultad);
        motorJuego = new MotorJuego(dificultad);
        motorJuego.jugar();
    }


    @Override
    public boolean onLongClick(View view) {

        // Obtenemos las coordenadas de la celda del texto del botón
        int x = Integer.parseInt(((Button) view).getText().toString().split(",")[0]);
        int y = Integer.parseInt(((Button) view).getText().toString().split(",")[1]);

        int resultado = motorJuego.compruebaCelda(x, y);
        if (resultado == -1) { // Hay personaje en la casilla
            Button b = (Button) view;
            b.setPadding(0, 0, 0, 0);
            b.setBackground(arrayImagenes.getDrawable(personaje));
            mostrarAlerta(R.string.haEncontradoPJ);
            encontradas++;
            if (encontradas == 10) {
                TableLayout tl = (TableLayout) view.getParent().getParent();
                jugando = false;
                encontradas = 0;
                mostrarAlerta(R.string.ganador);
                deshabilitaTablero(tl);
            }
        } else { // No hay personaje en la casilla
            Button b = (Button) view;
            b.setText(String.valueOf(resultado));
            b.setTextSize(20);
            b.setTextColor(Color.WHITE);
            b.setBackgroundColor(Color.GRAY);
            view.setEnabled(false);
            TableLayout tl = (TableLayout) view.getParent().getParent();
            jugando = false;
            mostrarAlerta(R.string.perdedor);
            deshabilitaTablero(tl);
        }

        return true;
    }

    /**
     * Muestra un cuadro de diálogo de alerta con el texto indicado.
     *
     * @param texto Entero indicado el id del recurso de texto asociado.
     */
    private void mostrarAlerta(int texto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(texto)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // El usuario pulsa OK.

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
