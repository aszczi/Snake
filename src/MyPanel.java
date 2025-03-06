import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.swing.Timer;

public class MyPanel extends JPanel implements ActionListener {
   static final int SCREEN_WIDTH = 600;
   static final int SCREEN_HEIGHT = 600;
   public static final int DELAY = 75;
   static final int UNIT_SIZE = 25;
   static final String BACKGROUND_COLOR = "#6A5ACD";
    static final String BODY_COLOR = "#B6ECB4";
   static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT ) / UNIT_SIZE;
   final int x[] = new int[GAME_UNITS];
   final int y[] = new int[GAME_UNITS];

   int bodyParts = 6;
   int foodEaten;
   int foodX;
   int foodY;
   Directions direction = Directions.EAST;
   boolean running = false;
   Timer timer;
   Random random;

    MyPanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.decode(BACKGROUND_COLOR));

        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
   
    public void startGame(){
        newFood();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
   
    public void draw(Graphics g){
        if(running) {
            //apple
            g.setColor(Color.decode("#000000"));
            g.fillRect(foodX + 9, foodY - 6, 2, 8);
            g.setColor(Color.decode("#DC143C"));
            g.fillOval(foodX - 5, foodY, UNIT_SIZE - 7, UNIT_SIZE - 7);
            g.fillOval(foodX + 5, foodY, UNIT_SIZE - 7, UNIT_SIZE - 7);

            //snake
            try {
                BufferedImage snakeHeadImageRight = ImageIO.read(new File("glowa_prawo.png"));
                BufferedImage snakeHeadImageLeft = ImageIO.read(new File("glowa_lewo.png"));
                BufferedImage snakeHeadImageUp = ImageIO.read(new File("glowa_gora.png"));
                BufferedImage snakeHeadImageDown = ImageIO.read(new File("glowa_dol.png"));
          

                for (int i = 0; i < bodyParts; i++) {
                    if (i == 0) {
                        // snake head
                        switch (direction) {
                            case EAST:
                                g.drawImage(snakeHeadImageRight, x[0]-5, y[0]-3, UNIT_SIZE+5, UNIT_SIZE+5, this);
                                break;
                            case WEST:
                                g.drawImage(snakeHeadImageLeft, x[0]-5, y[0]-3, UNIT_SIZE+5, UNIT_SIZE+5, this);
                                break;
                            case NORTH:
                                g.drawImage(snakeHeadImageUp, x[0]-2, y[0]-5, UNIT_SIZE+5, UNIT_SIZE+5, this);
                                break;
                            case SOUTH:
                                g.drawImage(snakeHeadImageDown, x[0]-2, y[0]-5, UNIT_SIZE+5, UNIT_SIZE+5, this);
                                break;
                        }
                    } else {
                        // snake body
                       g.setColor(Color.decode(BODY_COLOR));
                       g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

                    }
                }
            } catch (IOException e) {
                System.out.println("Błąd ładowania obrazka: " + e.getMessage());
            }

            g.setColor(Color.BLACK);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            g.drawString("Score: "+foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+foodEaten))/2, g.getFont().getSize());

        }else{
            gameOver(g);
        }
    }

    public void move(){
        for (int i = bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction){
            case NORTH -> y[0]  = y[0] - UNIT_SIZE;
            case SOUTH -> y[0]  = y[0] + UNIT_SIZE;
            case EAST -> x[0]  = x[0] + UNIT_SIZE;
            case WEST -> x[0]  = x[0] - UNIT_SIZE;

        }

    }
   
    public void newFood(){
        foodX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        foodY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void checkFood(){
        if((x[0] == foodX) && (y[0] == foodY)){
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }
   
    public void checkCollision(){
        //checking collisions with body
         for (int i = bodyParts; i>0; i--){
             if(x[0] == x[i] && y[0] == y[i]){
                 running = false;
             }
         }

         //checking collisions with borders
        if(x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if (!running){
            timer.stop();
        }
    }
   
    public void gameOver(Graphics g){
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        g.drawString("Score: "+foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+foodEaten))/2, g.getFont().getSize());

        setBackground(Color.BLACK);
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        metrics = g.getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

    }
   
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkFood();
            checkCollision();

        }
        repaint();
    }
   
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (direction != Directions.EAST) {
                        direction = Directions.WEST;
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != Directions.WEST) {
                        direction = Directions.EAST;
                    }
                }
                case KeyEvent.VK_UP -> {
                    if (direction != Directions.SOUTH) {
                        direction = Directions.NORTH;
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != Directions.NORTH) {
                        direction = Directions.SOUTH;
                    }
                }
            }
        }
    }
}
