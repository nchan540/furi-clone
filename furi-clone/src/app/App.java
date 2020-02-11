package app;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    private static final long TICKSPERFRAME = 75;

    public static Player player = new Player(720, 450, 100);

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
        g.setColor(HITBOXCOLOURS[0]);
        g.fillOval(player.getX() + 45, player.getY() + 170, player.getRadius(), Math.round(Math.round(player.radius*0.5)));
    }

    public static void gameLoop() {

    }
    public static void main(String[] args) throws Exception {

        JFrame window = new JFrame("Game");

        window.addKeyListener(new KeyListener() {

            int keyCode;

            @Override
            public void keyPressed(KeyEvent e) {
                keyCode = e.getKeyCode();
                for (int i = 0; i < 4; ++i) {
                    if (keyCode == placeholder[i][0]) {
                        if (i % 2 == 0) {
                            placeholderX = placeholder[i][1];
                        } else {
                            placeholderY = placeholder[i][1];
                        }
                    }
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                keyCode = e.getKeyCode();
                for (int i = 0; i < 4; ++i) {
                    if (keyCode == placeholder[i][0]) {
                        if (i % 2 == 0) {
                            placeholderX = 0;
                        } else {
                            placeholderY = 0;
                        }
                    }
                }
            }
        
            @Override
            public void keyTyped(KeyEvent e) {
        
            }
        });
        
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(1440, 900);
        File f = new File(BACKGROUND);
        try{backgroundImg = ImageIO.read(f);} 
        catch (Exception e){

        }

        while (true) {
            App panel = new App();
            window.add(panel, 0);
            window.setVisible(true);
            try {
                player.move(placeholderX, placeholderY);
                Thread.sleep(50);
            } catch (InterruptedException e) {}
            System.out.println(player.getX() + ", " + player.getY());
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