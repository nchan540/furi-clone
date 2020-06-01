package game;
import graph.*;
import shapes.*;
import units.*;
import constants.Settings;

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
import javax.swing.SwingUtilities;

public class App extends JPanel {

    //unused right now; graphics system
    private static final long serialVersionUID = 1L;
    // private static final String fileSeparator = System.getProperty("file.separator");
    // private static final String PLAYERIMGPATH = ("playerSprites" + fileSeparator);
    // private static final String png = ".png";

    //global jframe stuff (jframe and main game panel)
    public static JFrame window;
    public static JPanel panel;

    //placeholder global settings
    public static int MAXBOSSES = 2;
    public static boolean arcade = true;

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
    //refs ID #'s. 0 = charger, 1 = brawler, ...
    public static boolean[] bossesAllowed = {true, true, true, false};
    public static int bossesToKill;
    public static int bossesAlive;
    

    //player input info
    private static HashSet<Integer> keyIn = new HashSet<>();
    public static Point mouse = new Point(500, 500);
    // private static boolean mouseClicked = false;

    //player movement info
    public static int placeholderX = 0;
    public static int placeholderY = 0;

    public static final int[][] placeholder = {{KeyEvent.VK_D, 1}, {KeyEvent.VK_W, -1}, {KeyEvent.VK_A, -1}, {KeyEvent.VK_S, 1}};
    public static boolean game = true;
    public static boolean restart = true;

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
            if (b.hp>0) b.draw(g, g2, HITBOXCOLOURS);
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

        //draw laser energy
        g.setColor(Color.BLACK);
        g.fillRect(30, 75, 350, 20);
        g.setColor(Color.GRAY);
        g.fillRect(20, 70, 350, 20);
        if (player.energy > 4) g.setColor(Color.CYAN);
        else g.setColor(Color.BLACK);
        g.fillRect(20, 70, (int)(player.energy * 17.5), 20);

        // g.drawString(Long.toString(System.currentTimeMillis() - nextGameTick), 30, 150);

        //draw score
        if (player.score > 0) {
            g.setColor(Color.BLACK);
            g.fillRect(30, 105, (130 + Integer.toString(player.score).length() * 18), 40);
            g.setColor(Color.GRAY);
            g.fillRect(20, 100, (130 + Integer.toString(player.score).length() * 18), 40);
            g.setColor(Color.WHITE);
            g.drawString("SCORE " + player.score, 28, 131);
        }

