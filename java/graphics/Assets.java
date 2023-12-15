package graphics;

import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

/**
 * Esta clase sirve para cargar los recursos,como son las imagenes(de la nave,de propulsores,de lasers
 * ,meteoros,explosion,enemigos,fuentes de texto,sonidos). ademas sirve para contar cuantos recursos
 * se han cargado.
 */
public class Assets {

    public static boolean loaded = false;
    public static float count = 0;
    public static float MAX_COUNT = 57+20;


    public static BufferedImage player;
    public static BufferedImage doubleGunPlayer;

    // effects

    public static BufferedImage speed;

    public static BufferedImage[] shieldEffect = new BufferedImage[3];
    //Fondos
    public static BufferedImage fondoMenu;

    public static BufferedImage fondoInstrucions;

    public static BufferedImage fondoName;

    public static BufferedImage fondoScore;

    public static BufferedImage fondoPause;
    //Fondo mapa 1x1
    public static BufferedImage fondoGame;

    //Fondo mapa 2x2
    public static BufferedImage[][] fondoGame2x2 = new BufferedImage[2][2];

    //Fondo mapa 3x3
    public static BufferedImage[][] fondoGame3x3 = new BufferedImage[3][3];
    // explosion

    public static BufferedImage[] exp = new BufferedImage[9];

    // lasers

    public static BufferedImage blueLaser, greenLaser, redLaser;

    // Meteors

    public static BufferedImage[] bigs = new BufferedImage[4];
    public static BufferedImage[] meds = new BufferedImage[2];
    public static BufferedImage[] smalls = new BufferedImage[2];
    public static BufferedImage[] tinies = new BufferedImage[2];

    // ufo

    public static BufferedImage ufo;

    // numbers

    public static BufferedImage[] numbers = new BufferedImage[11];

    public static BufferedImage life;

    // fonts

    public static Font fontBig;
    public static Font fontMed;

    public static Clip backgroundMusic, explosion, playerLoose, playerShoot, ufoShoot, powerUp;

    // ui

    public static BufferedImage blueBtn;
    public static BufferedImage greyBtn;

    public static BufferedImage instructions;

    // power ups

    public static BufferedImage orb, doubleScore, doubleGun, fastFire, shield, star;

    /**
     * cargamos las imagenes/fuentes que provienen de los directorios
     */
    public static void init() {

        player = loadImage("/ships/player.png");

        doubleGunPlayer = loadImage("/ships/doubleGunPlayer.png");

        speed = loadImage("/effects/fire08.png");

        //------------------------------------------------------------//

        fondoMenu = loadImage("/Fondos/Fondo_menu.png");

        fondoInstrucions = loadImage("/Fondos/Fondo_instructions.jpg");

        fondoName = loadImage("/Fondos/Fondo_name.jpeg");

        fondoScore = loadImage("/Fondos/Fondo_Score.jpeg");

        fondoPause = loadImage("/Fondos/Fondo_pause.jpg");

        //mapa 1x1

        fondoGame = loadImage("/Fondos/Fondo_game.jpg");

        //mapa 2x2

        fondoGame2x2[0][0] = loadImage("/Fondos/Fondo_2x2/00.jpg");

        fondoGame2x2[0][1] = loadImage("/Fondos/Fondo_2x2/01.jpg");

        fondoGame2x2[1][0]  = loadImage("/Fondos/Fondo_2x2/10.jpg");

        fondoGame2x2[1][1] = loadImage("/Fondos/Fondo_2x2/11.jpg");


        //mapa 3x3

        fondoGame3x3[0][0] = loadImage("/Fondos/Fondo_3x3/1.jpg");

        fondoGame3x3[0][1]  = loadImage("/Fondos/Fondo_3x3/2.jpg");

        fondoGame3x3[0][2]  = loadImage("/Fondos/Fondo_3x3/3.jpg");

        fondoGame3x3[1][0]  = loadImage("/Fondos/Fondo_3x3/4.jpg");

        fondoGame3x3[1][1]  = loadImage("/Fondos/Fondo_3x3/5.jpg");

        fondoGame3x3[1][2]  = loadImage("/Fondos/Fondo_3x3/6.jpg");

        fondoGame3x3[2][0]  = loadImage("/Fondos/Fondo_3x3/7.jpg");

        fondoGame3x3[2][1]  = loadImage("/Fondos/Fondo_3x3/8.jpg");

        fondoGame3x3[2][2]  = loadImage("/Fondos/Fondo_3x3/9.jpg");

        //------------------------------------------------------------//

        blueLaser = loadImage("/lasers/laserBlue01.png");

        greenLaser = loadImage("/lasers/laserGreen11.png");

        redLaser = loadImage("/lasers/laserRed01.png");

        ufo = loadImage("/ships/ufo.png");

        life = loadImage("/others/life.png");

        fontBig = loadFont("/fonts/futureFont.ttf", 42);

        fontMed = loadFont("/fonts/futureFont.ttf", 20);

        for (int i = 0; i < 3; i++)
            shieldEffect[i] = loadImage("/effects/shield" + (i + 1) + ".png");

        for (int i = 0; i < bigs.length; i++)
            bigs[i] = loadImage("/meteors/big" + (i + 1) + ".png");

        for (int i = 0; i < meds.length; i++)
            meds[i] = loadImage("/meteors/med" + (i + 1) + ".png");

        for (int i = 0; i < smalls.length; i++)
            smalls[i] = loadImage("/meteors/small" + (i + 1) + ".png");

        for (int i = 0; i < tinies.length; i++)
            tinies[i] = loadImage("/meteors/tiny" + (i + 1) + ".png");

        for (int i = 0; i < exp.length; i++)
            exp[i] = loadImage("/explosion/" + i + ".png");

        for (int i = 0; i < numbers.length; i++)
            numbers[i] = loadImage("/numbers/" + i + ".png");

        backgroundMusic = loadSound("/sounds/backgroundMusic.wav");
        explosion = loadSound("/sounds/explosion.wav");
        playerLoose = loadSound("/sounds/playerLoose.wav");
        playerShoot = loadSound("/sounds/playerShoot.wav");
        ufoShoot = loadSound("/sounds/ufoShoot.wav");
        powerUp = loadSound("/sounds/powerUp.wav");

        greyBtn = loadImage("/ui/grey_button.png");
        blueBtn = loadImage("/ui/blue_button.png");
        instructions = loadImage("/ui/instructions.png");

        orb = loadImage("/powers/orb.png");
        doubleScore = loadImage("/powers/doubleScore.png");
        doubleGun = loadImage("/powers/doubleGun.png");
        fastFire = loadImage("/powers/fastFire.png");
        star = loadImage("/powers/star.png");
        shield = loadImage("/powers/shield.png");

        // ===========================================================

        loaded = true;

    }

    /**
     * cuenta la cant de imagenes que se cargaron.Esto sirve para la pantalla de carga
     *
     * @param path direccion del directorio que contiene la imagen
     * @return loader de la imagen
     */
    public static BufferedImage loadImage(String path) {
        count++;
        return Loader.ImageLoader(path);
    }

    /**
     * cuenta la cant de fuentes que se cargaron
     *
     * @param path direccion del directorio que contiene la fuente
     * @param size tamaño de la fuente
     * @return loader de la fuente
     */
    public static Font loadFont(String path, int size) {
        count++;
        return Loader.loadFont(path, size);
    }

    /**
     * cuenta la cant de sonidos que se cargaron
     *
     * @param path direccion del directorio que contiene el sonido
     * @return loader del sonido
     */
    public static Clip loadSound(String path) {
        count++;
        return Loader.loadSound(path);
    }

}
