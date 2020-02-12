package app;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.awt.Point;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Color;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;



public class App extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final String fileSeparator = System.getProperty("file.separator");
    private static final String PLAYERIMGPATH = ("playerSprites" + fileSeparator);
    private static final String png = ".png";

    public static String currentPlayerSprite;
    public static BufferedImage playerImg;
    public static BufferedImage backgroundImg;

    public static final Color[] HITBOXCOLOURS ={new Color(64, 159, 255, 127), new Color(0, 0, 0, 127), new Color(186, 0, 0, 180)};
    //dash available, dash unavailable, hit
    
    private static final String BACKGROUND = ("Screenshot (85).png");

    //FPS constants
    private static final int FPS = 15;
    private static final int TICKSPERFRAME = 1000/FPS;
    private static long nextGameTick;

    public static Boss boss;

    public static Player player = new Player(720, 450, 50);
    public static int[] dashAnim = {0, 0, 0};
    public static int dashTimer = 0;

    private static HashSet<Integer> keyIn = new HashSet<>();
    private static Point mouse = new Point(-100, -100);
    private static boolean mouseClicked = false;

    public static int placeholderX = 0;
    public static int placeholderY = 0;

    public static final int[][] placeholder = {{KeyEvent.VK_D, 1}, {KeyEvent.VK_W, -1}, {KeyEvent.VK_A, -1}, {KeyEvent.VK_S, 1}};

    public App() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1439, 900));
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0, 0, null);


        //draw player
        if (dashTimer > 0) {
            g.setColor(HITBOXCOLOURS[1]);
        } else {
            g.setColor(HITBOXCOLOURS[0]);
        }
        g.fillOval(player.getX() - player.getRadius()/2, player.getY() - player.getRadius()/2, player.getRadius(), player.getRadius());
        
        //draw mouse
        g.setColor(HITBOXCOLOURS[2]);
        g.fillOval(Math.round(mouse.x-10), Math.round(mouse.y-10), 20, 20);

        //draw boss
        g.fillOval(Math.round(boss.location.x-boss.radius), Math.round(boss.location.y-boss.radius), boss.getRadius(), boss.getRadius());

        if (dashAnim[0] > 0) {
            g.setColor(Color.white);
            g.fillOval(dashAnim[1] + player.getRadius()/2 - dashAnim[0] * 10, dashAnim[2] + player.getRadius()/2 - dashAnim[0] * 10, dashAnim[0] * 20, dashAnim[0] * 20);
            dashAnim[0] -= 1;
        }
    }

    public static void dash() {
            dashAnim[0] = 5;
            dashAnim[1] = player.getX();
            dashAnim[2] = player.getY();
            player.dash(placeholderX, placeholderY);
            dashTimer = 60;
    }

    public static void gameLoop() {

        //movement
        for (int i = 0; i < 4; ++i) {
            if (keyIn.contains(placeholder[i][0])) {
                if (i % 2 == 0) {
                    placeholderX += placeholder[i][1];
                    if (placeholderX > 1) placeholderX = 1;
                    if (placeholderX < -1) placeholderX = -1;
                } else {
                    placeholderY += placeholder[i][1];
                    if (placeholderY > 1) placeholderY = 1;
                    if (placeholderY < -1) placeholderY = -1;
                }
            }
        }
        if(keyIn.contains(KeyEvent.VK_SPACE)) {
            if (dashTimer == 0) dash();
            keyIn.remove(KeyEvent.VK_SPACE);
        } else {
            player.move(placeholderX, placeholderY);
        }

        boss.update();

        placeholderX = 0;
        placeholderY = 0;
        if (dashTimer > 0) --dashTimer;


    }

    public static void spawnBoss() {
        boss = new Charger(720, 450, player);
    }
    public static void main(String[] args) throws Exception {

        JFrame window = new JFrame("Game");
        window.setCursor(window.getToolkit().createCustomCursor(
            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
            "null"));

        window.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                keyIn.add(e.getKeyCode());
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                keyIn.remove(e.getKeyCode());
            }
        
            @Override
            public void keyTyped(KeyEvent e) {
                keyIn.add(e.getKeyCode());
            }
        });

        window.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e)  {
            }
            public void mouseExited(MouseEvent e)  {
                mouse.x = -1000;
                mouse.y = -1000;
            }
            public void mouseReleased(MouseEvent e)  {
                
            }
            public void mouseClicked(MouseEvent e)  {
                
            }
            public void mouseEntered(MouseEvent e)  {
                mouse = new Point(e.getX(), e.getY());
            }
        });

        window.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                mouse = new Point(e.getX(), e.getY());
            }
            public void mouseDragged(MouseEvent e) {
                mouse = new Point(e.getX(), e.getY());
            }
        });
        
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1440, 900);
        File f = new File(BACKGROUND);
        try{backgroundImg = ImageIO.read(f);} 
        catch (Exception e){

        }

        spawnBoss();  

        while (true) {

            nextGameTick = System.currentTimeMillis();          

            gameLoop();
            
            //graphics, rewriting old surface
            App panel = new App();
            window.add(panel, 0);
            window.setVisible(true);

            if (TICKSPERFRAME > System.currentTimeMillis() - nextGameTick) Thread.sleep((TICKSPERFRAME - (System.currentTimeMillis() - nextGameTick)));
        } 
    }

    public static void loadPlayerImage (String read) {
        File file = new File(read);
        try { playerImg = ImageIO.read(file); }
        catch (IOException e) {
            // change image to error image
        }
    }

}