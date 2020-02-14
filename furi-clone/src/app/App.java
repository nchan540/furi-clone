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

    //unused right now; graphics system
    private static final long serialVersionUID = 1L;
    private static final String fileSeparator = System.getProperty("file.separator");
    private static final String PLAYERIMGPATH = ("playerSprites" + fileSeparator);
    private static final String png = ".png";

    //unused right now; graphics system
    public static String currentPlayerSprite;
    public static BufferedImage playerImg;
    public static BufferedImage backgroundImg;
    private static final String BACKGROUND = ("Screenshot (85).png");
    
    //dash available, dash unavailable, hit
    public static final Color[] HITBOXCOLOURS ={new Color(64, 159, 255, 127), new Color(34, 79, 120, 197), new Color(186, 0, 0, 180)};
    
    //FPS constants
    private static final int FPS = 60;
    private static final int TICKSPERFRAME = 1000/FPS;
    private static long nextGameTick;

    //player info
    public static Player player = new Player(720, 450, 50);
    public static int[] dashAnim = {0, 0, 0};
    public static int dashTimer = 0;

    //boss info
    public static Boss boss = new Charger(0, 0, 0, player);
    public static int bossTimer = 120;

    //player input info
    private static HashSet<Integer> keyIn = new HashSet<>();
    private static Point mouse = new Point(-100, -100);
    private static boolean mouseClicked = false;

    //player movement info
    public static int placeholderX = 0;
    public static int placeholderY = 0;

    public static final int[][] placeholder = {{KeyEvent.VK_D, 1}, {KeyEvent.VK_W, -1}, {KeyEvent.VK_A, -1}, {KeyEvent.VK_S, 1}};

    public App() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(Constants.Graphics.WIDTH, Constants.Graphics.HEIGHT));
        setFont(Constants.FONT);
    }

    public void drawCircle(Graphics g, Circle c) {
        g.fillOval(Math.round(c.p.x - c.diameter/2), Math.round(c.p.y - c.diameter/2), c.diameter, c.diameter);
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0, 0, null);

        //draw player
        if(player.iFrames > 0) {
            g.setColor(HITBOXCOLOURS[2]);
        } else if (dashTimer > 0) {
            g.setColor(HITBOXCOLOURS[1]);
        } else {
            g.setColor(HITBOXCOLOURS[0]);
        }
        g.fillOval(player.getX() - player.getRadius()/2, player.getY() - player.getRadius()/2, player.getRadius(), player.getRadius());
        
        //draw mouse
        g.setColor(HITBOXCOLOURS[2]);
        g.fillOval(Math.round(mouse.x-10), Math.round(mouse.y-10), 20, 20);

        //draw boss
        if (boss.hp > 0) {
            g.fillOval(Math.round(boss.location.x-(boss.radius/2)), Math.round(boss.location.y-(boss.radius/2)), boss.getRadius(), boss.getRadius());
        }

        //draw blink
        if (dashAnim[0] > 0) {
            g.setColor(Color.white);
            g.fillOval(dashAnim[1]  - dashAnim[0] * 10, dashAnim[2]  - dashAnim[0] * 10, dashAnim[0] * 20, dashAnim[0] * 20);
            dashAnim[0] -= 1;
        }

        //draw health
        g.setColor(Color.BLACK);
        g.fillRect(30, 25, 240, 40);
        g.setColor(Color.GRAY);
        g.fillRect(20, 20, 240, 40);
        for (int i = player.hp; i > 0; i--) {
            g.setColor(HITBOXCOLOURS[2]);
            g.fillRect(55*i + 35, 30, 45, 20);
        }
        g.setColor(Color.WHITE);
        g.drawString("HP", 30, 50);

        //draw score
        if (player.score > 0) {
            g.setColor(Color.BLACK);
            g.fillRect(30, 75, (130 + Integer.toString(player.score).length() * 18), 40);
            g.setColor(Color.GRAY);
            g.fillRect(20, 70, (130 + Integer.toString(player.score).length() * 18), 40);
            g.setColor(Color.WHITE);
            g.drawString("SCORE " + player.score, 28, 101);
        }

        //draw boss health

        g.setColor(Color.BLACK);
        g.fillRect(30, 710, 545, 40);
        g.setColor(Color.GRAY);
        g.fillRect(20, 705, 545, 40);
        g.setColor(Color.BLACK);
        g.fillRect(25, 710, 200, 30);
        g.fillRect(235, 705, 10, 40);
        g.setColor(Color.WHITE);
        g.drawString(boss.toString(), 30, 735);
        if (boss.maxHp > 0) {
            g.setColor(Color.BLACK);
            g.fillRect(255, 710, 300, 30);
            g.setColor(Color.RED);
            g.fillRect(255, 710, (int)(300 * ((float)boss.hp / boss.maxHp)), 30);
        }

        //draw boss spawning
        if (bossTimer > 30 && bossTimer < 70) {
            if (bossTimer > 20) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(HITBOXCOLOURS[2]);
            }
            g.fillOval(720 - ((int)(Math.pow(bossTimer-30,2) / 2)), 450 - ((int)(Math.pow(bossTimer-30,2) / 2)), (int)(Math.pow(bossTimer-30, 2)), (int)(Math.pow(bossTimer-30,2)));
        } else if (bossTimer > 10 && bossTimer <= 30) {
            g.setColor(Color.WHITE);
            int rad = 31 - bossTimer;
            g.fillOval((int)(720 - rad * 1.5), (int)(450 - rad * 1.5), rad * 3, rad * 3);
        } else if (bossTimer <= 10 && bossTimer > 0) {
            g.fillOval(690, 420, 60, 60);
        }

        //draw attack
        if (mouse.getX() > -1 && mouse.getY() > -1) {
            for (int i = 0; i < player.curAttack.hitboxes.length; ++i) {
                if (player.curAttack.hitboxes[i] instanceof Circle) {
                    if (player.attackFrames > 0) {
                        if (player.attackFrames > 5 && player.attackFrames <= 5 + Constants.Player.ATTACK_FRAMES) {
                            g.setColor(HITBOXCOLOURS[2]);
                        } else {
                            g.setColor(Color.BLACK);
                        }
                        drawCircle(g, player.curAttack.hitboxes[i]);
                    }
                }
            }
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

        //update units
        player.update();
        if (boss.hp > 0) boss.update();
        else {
            boss.location.x = -100; 
            boss.location.y = -100; 
        }

        //boss spawning
        spawnBoss();  

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
        if (player.attackFrames > 5 && player.attackFrames <= 10) {
            player.checkAttack(boss);
        }

        //check player hit by boss (collision)
        if (Constants.distanceFormula(boss.location, player.location) < (boss.radius + player.radius)/2) {
            player.hit();
        }

        

        //resetting movement
        placeholderX = 0;
        placeholderY = 0;
        if (dashTimer > 0) --dashTimer;


    }

    public static void spawnBoss() {
        if (boss.hp <= 0) {
            if (bossTimer == 0) {
                boss = new Charger(300, 720, 450, player);
                bossTimer = 120;
            } else {
                --bossTimer;
            }
        }
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
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    player.attack();
                    System.out.println("pressed");
                }
            }
        });

        window.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e)  {
                if (mouse.getX() > -1 && mouse.getY() > -1) player.setAttack(new Point_(mouse.getX(), mouse.getY()));
                player.attack();
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
        window.setSize(Constants.Graphics.WIDTH, Constants.Graphics.HEIGHT);
        File f = new File(BACKGROUND);
        window.setResizable(false);
        try{backgroundImg = ImageIO.read(f);} 
        catch (Exception e){

        }

        while (player.hp > 0) {


            nextGameTick = System.currentTimeMillis();          

            gameLoop();
            
            //graphics, rewriting old surface
            App panel = new App();
            window.add(panel, 0);
            window.setVisible(true);

            if (player.dramaticPause) try{Thread.sleep(200);}catch(InterruptedException e){}finally{player.dramaticPause = false;}

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