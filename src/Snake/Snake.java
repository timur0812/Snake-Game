package Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class Snake extends JPanel implements ActionListener {
    private final int WIDTH = 40;
    private final int HEIGHT = 40;
    private final int SIZE = 10;
    private ArrayList<Cell> snake = new ArrayList<>();
    private int appleX;
    private int appleY;
    private static JFrame jFrame = new JFrame("Snake game");
    private String key;
    private int score;
    private Cell head;
    private Timer timer;
    private boolean inGame = true;
    private String text = "";
    private int delay;

    public Snake() {

        snake.add(new Cell(10, 20, new Color(0,200,0)));
        snake.add(new Cell(9, 20, Color.GREEN));
        snake.add(new Cell(8, 20, Color.GREEN));

        appleX = 20;
        appleY = 20;

        delay = 130;

        key = "Right";
        head = snake.get(0);

        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inGame) {
            //Area
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    if (i == 0 || j == 0 || i == WIDTH - 1 || j == HEIGHT - 1) {
                        g.setColor(Color.ORANGE);
                        g.fillRect(i * SIZE, j * SIZE, SIZE, SIZE);
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillRect(i * SIZE, j * SIZE, SIZE, SIZE);
                    }
                }
            }

            //Apple
            g.setColor(Color.RED);
            g.fillOval(appleX * SIZE, appleY * SIZE, SIZE, SIZE);

            //Snake
            g.setColor(Color.green);
            for (int i = 0; i < snake.size(); i++) {
                g.setColor(snake.get(i).getColor());
                g.fillRect(snake.get(i).getX() * SIZE, snake.get(i).getY() * SIZE, SIZE, SIZE);
            }

            //Score
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("Score:" + score, 10, 9);

            keyListener();
            move();
        } else {
            timer.stop();

            setBackground(Color.BLACK);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString(text, 120, 150);
            g.drawString("Your score:" + score, 110, 180);
        }
    }

    private void keyListener() {
        jFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                key = e.getKeyText(e.getKeyCode());
            }
        });
    }

    private void move() {
            for (int i = snake.size() - 1; i > 0; i--) {
                snake.set(i, new Cell(snake.get(i - 1).getX(), snake.get(i - 1).getY(), snake.get(i).getColor()));
            }

        switch (key) {
            case "Left":
                snake.set(0, new Cell(head.getX() - 1, head.getY(), head.getColor()));
                break;
            case "Right":
                snake.set(0, new Cell(head.getX() + 1, head.getY(), head.getColor()));
                break;
            case "Up":
                snake.set(0, new Cell(head.getX(), head.getY() - 1, head.getColor()));
                break;
            case "Down":
                snake.set(0, new Cell(head.getX(), head.getY() + 1, head.getColor()));
                break;

        }

            head = snake.get(0);

            timer.setDelay(delay - (score * 2));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        checkCollisions();
        checkApple();

        repaint();
    }

    private void checkCollisions() {
        if (head.getX() == 0 || head.getY() == 0 || head.getX() == WIDTH - 1|| head.getY() == HEIGHT - 1) {
            gameOver();
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.getX() == snake.get(i).getX() && head.getY() == snake.get(i).getY()) {
                gameOver();
            }
        }
    }

    private void checkApple() {
        if (head.getX() == appleX && head.getY() == appleY) {
            snake.add(snake.get(snake.size() - 1));
            score++;
            boolean check = true;

            while (check) {

                appleX = (int) (Math.random() * 38 + 1);
                appleY = (int) (Math.random() * 38 + 1);

                for (int i = 0; i < snake.size(); i++) {
                    if (!(appleX == snake.get(i).getX() && appleY == snake.get(i).getY())) {
                        check = false;
                    }
                }
            }
        }
    }

    private void gameOver() {
        inGame = false;
        text = "Game Over";
    }

    public static void main(String[] args) {
        jFrame.setVisible(true);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        jFrame.setBounds(dimension.width / 2 - 209, dimension.height / 2 - 223, 418, 446);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.setFocusable(true);

        jFrame.add(new Snake());
    }
}