        //draw boss health
        for (int i = 0; i < MAXBOSSES; ++i) {
            if (bosses[i].alive) bosses[i].drawUI(g, 10 + i * 490, 750);
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
        if (bossTimer == 0) {
            if ((arcade || (bossesAlive < bossesToKill)) && bossesAlive < MAXBOSSES) spawnBoss();
        }  
        else --bossTimer; 

        //update units
        player.update();

        //spawn & update adds
        if (--addTimer == 0) spawnAdd();
        for (Add a : ads) {
            if (a != null) {
                a.update();
                for (int i = 0; i < a.bullets.length; ++i) {
                    if (a.bullets[i] != null) {
                        boolean[] hits = a.bullets[i].hitDetect();
                        boolean hit = false;
                        for (int j = 0; j < hits.length; ++j) {
                            if (hits[j]) {
                                a.bullets[i].targets[j].takeDamage(a.bullets[i].damage);
                                hit = true;
                            }
                        }
                        if (hit) a.bullets[i] = null;
                    }
                }
            }
        }

        //update bosses
        for (Boss b : bosses) {
            if (b.hp > 0) { 
                b.update();
            }
            if (b.hp <= 0 && b.alive) {  
                b.kill();
                ++player.score;
                if(player.hp<constants.Player.HEALTH)++player.hp;
                if(player.hp<constants.Player.HEALTH)++player.hp;
                --bossesAlive;
                b = new EmptyBoss(player);
                if (!arcade) --bossesToKill;
                b.hitbox.p1.x = -100; 
                b.hitbox.p1.y = -100; 
                if (bossTimer == 0) {
                    bossTimer = 120;
                }
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
        //dash
        if(keyIn.contains(KeyEvent.VK_SPACE)) {
            if (player.dashTimer == 0) dash();
            keyIn.remove(KeyEvent.VK_SPACE);
        } else {
            player.move(placeholderX, placeholderY);
        }
        //attack
        if (player.attackFrames > 5 && player.attackFrames <= 10) {
            for (Boss b : bosses) {
                if (b.alive) {
                    player.checkAttack(b);
                    player.deflect(b.getBullets(), bosses);
                }
            }
            
            for (Add a : ads) {
                if (a != null) {
                    player.checkAttack(a);
                    player.deflect(a.getBullets(), bosses);
                }
            }
        }

        //check player hit by boss (collision)
        for (Boss b: bosses) {
            if (b.alive && Point_.distanceFormula(b.hitbox.p1, player.hitbox.p1) < (b.hitbox.diameter + player.hitbox.diameter)/2) {
                player.takeDamage(1);
            }
        }

        //end of frame; resetting movement
        placeholderX = 0;
        placeholderY = 0;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        //setting up the jframe + mouse cursor
        window = new JFrame("Game");
        window.setCursor(window.getToolkit().createCustomCursor(
            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
            "null"));
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(constants.Display.WIDTH+17, constants.Display.HEIGHT+40);
        window.setResizable(false);
        window.setVisible(true);


        //main game panel setting up
        panel = new App();
        panel.setSize(constants.Display.WIDTH, constants.Display.HEIGHT);
        try{backgroundImg = ImageIO.read(App.class.getResourceAsStream(BACKGROUND));}
        catch (Exception e){}
        panel.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                keyIn.add(e.getKeyCode());
                if (keyIn.contains(KeyEvent.VK_R) && !checkGameCondition()) {
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

        panel.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e)  {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (mouse.getX() > -1 && mouse.getY() > -1 && player.attackFrames == 0) player.setAttack(mouse, true);
                    player.attack();
                } else if (player.energy > 4) {
                    player.setAttack(mouse, false);
                    player.attack();
                }
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

        panel.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                mouse = new Point(e.getX(), e.getY());
            }
            public void mouseDragged(MouseEvent e) {
                mouse = new Point(e.getX(), e.getY());
            }
        });

