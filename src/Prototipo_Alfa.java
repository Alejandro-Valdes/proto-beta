/**
 * AppletJuego
 *
 * Personaje para juego previo Examen
 *
 * @author Luis Alberto Lamadrid - A01191158  
 * @author Jeronimo Martinez - A01191487
 * @version 1.00 2008/6/13
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.lang.System.console;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;

public class Prototipo_Alfa extends JFrame implements Runnable, KeyListener {

    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private int iVidas;                 // Vidas del juego
    private int iScore;                 // Score del juego
    private int iDireccion;             // Direccion de la Tabla
    private int iDireccionProyectil;    // Direccion del proyectil
    private LinkedList lstCajas;        // Lista Cajas
    private LinkedList lstPowerUps;     // Lista de todos los power ups
    private Proyectil proBola;          // Objeto Bola de la clase personaje
    private Personaje perMain;          // Objeto Tabla de la clase personaje
    private Personaje perChicken;       // Objeto Tabla de la clase personaje
    private int iDeltaY;                // entero para hacer q bricks se muevan
    private int iTimer;                 // Timer para mover cajas para abajo
    private int iIntervalo;             // Maneja el intervalo de tiempo para 
                                        //  que los ladrillos bajen
    private boolean ChocaAbajoBrick;
    private boolean bPausado;
    private String Text;
    //private boolean bPausado;         // Pausa
    
    /* objetos de audio */
    private SoundClip aucSonidoSuccess; // Objeto AudioClip sonido Caminador
    private SoundClip aucSonidoFailure; // Objeto AudioClip sonido Corredor
    //Objeto de la clase Animacion para el manejo de la animación
    private Animacion animHero;
    private Animacion animChicken;
        
	
	//Variables de control de tiempo de la animación
	private long tiempoActual;
	private long tiempoInicial;
	int posX, posY;
    
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
                                
        ChocaAbajoBrick = false;
        Text = "";
        
        // hago el applet de un tamaño 500,500
        setSize(1600, 600);
        
        iScore = 0;
        iVidas = 5;
        
        // Crear imagen de Tabla y le pone velocidad
        URL urlImagenMain = this.getClass().getResource("HeroRunning_00000.png");
        perMain = new Personaje(200, 200,
                Toolkit.getDefaultToolkit().getImage(urlImagenMain));
        perMain.setX(30);
        perMain.setY(getHeight() - perMain.getAlto() - 10);
        
        // Crear imagen de Tabla y le pone velocidad
        URL urlImagenChick = this.getClass().getResource("gallina_zombie_stand.gif");
        perChicken = new Personaje(200, 200,
                Toolkit.getDefaultToolkit().getImage(urlImagenChick));
        System.out.print(perChicken);
        
        perChicken.setX(1200);
        perChicken.setY(getHeight() - perChicken.getAlto() - 10);
        

        // se define el background en color amarillo
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
            actualiza();
            checaColision();
                
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
        
        // creo imagen para el background
        URL urlImagenBackground = this.getClass().getResource("background_level1.jpg");
        Image imaImagenBackground = Toolkit.getDefaultToolkit()
                                           .getImage(urlImagenBackground);

        // Despliego la imagen
        graGraficaApplet.drawImage(imaImagenBackground, 0, 0, 
                getWidth(), getHeight(), this);

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
        if (perMain != null && animHero != null) {            
            g.drawImage(perMain.getImagen() , perMain.getX(), perMain.getY(), this);
            g.drawImage(perChicken.getImagen() , perChicken.getX(), perChicken.getY(), this);
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

    }
}
