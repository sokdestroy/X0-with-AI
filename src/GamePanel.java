import com.sun.xml.internal.ws.api.ResourceLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GamePanel extends JPanel{
    private Image cross, zero;
    private char [][]status;
    private ClassLoader cl;

    public GamePanel() {
        try {
            cl = this.getClass().getClassLoader();
            cross = new ImageIcon(cl.getResource("Img/X.png")).getImage();
            zero = new ImageIcon(cl.getResource("Img/O.png")).getImage();
        }
        catch(Exception ex) {ex.printStackTrace();}
    }

    public void paintComponent(Graphics g) {
        //0 - 0, 1 - x
        g.setColor(Color.BLACK);
        drawGrid(g);

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (status[i][j] == 'X') drawCross(g, i + 1, j + 1);
                else if (status[i][j] == '0') drawZero(g, i + 1, j + 1);
    }

    public void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        Graphics2D gr = (Graphics2D) g;
        g.drawLine((int) (getWidth() / 3), 0, (int) (getWidth() / 3), getHeight());
        g.drawLine((int) (getWidth() * 2 / 3), 0, (int) (getWidth() * 2 / 3), getHeight());
        g.drawLine(0, (int) (getHeight() / 3), getWidth(), (int) (getHeight() / 3));
        g.drawLine(0, (int) (getHeight() * 2 / 3), getWidth(), (int) (getHeight() * 2 / 3));
    }

    public void drawCross(Graphics g, int x, int y) {
        Graphics2D gr = (Graphics2D) g;
        gr.drawImage(cross,(x-1)*getWidth()/3,(y-1)*getHeight()/3,getWidth()/3,getHeight()/3,this);
    }

    public void drawZero(Graphics g, int x, int y) {
        Graphics2D gr = (Graphics2D) g;
        gr.drawImage(zero,(x-1)*getWidth()/3,(y-1)*getHeight()/3,getWidth()/3,getHeight()/3,this);
    }

    public void setStatus(char [][]s) {
        status = s;
    }
}
