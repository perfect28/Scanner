
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by kkpanda on 2014/12/6.
 */
public class Draw extends JPanel {


    private double x;
    private double y;

    Draw()
    {

    }

    public void modify(double x,double y)
    {
        this.x=x;
        this.y=y;
    }
    /*
    //overrides javax.swing.JComponent.paintComponent
    public void paintComponent(Graphics g)
    {
        //让超类完成自己的工作
        //super.paintComponent(g);
        /*
        Graphics2D g2=(Graphics2D)g;
        Stroke s = new BasicStroke(10.0f);
        g2.setStroke(s);
        Ellipse2D ellipse = new Ellipse2D.Doublde(x,y,10,10);
        g2.draw(ellipse);

        g.setColor(Color.BLUE);
        g.fillOval((int)x,(int)y,10,10);
    }
    */

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2=(Graphics2D)g;
        Stroke s = new BasicStroke(2.0f);
        g2.setStroke(s);
        for(int i=1;i<=Parser.ans_num;i++)
        {
            Ellipse2D ellipse = new Ellipse2D.Double(Parser.ans[i].x_val,Parser.ans[i].y_val,1,1);
            g2.draw(ellipse);
        }
    }

}