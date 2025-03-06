import javax.swing.*;
import java.awt.*;

public class MyFrame  extends JFrame {

    public MyFrame() {
        super("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyPanel panel = new MyPanel();
        this.add(panel);
        this.setResizable(false);
        this.pack();
        setVisible(true);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));

      /*
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);


      setLayout(new GridLayout(3, 1, 55, 5) );
        add(new JButton("Przycisk 1"));
        add(new JButton("Przycisk 2"));
        add(new JButton("Przycisk 3"));*/


    }

}
