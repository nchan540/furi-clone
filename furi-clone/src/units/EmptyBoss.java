package units;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;

public class EmptyBoss extends Boss {

    public EmptyBoss(Player p) {
        super(0, 0, 0, -1000, -1000, 0, 0f, p);
        alive = false;
        ID = -1;
    }

    public void update() {

    }

    public void draw(Graphics g, Graphics2D g2, Color[] HITBOXCOLOURS) {

    }

    public String toString() {
        return "";
    }

    public void kill() {

    }

}