        //main menu panel setting up + buttons
        MainPanel main = new MainPanel(mouse);
        main.setSize(constants.Display.WIDTH, constants.Display.HEIGHT);
        main.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                mouse = new Point(e.getX(), e.getY());
                for (UIElementInteractable u: main.elements) {
                    u.checkPos(mouse);
                }
            }
            public void mouseDragged(MouseEvent e) {
                mouse = new Point(e.getX(), e.getY());
                for (UIElementInteractable u: main.elements) {
                    u.checkPos(mouse);
                }
            }
        });

        main.addMouseListener(new MouseListener (){
            public void mousePressed(MouseEvent e)  {

            }
            public void mouseExited(MouseEvent e)  {
                mouse.x = -1000;
                mouse.y = -1000;
            }
            public void mouseReleased(MouseEvent e)  {
                
            }
            public void mouseClicked(MouseEvent e)  {
                for (UIElementInteractable u: main.elements) {
                    u.click();
                }
            }
            public void mouseEntered(MouseEvent e)  {
                mouse = new Point(e.getX(), e.getY());
            }
        });

        //set up images for main menu buttons
        String buttonLoad = "/buttonassets/";
        BufferedImage[] mainLoadedImages = new BufferedImage[3];
        //set up play button
        try{
            mainLoadedImages[0] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"arcade0.png"));
            mainLoadedImages[1] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"arcade1.png"));
            mainLoadedImages[2] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"arcade2.png"));
        }
        catch (Exception e){}
        main.addUIElement(new UIButton(mainLoadedImages, new Rectangle(new Point_(1300, 350), 170, 120)));
        //set up three vs two boss
        try{
            mainLoadedImages[0] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"bosses0.png"));
            mainLoadedImages[1] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"bosses1.png"));
            mainLoadedImages[2] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"bosses2.png"));
        }
        catch (Exception e){}
        main.addUIElement(new UIButton(mainLoadedImages, new Rectangle(new Point_(1300, 500), 170, 120), true));
        //campaign mode
        try{
            mainLoadedImages[0] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"camp0.png"));
            mainLoadedImages[1] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"camp1.png"));
            mainLoadedImages[2] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"camp2.png"));
        }
        catch (Exception e){}
        main.addUIElement(new UIButton(mainLoadedImages, new Rectangle(new Point_(1300, 650), 170, 120)));
        //whether or not to include superbosses
        try{
            mainLoadedImages[0] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"super0.png"));
            mainLoadedImages[1] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"super1.png"));
            mainLoadedImages[2] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"super2.png"));
        }
        catch (Exception e){}
        main.addUIElement(new UIButton(mainLoadedImages, new Rectangle(new Point_(1300, 200), 170, 120), true));

        //setting up campaign panel
        String[] campaignLoadImages = {"Lev1.png", "Lev2.png", "Lev3.png", "Lev4.png", "Lev5.png", "Lev6.png", "Lev7.png"};
        Point_[] campaignLoadPoints = {new Point_(200, 200), new Point_(400, 200),new Point_(600, 200),new Point_(200, 400),new Point_(400, 400),new Point_(600, 400), new Point_(20, 670)};
        CampaignPanel campaign = new CampaignPanel(mouse);
        
        campaign.setSize(constants.Display.WIDTH, constants.Display.HEIGHT);
        try{
            mainLoadedImages[1] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"campHover.png"));
            mainLoadedImages[2] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"campClicked.png"));
        } catch (Exception e){}

        for (String load : campaignLoadImages) {
            try{
                mainLoadedImages[0] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+load));
            }
            catch (Exception e){}
            campaign.addUIElement(new UIButton(mainLoadedImages, new Rectangle(campaignLoadPoints[campaign.elements.size()], 170, 120)));
        }

        try{
            mainLoadedImages[0] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"0campBack.png"));
            mainLoadedImages[1] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"1campBack.png"));
            mainLoadedImages[2] = ImageIO.read(App.class.getResourceAsStream(buttonLoad+"2campBack.png"));
        } catch (Exception e){}
        campaign.addUIElement(new UIButton(mainLoadedImages, new Rectangle (new Point_(800, 400), 170, 120)));

        campaign.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                mouse = new Point(e.getX(), e.getY());
                for (UIElementInteractable u: campaign.elements) {
                    u.checkPos(mouse);
                }
                System.out.println("click");
            }
            public void mouseDragged(MouseEvent e) {
                mouse = new Point(e.getX(), e.getY());
                for (UIElementInteractable u: campaign.elements) {
                    u.checkPos(mouse);
                }
            }
        });

        campaign.addMouseListener(new MouseListener (){
            public void mousePressed(MouseEvent e)  {

            }
            public void mouseExited(MouseEvent e)  {
                mouse.x = -1000;
                mouse.y = -1000;
            }
            public void mouseReleased(MouseEvent e)  {
                
            }
            public void mouseClicked(MouseEvent e)  {
                for (UIElementInteractable u: campaign.elements) {
                    u.click();
                    System.out.println("click");
                }
            }
            public void mouseEntered(MouseEvent e)  {
                mouse = new Point(e.getX(), e.getY());
            }
        });

        //let us start the game
        window.add(panel, 0);
        panel.setVisible(false);
        window.add(campaign, 1);
        campaign.setVisible(false);
        window.add(main, 0); 
        main.setVisible(true);
        Settings settings = new Settings();
        while (true) {
            nextGameTick = System.currentTimeMillis();

            main.update(mouse);
            main.update();
            window.repaint();

            if (TICKSPERFRAME > System.currentTimeMillis() - nextGameTick) Thread.sleep((TICKSPERFRAME - (System.currentTimeMillis() - nextGameTick)));

            //main menu begin screen was clicked
            if (main.bools.get(0)) {
                settings.arcadePreset(main.bools.get(1) ? 3 : 2, main.bools.get(3));
                main.setVisible(false);
                panel.setVisible(true);
                game(settings);
                main.setVisible(true);
                panel.setVisible(false);
                main.bools.set(0, false);
            }

            if (main.bools.get(2)) {
                campaign.setVisible(true);
                main.setVisible(false);
                campaign.grabFocus();
                while (!campaign.bools.get(7)) {
                    nextGameTick = System.currentTimeMillis();
                    campaign.update(mouse);
                    campaign.update();
                    campaign.repaint();

                    if (campaign.checkGame() >= 0) {
                        settings.campaignPreset(campaign.checkGame());
                        campaign.setVisible(false);
                        panel.setVisible(true);
                        game(settings);
                        campaign.setVisible(true);
                        panel.setVisible(false);
                        campaign.resetBools();
                    }

                    FPS();
                }
                main.setVisible(true);
                campaign.setVisible(false);
                main.bools.set(2, false);
                campaign.resetBools();
            }
        }
    }

    public static void FPS() {
        if (TICKSPERFRAME > System.currentTimeMillis() - nextGameTick) try{Thread.sleep((TICKSPERFRAME - (System.currentTimeMillis() - nextGameTick)));} catch (InterruptedException e){}
    }

    public static void loadPlayerImage (String read) {
        File file = new File(read);
        try { playerImg = ImageIO.read(file); }
        catch (IOException e) {
            // change image to error image
        }
    }





    //any function needed to set up the game loop

    public static void spawnAdd() {
        ads.add(new Add(player, 1, (int)Math.round(Math.random() * 1040) + 200, (int)Math.round(Math.random() * 400) + 200, 40, 0.5f));
        addTimer = 400 + (50 * ads.size()) + (int)(Math.random() * 100);
    }

    public static boolean checkBosses() {
        for (Boss b : bosses) {
            if (b.alive && b.ID == nextBoss) return true;
        }
        return false;
    }

    public static void spawnBoss() {
        do {
            nextBoss = Math.round(Math.round(Math.random() * (bossesAllowed.length-1)));
        } while (!bossesAllowed[nextBoss] || checkBosses());
        for (int i = 0; i < bosses.length; ++i) {
            if (bosses[i].hp <= 0) {
                switch(nextBoss) {
                    case 0:
                        bosses[i] = new Charger(bossSpawn[0], bossSpawn[1], player);
                        bosses[i].spawn();
                        break;
                    case 1:
                        bosses[i] = new Brawler(bossSpawn[0], bossSpawn[1], player);
                        bosses[i].spawn();
                        break;
                    case 2:
                        bosses[i] = new Laserman(bossSpawn[0], bossSpawn[1], player);
                        bosses[i].spawn();
                        break;
                    case 3:
                        bosses[i] = new Beast(bossSpawn[0], bossSpawn[1], player);
                        bosses[i].spawn();
                        break;
                    case 4:
                        bosses[i] = new Devourer(bossSpawn[0], bossSpawn[1], player);
                        bosses[i].spawn();
                        break;
                    default: break;
                }
                ++bossesAlive;
                if ((arcade || (bossesAlive < bossesToKill)) && bossesAlive < MAXBOSSES) bossTimer = 120;
                return;
            }
        }
    }
    
    //game window

    public static void game(Settings settings) {
        game = true;
        restart = true;
        while (game) {
            nextGameTick = System.currentTimeMillis();   

            if (restart) {
                restart(panel, settings);
            }
            while (checkGameCondition()) {
                nextGameTick = System.currentTimeMillis();          

                gameLoop();
                window.repaint();

                if (player.dramaticPause) try{Thread.sleep(200);}catch(InterruptedException e){}finally{player.dramaticPause = false;}
                FPS();
            } 
            if (keyIn.contains(KeyEvent.VK_ENTER)) {
                game = false;
                keyIn.remove(KeyEvent.VK_ENTER);
            }

            FPS();
        }
    }

    //call this before any instance of the game to properly set it up
    public static void restart(JPanel game, Settings settings) {
        panel.grabFocus();

        arcade = settings.arcade;

        mouse = new Point(-100, -100);
        player = new Player(720, 450, 50);
        player.hp = constants.Player.HEALTH;
        bosses = new Boss[settings.maxBosses];
        for (int i = 0; i < settings.maxBosses; ++i) {
            bosses[i] = new EmptyBoss(player);
        }

        MAXBOSSES = settings.maxBosses;
        ads.clear();
        addTimer = 200;

        bossTimer = 120;
        bossSpawn = new int[]{720, 400};
        nextBoss = Math.round(Math.round(Math.random() * 3));
        bossesAllowed = settings.bossesAllowed;
        bossesToKill = settings.bossesToKill;
        bossesAlive = 0;

        restart = false;
        keyIn.remove(KeyEvent.VK_R);
        System.gc();
    }

    public static boolean checkGameCondition() {
        if (player.hp <= 0) return false;
        if (!arcade && bossesToKill == 0) return false;

        return true;
    }

}