package app;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Color;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



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
    private static final int FPS = 30;
    private static final int TICKSPERFRAME = 1000/30;
    private static final int MAXFRAMESKIP = 5;
    private static long nextGameTick;


    public static Player player = new Player(720, 450, 100);
    public static int[] dashAnim = {0, 0, 0};
    public static int dashTimer = 0;

    private static final HashSet<Integer> keyIn = new HashSet<>();
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

        if (dashTimer > 0) {
            g.setColor(HITBOXCOLOURS[1]);
        } else {
            g.setColor(HITBOXCOLOURS[0]);
        }
        
        g.fillOval(player.getX(), player.getY(), player.getRadius(), Math.round(Math.round(player.radius*0.5)));

        if (dashAnim[0] > 0) {
            g.setColor(Color.white);
            g.fillOval(dashAnim[1], dashAnim[2], dashAnim[0] * 20, dashAnim[0] * 20);
            dashAnim[0] -= 1;
        }
    }

    public static void dash() {
            dashAnim[0] = 5;
            dashAnim[1] = player.getX();
            dashAnim[2] = player.getY();
            player.dash(placeholderX, placeholderY);
            dashTimer = 30;
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
        placeholderX = 0;
        placeholderY = 0;
        if (dashTimer > 0) --dashTimer;


    }
    public static void main(String[] args) throws Exception {

        JFrame window = new JFrame("Game");

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
        
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1440, 900);
        File f = new File(BACKGROUND);
        try{backgroundImg = ImageIO.read(f);} 
        catch (Exception e){

        }

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