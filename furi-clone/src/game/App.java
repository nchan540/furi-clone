package game;
import graph.*;
import projectiles.*;
import shapes.*;
import units.*;

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
import java.awt.Graphics2D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;



public class App extends JPanel {

    //unused right now; graphics system
    private static final long serialVersionUID = 1L;
    // private static final String fileSeparator = System.getProperty("file.separator");
    // private static final String PLAYERIMGPATH = ("playerSprites" + fileSeparator);
    // private static final String png = ".png";

    //unused right now; graphics system
    public static String currentPlayerSprite;
    public static BufferedImage playerImg;
    public static BufferedImage backgroundImg;
    private static final String BACKGROUND = ("/Screenshot (85).png");
    
    //dash available, dash unavailable, hit
    public static final Color[] HITBOXCOLOURS = {new Color(64, 159, 255, 127), new Color(236, 240, 38, 80), new Color(186, 0, 0, 180), new Color(0, 0, 0, 127), new Color (236, 240, 38, 255), new Color(186, 50, 50, 100)};
    
    //FPS constants
    private static final int FPS = 60;
    private static final int TICKSPERFRAME = 1000/FPS;
    private static long nextGameTick;

    //player info
    public static Player player = new Player(720, 450, 50);
    public static int[] dashAnim = {0, 0, 0};

    //add info
    public static HashSet<Add> ads = new HashSet<Add>();
    public static int addTimer;

    //boss info
    public static Boss bosses[];
    public static int bossTimer;
    public static int bossSpawn[];
    public static int nextBoss;
    public static boolean[] bossesAlive; //0 = dead, 1 = alive, charger, brawler, laserman
    

    //player input info
    private static HashSet<Integer> keyIn = new HashSet<>();
    private static Point mouse;
    // private static boolean mouseClicked = false;

    //player movement info
    public static int placeholderX = 0;
    public static int placeholderY = 0;

    public static final int[][] placeholder = {{KeyEvent.VK_D, 1}, {KeyEvent.VK_W, -1}, {KeyEvent.VK_A, -1}, {KeyEvent.VK_S, 1}};
    public static boolean game = true;
    public static boolean restart = false;

