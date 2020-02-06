package app;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;



public class App extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String fileSeparator = System.getProperty("file.separator");
    private static Graphics g;
    private static final String PLAYERIMGPATH = ("playerSprites" + fileSeparator);
    private static final String png = ".png";
    public static String currentPlayerSprite;
    public static BufferedImage img;

    private static int count = 0;

    public static void main(String[] args) throws Exception {

        JFrame window = new JFrame("Game");
        
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(800, 500);
        window.setVisible(true);
        g = window.getGraphics();
        window.add(new JLabel(new ImageIcon("")));

        System.out.println(fileSeparator + " " + currentPlayerSprite);
        while (true) {
            g.clearRect(0, 0, 800, 500);
            try {
                count = ++count % 8;
                currentPlayerSprite = PLAYERIMGPATH + "Walk" + count + png;
                loadImage(currentPlayerSprite);
                try {
                    g.drawImage(img, 50, 50, null);
                } catch (NullPointerException e) {
                    System.out.println(g == null);
                }
                Thread.sleep(150);
                
            } catch (InterruptedException e) {}
        }
    }

    public static void loadImage (String read) {
        File file = new File(read);
        try { img = ImageIO.read(file); }
        catch (IOException e) {
            // change image to error image
        }
    }

}