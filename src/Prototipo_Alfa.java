/**
 * AppletJuego
 *
 * Personaje para juego previo Examen
 *
 * @author Luis Alberto Lamadrid - A01191158  
 * @author Jesus Alejandro Valdes Valdes - A00999044
 * @version 1.00 2008/6/13
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Prototipo_Alfa extends JFrame implements Runnable, KeyListener {

    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private boolean bPause;       //pausa

    //Objeto de la clase Animacion para el manejo de la animación
    private Animacion animChickenRun;
    private Animacion animChickenAttack;
    private Animacion animChickenStand;
    private Animacion animHeroRun;
    private Animacion animHeroStand;
    private Animacion animHeroJump;   
    private Animacion animPato1;
    private Animacion animPato2;
	
    //Variables de control de tiempo de la animación
    private long tiempoActual;
    private int iContTiempo; //contador de tiempo
    /*
    Variable para controlar backgrounds
    0-Loading
    1-Main
    2-LevelSelect 
    3-Tutorial
    4-Level 1
    */
    private int iBackground;
    //imagen props
    private Image imaHealthBar;
    private Image imaPause;
    
    //arreglo de backgrounds
    private ArrayList arlBackgrounds;
    
    public Prototipo_Alfa(){
        init();
        start();
    }
    	
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     */
    public void init() {    
        // hago el applet de un tamaño 500,500
        setSize(1600, 600);
        iContTiempo = 0;
        //sin pause
        bPause = false;

        //empiezo en main
        iBackground = 0;
        //Creo imagenes de gallina stand y las agrego a la animacion
        Image imaChickenStand1 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_stand_01.png"));
        Image imaChickenStand2 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_stand_02.png"));
        Image imaChickenStand3 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_stand_03.png"));
        Image imaChickenStand4 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_stand_04.png"));
        
        animChickenStand = new Animacion(0, 0);
        animChickenStand.sumaCuadro(imaChickenStand1, 150);
        animChickenStand.sumaCuadro(imaChickenStand2, 150);
        animChickenStand.sumaCuadro(imaChickenStand3, 150);
        animChickenStand.sumaCuadro(imaChickenStand4, 150);
        
        //Creo imagenes de gallina run y las agrego a la animacion
        Image imaChickenRun1 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_attack_01.png"));
        Image imaChickenRun2 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_attack_02.png"));
        Image imaChickenRun3 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_attack_03.png"));
        Image imaChickenRun4 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_attack_04.png"));
        Image imaChickenRun5 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_attack_05.png"));
        Image imaChickenRun6 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_attack_06.png"));
        
        animChickenRun = new Animacion(0, 0);
        animChickenRun.sumaCuadro(imaChickenRun1, 150);
        animChickenRun.sumaCuadro(imaChickenRun2, 150);
        animChickenRun.sumaCuadro(imaChickenRun3, 150);
        animChickenRun.sumaCuadro(imaChickenRun4, 150);
        animChickenRun.sumaCuadro(imaChickenRun5, 150);
        animChickenRun.sumaCuadro(imaChickenRun6, 150);
        
        //Creo imagenes de gallina attack y las agrego a la animacion
        Image imaChickenAttack1 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_attack2_01.png"));
        Image imaChickenAttack2 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_attack2_02.png"));
        Image imaChickenAttack3 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_attack2_03.png"));
        Image imaChickenAttack4 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("gallina_zombie_attack2_04.png"));
        
        animChickenAttack = new Animacion(0, 0);
        animChickenAttack.sumaCuadro(imaChickenAttack1, 150);
        animChickenAttack.sumaCuadro(imaChickenAttack2, 150);
        animChickenAttack.sumaCuadro(imaChickenAttack3, 150);
        animChickenAttack.sumaCuadro(imaChickenAttack4, 150);
        
        //Creo imagenes de hero run y las agrego a la animacion
        Image imaHeroRun1 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00000.png"));
        Image imaHeroRun2 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00001.png"));
        Image imaHeroRun3 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00002.png"));
        Image imaHeroRun4 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00003.png"));
        Image imaHeroRun5 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00004.png"));
        Image imaHeroRun6 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00005.png"));
        Image imaHeroRun7 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00006.png"));
        Image imaHeroRun8 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00007.png"));
        Image imaHeroRun9 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00008.png"));
        Image imaHeroRun10 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00009.png"));
        Image imaHeroRun11 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00010.png"));
        Image imaHeroRun12 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00011.png"));
        Image imaHeroRun13 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00012.png"));
        Image imaHeroRun14 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00013.png"));
        Image imaHeroRun15 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00014.png"));
        Image imaHeroRun16 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00015.png"));
        Image imaHeroRun17 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00016.png"));
        Image imaHeroRun18 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00017.png"));
        Image imaHeroRun19 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00018.png"));
        Image imaHeroRun20 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00019.png"));
        Image imaHeroRun21 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroRunning_00020.png"));
        
        animHeroRun = new Animacion (0,0);
        animHeroRun.sumaCuadro(imaHeroRun1, 50);
        animHeroRun.sumaCuadro(imaHeroRun2, 50);
        animHeroRun.sumaCuadro(imaHeroRun3, 50);
        animHeroRun.sumaCuadro(imaHeroRun4, 50);
        animHeroRun.sumaCuadro(imaHeroRun5, 50);
        animHeroRun.sumaCuadro(imaHeroRun6, 50);
        animHeroRun.sumaCuadro(imaHeroRun7, 50);
        animHeroRun.sumaCuadro(imaHeroRun8, 50);
        animHeroRun.sumaCuadro(imaHeroRun9, 50);
        animHeroRun.sumaCuadro(imaHeroRun10, 50);
        animHeroRun.sumaCuadro(imaHeroRun11, 50);
        animHeroRun.sumaCuadro(imaHeroRun12, 50);
        animHeroRun.sumaCuadro(imaHeroRun13, 50);
        animHeroRun.sumaCuadro(imaHeroRun14, 50);
        animHeroRun.sumaCuadro(imaHeroRun15, 50);
        animHeroRun.sumaCuadro(imaHeroRun16, 50);
        animHeroRun.sumaCuadro(imaHeroRun17, 50);
        animHeroRun.sumaCuadro(imaHeroRun18, 50);
        animHeroRun.sumaCuadro(imaHeroRun19, 50);
        animHeroRun.sumaCuadro(imaHeroRun20, 50);
        animHeroRun.sumaCuadro(imaHeroRun21, 50);
        
        
        //Creo imagenes de hero stand y las agrego a la animacion
        Image imaHeroStand1 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00000.png"));
        Image imaHeroStand2 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00001.png"));
        Image imaHeroStand3 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00002.png"));
        Image imaHeroStand4 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00003.png"));
        Image imaHeroStand5 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00004.png"));
        Image imaHeroStand6 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00005.png"));
        Image imaHeroStand7 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00006.png"));
        Image imaHeroStand8 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00007.png"));
        Image imaHeroStand9 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00008.png"));
        Image imaHeroStand10 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00009.png"));
        Image imaHeroStand11 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00010.png"));
        Image imaHeroStand12 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00011.png"));
        Image imaHeroStand13 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00012.png"));
        Image imaHeroStand14 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00013.png"));
        Image imaHeroStand15 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00014.png"));
        Image imaHeroStand16 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00015.png"));
        Image imaHeroStand17 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00016.png"));
        Image imaHeroStand18 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00017.png"));
        Image imaHeroStand19 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00018.png"));
        Image imaHeroStand20 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00019.png"));
        Image imaHeroStand21 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00020.png"));
        Image imaHeroStand22 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00021.png"));
        Image imaHeroStand23 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00022.png"));
        Image imaHeroStand24 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00023.png"));
        Image imaHeroStand25 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00024.png"));
        Image imaHeroStand26 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00025.png"));
        Image imaHeroStand27 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00026.png"));
        Image imaHeroStand28 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00027.png"));
        Image imaHeroStand29 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroIdle_00028.png"));
        
        
        animHeroStand = new Animacion (0,0);
        animHeroStand.sumaCuadro(imaHeroStand1, 50);
        animHeroStand.sumaCuadro(imaHeroStand2, 50);
        animHeroStand.sumaCuadro(imaHeroStand3, 50);
        animHeroStand.sumaCuadro(imaHeroStand4, 50);
        animHeroStand.sumaCuadro(imaHeroStand5, 50);
        animHeroStand.sumaCuadro(imaHeroStand6, 50);
        animHeroStand.sumaCuadro(imaHeroStand7, 50);
        animHeroStand.sumaCuadro(imaHeroStand8, 50);
        animHeroStand.sumaCuadro(imaHeroStand9, 50);
        animHeroStand.sumaCuadro(imaHeroStand10, 50);
        animHeroStand.sumaCuadro(imaHeroStand11, 50);
        animHeroStand.sumaCuadro(imaHeroStand12, 50);
        animHeroStand.sumaCuadro(imaHeroStand13, 50);
        animHeroStand.sumaCuadro(imaHeroStand14, 50);
        animHeroStand.sumaCuadro(imaHeroStand15, 50);
        animHeroStand.sumaCuadro(imaHeroStand16, 50);
        animHeroStand.sumaCuadro(imaHeroStand17, 50);
        animHeroStand.sumaCuadro(imaHeroStand18, 50);
        animHeroStand.sumaCuadro(imaHeroStand19, 50);
        animHeroStand.sumaCuadro(imaHeroStand20, 50);
        animHeroStand.sumaCuadro(imaHeroStand21, 50);
        animHeroStand.sumaCuadro(imaHeroStand22, 50);
        animHeroStand.sumaCuadro(imaHeroStand23, 50);
        animHeroStand.sumaCuadro(imaHeroStand24, 50);
        animHeroStand.sumaCuadro(imaHeroStand25, 50);
        animHeroStand.sumaCuadro(imaHeroStand26, 50);
        animHeroStand.sumaCuadro(imaHeroStand27, 50);
        animHeroStand.sumaCuadro(imaHeroStand28, 50);
        animHeroStand.sumaCuadro(imaHeroStand29, 50);
        
        //Creo imagenes de hero jump y las agrego a la animacion
        Image imaHeroJump1 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00000.png"));
        Image imaHeroJump2 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00001.png"));
        Image imaHeroJump3 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00002.png"));
        Image imaHeroJump4 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00003.png"));
        Image imaHeroJump5 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00004.png"));
        Image imaHeroJump6 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00005.png"));
        Image imaHeroJump7 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00006.png"));
        Image imaHeroJump8 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00007.png"));
        Image imaHeroJump9 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00008.png"));
        Image imaHeroJump10 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00009.png"));
        Image imaHeroJump11 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00010.png"));
        Image imaHeroJump12 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00011.png"));
        Image imaHeroJump13 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00012.png"));
        Image imaHeroJump14 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00013.png"));
        Image imaHeroJump15 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00014.png"));
        Image imaHeroJump16 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00015.png"));
        Image imaHeroJump17 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00016.png"));
        Image imaHeroJump18 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00017.png"));
        Image imaHeroJump19 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00018.png"));
        Image imaHeroJump20 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00019.png"));
        Image imaHeroJump21 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("HeroJumping_00020.png"));
        
        
        
        animHeroJump = new Animacion (0,0);
        animHeroJump.sumaCuadro(imaHeroJump1, 50);
        animHeroJump.sumaCuadro(imaHeroJump2, 50);
        animHeroJump.sumaCuadro(imaHeroJump3, 50);
        animHeroJump.sumaCuadro(imaHeroJump4, 50);
        animHeroJump.sumaCuadro(imaHeroJump5, 50);
        animHeroJump.sumaCuadro(imaHeroJump6, 50);
        animHeroJump.sumaCuadro(imaHeroJump7, 50);
        animHeroJump.sumaCuadro(imaHeroJump8, 50);
        animHeroJump.sumaCuadro(imaHeroJump9, 50);
        animHeroJump.sumaCuadro(imaHeroJump10, 50);
        animHeroJump.sumaCuadro(imaHeroJump11, 50);
        animHeroJump.sumaCuadro(imaHeroJump12, 50);
        animHeroJump.sumaCuadro(imaHeroJump13, 50);
        animHeroJump.sumaCuadro(imaHeroJump14, 50);
        animHeroJump.sumaCuadro(imaHeroJump15, 50);
        animHeroJump.sumaCuadro(imaHeroJump16, 50);
        animHeroJump.sumaCuadro(imaHeroJump17, 50);
        animHeroJump.sumaCuadro(imaHeroJump18, 50);
        animHeroJump.sumaCuadro(imaHeroJump19, 50);
        animHeroJump.sumaCuadro(imaHeroJump20, 50);
        animHeroJump.sumaCuadro(imaHeroJump21, 50);
        
        //Creo imagenes pato y animaciones
        Image imaPato1 = Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("pato_stand_01.png"));
        Image imaPato2= Toolkit.getDefaultToolkit().getImage(
                this.getClass().getResource("pato_stand_02.png"));
        
        animPato1 = new Animacion(0, 0);
        animPato2 = new Animacion(0, 0);
        animPato1.sumaCuadro(imaPato1, 150);
        animPato2.sumaCuadro(imaPato1, 150);
        animPato1.sumaCuadro(imaPato2, 150);
        animPato2.sumaCuadro(imaPato2, 150);
        
        imaHealthBar = Toolkit.getDefaultToolkit().getImage(
            this.getClass().getResource("Health_bar.jpg"));
        imaPause = Toolkit.getDefaultToolkit().getImage(
            this.getClass().getResource("Pause_menu.jpg"));
        
        //lleno el arraylist con imagenes de fondo
        arlBackgrounds = new ArrayList();
        
        //creo imagen load
        URL urlImageLoad = 
                this.getClass().getResource("background_loading.jpg");
        Image imaImagenLoad = Toolkit.getDefaultToolkit()
                .getImage(urlImageLoad);
        
        arlBackgrounds.add(imaImagenLoad);
        
        // creo imagen Main
        URL urlImagenMain = 
                this.getClass().getResource("background_main.jpg");
        Image imaImagenMain = Toolkit.getDefaultToolkit()
                                           .getImage(urlImagenMain);
        arlBackgrounds.add(imaImagenMain);
        
        // creo imagen LevelSelect
        URL urlImagenLevelSelect = 
                this.getClass().getResource("background_levelselect.jpg");
        Image imaImagenLevelSelect = Toolkit.getDefaultToolkit()
                                           .getImage(urlImagenLevelSelect);
        arlBackgrounds.add(imaImagenLevelSelect);
        
        // creo imagen tutorial
        URL urlImagenTutorial = 
                this.getClass().getResource("background_tutorial.jpg");
        Image imaImagenTutorial= Toolkit.getDefaultToolkit()
                                           .getImage(urlImagenTutorial);
        arlBackgrounds.add(imaImagenTutorial);
        
        // creo imagen LevelSelect
        URL urlImagenLevel1 = 
                this.getClass().getResource("background_level1.jpg");
        Image imaImagenLevel1 = Toolkit.getDefaultToolkit()
                                           .getImage(urlImagenLevel1);
        arlBackgrounds.add(imaImagenLevel1);

 
        // se define el background en color blanco
	setBackground (Color.white);
        addKeyListener(this);
    }
    
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        // se realiza el ciclo del juego en este caso nunca termina
        tiempoActual = System.currentTimeMillis();
        
        while (true) {
            /* mientras dure el juego, se actualizan posiciones de jugadores
               se checa si hubo colisiones para desaparecer jugadores o corregir
               movimientos y se vuelve a pintar todo
            */ 
            if(!bPause) {
                actualiza();
                checaColision();
            }
                
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError)	{
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion del objeto elefante 
     * 
     */
    public void actualiza() {
        
        //Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
         long tiempoTranscurrido =
             System.currentTimeMillis() - tiempoActual;
            
         //Guarda el tiempo actual
       	 tiempoActual += tiempoTranscurrido;
         
         //Acutaliza la animacion
         animChickenStand.actualiza(tiempoTranscurrido);
         animChickenRun.actualiza(tiempoTranscurrido);
         animChickenAttack.actualiza(tiempoTranscurrido);
         animHeroRun.actualiza(tiempoTranscurrido);
         animHeroStand.actualiza(tiempoTranscurrido);
         animHeroJump.actualiza(tiempoTranscurrido);
         animPato1.actualiza(tiempoTranscurrido);
         animPato2.actualiza(tiempoTranscurrido);
         
         if(iBackground == 0) {
             if(iContTiempo <= 120) {
                 iContTiempo++;
             }
             else {
                 iContTiempo = 0;
                 iBackground++;
             }
         }
    }
    
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision de los objetos del JFrame
     * con las orillas del <code>JFrame</code> y entre si.
     * 
     */
    public void checaColision(){
        return;
    }
    
    
   
	
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint (Graphics graGrafico) {
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Despliego la imagen correspondiente
        if(iBackground < 5) {
            graGraficaApplet.drawImage((Image) arlBackgrounds.
                get(iBackground), 0, 0, getWidth(), getHeight(), this);
        }

        // Actualiza la imagen de fondo.
        graGraficaApplet.setColor (getBackground ());
        
        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint_buffer(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
        

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint_buffer
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint_buffer(Graphics g) {
        
        // si la imagen ya se cargo
        g.setFont(new Font("TimesRoman", Font.BOLD, 15));
        if (animChickenStand != null && animChickenRun != null
            && animChickenAttack != null && animHeroRun != null
                &&animHeroStand != null
                    && (iBackground == 3 || iBackground == 4) 
                            && bPause == false) { 
            
            g.drawImage(imaHealthBar, getWidth() - 230, 50, this);
            
            g.drawString("Standing",getWidth()/2 - 200, getHeight() - 210);
            g.drawImage(animChickenStand.getImagen() ,getWidth()/2 - 200 , 
                    getHeight() - 100, this);
            g.drawImage(animHeroStand.getImagen() ,getWidth()/2 - 200 , 
                    getHeight() - 200, this);
                        
            g.drawString("Running",getWidth()/2 - 100, getHeight() - 210);
            g.drawImage(animChickenRun.getImagen() ,getWidth()/2 - 100, 
                    getHeight() - 100, this);
            g.drawImage(animHeroRun.getImagen() ,getWidth()/2 - 100 , 
                    getHeight() - 200, this);
            
            g.drawString("Attacking",getWidth()/2, getHeight() - 210);
            g.drawImage(animChickenAttack.getImagen() ,getWidth()/2, 
                    getHeight() - 100, this);
            g.drawImage(animHeroJump.getImagen() ,getWidth()/2, 
                    getHeight() - 200, this);
                    
        }      
        else if (bPause) {
            g.drawImage(imaPause, getWidth()/2 - (imaPause.getWidth(this) / 2),
                    getHeight()/2 - (imaPause.getHeight(this)) / 2, this);
        }
        
        else if(iBackground == 2){
            g.drawImage(animHeroStand.getImagen() ,getWidth()/2 - 370 , 
                    getHeight() - 124, this);
            g.drawImage(animPato1.getImagen(), getWidth()/2,
                    getHeight() - 124, this);
            g.drawImage(animPato2.getImagen(), getWidth()/2 + 370,
                    getHeight() - 124, this);
        }

        // sino se ha cargado se dibuja un mensaje 
        else {
                //Da un mensaje mientras se carga el dibujo	
                g.drawString("No se cargo la imagen..", 20, 20);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }    
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_N){
            if(iBackground < 4 && iBackground != 0 && !bPause) {
               iBackground++;
            }
           
            else {
               iBackground = 0;
            }
        }
        
        else if(keyEvent.getKeyCode() == KeyEvent.VK_P){ 
            if(iBackground == 3 || iBackground ==4) {
                bPause = !bPause;
            }
        }
    }
}
