package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import constants.Constants;
import graphics.Assets;
import input.KeyBoard;
import input.MouseInput;
import input.Timer;
import states.LoadingState;
import states.State;


/**
 * Crea una instancia de ventana en donde se crea y corre el juego,
 * contiene el metodo main
 */
public class Window extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    private final Canvas canvas;
    private Thread thread;
    private boolean running = false;

    private final int FPS = 60;
    private double delta = 0;
    private int AVERAGEFPS = FPS;

    private final KeyBoard keyBoard;
    private final MouseInput mouseInput;

    public static Timer mouseTimer;
    public static Timer keyBoardTimer;

    public Window() {
        /*
          JFrame Config
         */
        setTitle("Space Ship Game");
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        /*
            Canvas Config, Input Listeners
         */
        canvas = new Canvas();
        keyBoard = new KeyBoard();
        mouseInput = new MouseInput();
        mouseTimer = new Timer(Constants.CLICK_TIMEOUT,mouseInput);
        keyBoardTimer = new Timer(Constants.CLICK_TIMEOUT,keyBoard);

        canvas.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        canvas.setMaximumSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        canvas.setMinimumSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        canvas.setFocusable(true);

        add(canvas);
        canvas.addKeyListener(keyBoard);
        canvas.addMouseListener(mouseInput);
        canvas.addMouseMotionListener(mouseInput);
        setVisible(true);
    }


    /**
     * Inicia el hilo de Window
     *
     * @param args no se reciben argumentos
     */
    public static void main(String[] args) {
        new Window().start();
    }


    /**
     * Actualiza las clases necesarias,
     * State y Keyboard
     *
     * @param dt tiempo desde el ultimo update en ns
     */
    private void update(float dt) {
        if (dt > 40) dt = 40;
        keyBoard.update();
        mouseTimer.update(dt);
        keyBoardTimer.update(dt);
        State.getCurrentState().update(dt);
    }

    /**
     * Dibuja el Contenido de la ventana
     */
    private void draw() {
        /*
            Config
         */
        BufferStrategy bs = canvas.getBufferStrategy();

        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        /*
            ------------------- 	CONTENIDO       ---------------------------
         */
        g.setColor(Color.BLACK);

        g.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

        State.getCurrentState().draw(g);

        g.setColor(Color.WHITE);

        g.setFont(Assets.fontMed);

        g.drawString(" " + AVERAGEFPS, 10, 20);


        /*
            ------------------- 	FIN DE CONTENIDO      ---------------------------
         */

        g.dispose();
        bs.show();
    }

    /**
     * Inicializa los recursos del juego de manera asincrona,
     * inicia en estado de carga, LoadingState
     */
    private void init() {

        Thread loadingThread = new Thread(Assets::init);

        State.changeState(new LoadingState(loadingThread));
    }


    /**
     * Se inicializa y corre el juego a una determinada
     * cantidad de FPS
     */
    @Override
    public void run() {

        long now;
        long lastTime = System.nanoTime();
        int frames = 0;
        long time = 0;

        init();

        /*
             ------------------------ GAME LOOP -------------------------------
         */
        while (running) {
            // control de tiempo y frames
            now = System.nanoTime();
            double TARGETTIME = (double) 1000000000 / FPS;
            delta += (now - lastTime) / TARGETTIME;
            time += (now - lastTime);
            lastTime = now;

            // solo actualizar despues de cierta cantidad de tiempo
            if (delta >= 1) {
                // actualizar proximo frame se pasa el tiempo en milisegundos
                update((float) (delta * TARGETTIME * 0.000001f));

                // dibujar
                draw();

                delta--;
                frames++;
            }
            if (time >= 1000000000) {

                AVERAGEFPS = frames;
                frames = 0;
                time = 0;

            }


        }

        /*
             ------------------------ FIN DE GAME LOOP -------------------------------
         */
        stop();
    }

    private void start() {

        thread = new Thread(this);
        thread.start();
        running = true;
    }

    private void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