    public App() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(constants.Display.WIDTH, constants.Display.HEIGHT));
        setFont(constants.Display.FONT);
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g.drawImage(backgroundImg, 0, 0, null);

        //draw player
        player.draw(g, g2, HITBOXCOLOURS);
        
        
        //draw mouse5
        g.setColor(HITBOXCOLOURS[2]);
        g.fillOval(Math.round(mouse.x-10), Math.round(mouse.y-10), 20, 20);
        constants.Display.drawCircle(g, new Circle(new Point_(mouse.x, mouse.y), 20));

        //draw boss
        for (Boss b : bosses) {
                b.draw(g, g2, HITBOXCOLOURS);
        }

        for (Add add : ads) {
            if (add != null) add.draw(g, g2, HITBOXCOLOURS);
        }

        //draw blink
        if (dashAnim[0] > 0) {
            g.setColor(Color.white);
            g.fillOval(dashAnim[1]  - dashAnim[0] * 10, dashAnim[2]  - dashAnim[0] * 10, dashAnim[0] * 20, dashAnim[0] * 20);
            dashAnim[0] -= 1;
        }

        //draw health
        g.setColor(Color.BLACK);
        g.fillRect(30, 25, 350, 40);
        g.setColor(Color.GRAY);
        g.fillRect(20, 20, 350, 40);
        for (int i = player.hp; i > 0; i--) {
            g.setColor(HITBOXCOLOURS[2]);
            g.fillRect(55*i + 35, 30, 45, 20);
        }
        g.setColor(Color.WHITE);
        g.drawString("HP", 30, 50);
        // g.drawString(Long.toString(System.currentTimeMillis() - nextGameTick), 30, 150);

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
            if (bosses[0].maxHp > 0) {
                g.setColor(Color.WHITE);
                g.drawString(bosses[0].toString(), 30, 735);
                g.setColor(Color.BLACK);
                g.fillRect(255, 710, 300, 30);
                g.setColor(Color.RED);
                g.fillRect(255, 710, (int)(300 * ((float)bosses[0].hp / bosses[0].maxHp)), 30);
            }

            g.setColor(Color.BLACK);
            g.fillRect(860, 710, 545, 40);
            g.setColor(Color.GRAY);
            g.fillRect(850, 705, 545, 40);
            g.setColor(Color.BLACK);
            g.fillRect(855, 710, 200, 30);
            g.fillRect(1065, 705, 10, 40);
            if (bosses[1].maxHp > 0) {
                g.setColor(Color.WHITE);
                g.drawString(bosses[1].toString(), 860, 735);
                g.setColor(Color.BLACK);
                g.fillRect(1085, 710, 300, 30);
                g.setColor(Color.RED);
                g.fillRect(1085, 710, (int)(300 * ((float)bosses[1].hp / bosses[1].maxHp)), 30);
            }

        //draw boss spawning
        if (bossTimer > 30 && bossTimer < 70) {
            if (bossTimer > 20) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(HITBOXCOLOURS[2]);
            }
            g.fillOval(bossSpawn[0] - ((int)(Math.pow(bossTimer-30,2) / 2)), bossSpawn[1] - ((int)(Math.pow(bossTimer-30,2) / 2)), (int)(Math.pow(bossTimer-30, 2)), (int)(Math.pow(bossTimer-30,2)));
        } else if (bossTimer > 10 && bossTimer <= 30) {
            g.setColor(Color.WHITE);
            int rad = 31 - bossTimer;
            g.fillOval((int)(bossSpawn[0] - rad * 1.5), (int)(bossSpawn[1] - rad * 1.5), rad * 3, rad * 3);
        } else if (bossTimer <= 10 && bossTimer > 0) {
            g.fillOval(bossSpawn[0]-30, bossSpawn[1]-30, 60, 60);
        }
    }

    public static void dash() {
            dashAnim[0] = 5;
            dashAnim[1] = player.getX();
            dashAnim[2] = player.getY();
            player.dash(placeholderX, placeholderY);
    }

    public static void gameLoop() {
        //boss spawning
        if (bossTimer == 0) spawnBoss();  
        else --bossTimer;

        //update units
        player.update();
        if (player.killedBoss) {
            if (player.bossesAlive > 0 && bossTimer == 0) {
                bossTimer = 900;
            } else {
                bossTimer = 120;
            }
            for (Unit u : player.killed) {
                bossesAlive[u.ID] = false;
            }
            nextBoss = Math.round(Math.round(Math.random() * 2));
            player.killedBoss = false;
        }

        if (--addTimer == 0) spawnAdd();

        for (Add a : ads) {
            if (a != null) {
                a.update();
                for (Bullet b : a.bullets) {
                    if (b != null) {
                        b.changeTargets(new Shape[] {new Circle(player.location, (int)(player.radius / 2))});
                        if (b.hitDetect()[0]) player.hit();
                    }
                }
            }
        }

        for (Boss b : bosses) {
            if (b.hp > 0) { 
                b.update();
            } else { 
                b.location.x = -100; 
                b.location.y = -100; 
            }
        }

        

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
            if (player.dashTimer == 0) dash();
            keyIn.remove(KeyEvent.VK_SPACE);
        } else {
            player.move(placeholderX, placeholderY);
        }
        if (player.attackFrames > 5 && player.attackFrames <= 10) {
            for (Boss b : bosses) {
                player.checkAttack(b);
            }
            
            for (Add a : ads) {
                if (a != null) {
                    player.checkAttack(a);
                }
            }
        }

        //check player hit by boss (collision)
        for (Boss b: bosses) {
            if (Point_.distanceFormula(b.location, player.location) < (b.radius + player.radius)/2) {
                player.hit();
            }
        }

        //resetting movement
        placeholderX = 0;
        placeholderY = 0;
    }

    public static void setBossSpawn() {
        if (nextBoss != 2) {
            bossSpawn[0] = 720;
            bossSpawn[1] = 400;
        } else {
            bossSpawn[0] = 200;
            bossSpawn[1] = 200;
        }
    }

    public static void spawnBoss() {
        for (int i = 0; i < 2; i++) {
            if (bosses[i].hp <= 0) {
                switch(nextBoss) {
                    case 0:
                        bosses[i] = new Charger(bossSpawn[0], bossSpawn[1], player);
                        bosses[i].spawn();
                        bossesAlive[nextBoss] = true;
                        break;
                    case 1:
                        bosses[i] = new Brawler(bossSpawn[0], bossSpawn[1], player);
                        bosses[i].spawn();
                        bossesAlive[nextBoss] = true;
                        break;
                    case 2:
                        bosses[i] = new Laserman(bossSpawn[0], bossSpawn[1], player);
                        bosses[i].spawn();
                        bossesAlive[nextBoss] = true;
                        break;
                    case 3:
                        bosses[i] = new Beast(bossSpawn[0], bossSpawn[1], player);
                        bosses[i].spawn();
                        bossesAlive[nextBoss] = true;
                        break;
                    default: break;
                }
                while (bossesAlive[nextBoss]) {
                    nextBoss = Math.round(Math.round(Math.random() * 2));
                }
                setBossSpawn();
                ++player.bossesAlive;
                if (player.bossesAlive != 2) bossTimer = 1200;
                return;
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
                if (keyIn.contains(KeyEvent.VK_R) && player.hp <= 0) {
                    restart = true;
                }
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
                if (mouse.getX() > -1 && mouse.getY() > -1 && player.attackFrames == 0) player.setAttack(new Point_(mouse.getX(), mouse.getY()));
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
        window.setSize(constants.Display.WIDTH, constants.Display.HEIGHT);
        window.setResizable(false);
        try{backgroundImg = ImageIO.read(App.class.getResourceAsStream(BACKGROUND));}
        catch (Exception e){

        }

        App panel = new App();
        JPanel something = new JPanel();
        
        window.add(panel, 0);
        window.setVisible(true);
        panel.setVisible(false);

        while (game) {
            try{
                Thread.sleep(10);
            }catch(InterruptedException e) {

            }
            if (restart) {
                restart(panel);
            }
            while (player.hp > 0) {
                keyIn.remove(KeyEvent.VK_R);
                setBossSpawn();

                while (player.hp > 0) {


                nextGameTick = System.currentTimeMillis();          

                gameLoop();
                window.repaint();

                if (player.dramaticPause) try{Thread.sleep(200);}catch(InterruptedException e){}finally{player.dramaticPause = false;}

                if (TICKSPERFRAME > System.currentTimeMillis() - nextGameTick) Thread.sleep((TICKSPERFRAME - (System.currentTimeMillis() - nextGameTick)));
                } 
            }
            if (keyIn.contains(KeyEvent.VK_ESCAPE)) {
                game = false;
            }
        }
    }

    public static void loadPlayerImage (String read) {
        File file = new File(read);
        try { playerImg = ImageIO.read(file); }
        catch (IOException e) {
            // change image to error image
        }
    }

    public static void spawnAdd() {
        ads.add(new Add(player, 1, (int)Math.round(Math.random() * 1040) + 200, (int)Math.round(Math.random() * 400) + 200, 40, 0.5f));
        addTimer = 400 + (50 * ads.size()) + (int)(Math.random() * 100);
    }

    public static void restart(JPanel game) {
        game.setVisible(true);
        mouse = new Point(-100, -100);
        player = new Player(720, 450, 50);
        player.hp = constants.Player.HEALTH;
        bosses = new Boss[]{new EmptyBoss(player), new EmptyBoss(player)};
        ads.clear();
        addTimer = 200;
        bossTimer = 120;
        bossSpawn = new int[]{0, 0};
        nextBoss = Math.round(Math.round(Math.random() * 2));
        bossesAlive = new boolean[]{false, false, false, false};
        restart = false;
        keyIn.remove(KeyEvent.VK_R);
    }

}