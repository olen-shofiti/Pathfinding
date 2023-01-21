package pathfinding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;


public class Main {
    public static void main(String[] args) {
        // Linux/X11 does not automatically apply common fractional desktop scales.
        if (System.getProperty("os.name", "").toLowerCase(java.util.Locale.ROOT).startsWith("linux")) {
            System.setProperty("sun.java2d.uiScale", "2.0");
        }

        JFrame frame = new JFrame("Pathfinding");

        Pathfinding pathFinding = new Pathfinding();
        pathFinding.setPreferredSize(new Dimension(600, 600));

        pathFinding.setBackground(Color.gray);
        pathFinding.setBorder(new LineBorder(Color.black, 1));

        frame.add(pathFinding, BorderLayout.WEST);

        frame.add(pathFinding.mainPanel, BorderLayout.EAST);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
