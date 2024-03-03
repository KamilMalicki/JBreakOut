import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JBreakOut extends JFrame implements KeyListener {

    private static final int WIDTH = 640;
    private static final int HEIGHT = 514;
    private static final int PADDLE_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 80;
    private static final int PADDLE_SPEED = 10;
    private static final int BALL_SIZE = 12;
    private static final int BALL_SPEED = 3;
    private static final int BRICK_WIDTH = 20;
    private static final int BRICK_HEIGHT = 60;
    private static final int NUM_BRICKS_PER_COL = HEIGHT / BRICK_HEIGHT;
    private static final int NUM_BRICK_COLS = 5;

    private int paddleX;
    private int ballX;
    private int ballY;
    private int ballXSpeed;
    private int ballYSpeed;
    private boolean[][] bricks;
    private boolean gameWon;

    public JBreakOut() {
        setTitle("JBreakOut");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(this);
        setFocusable(true);

        paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballXSpeed = BALL_SPEED;
        ballYSpeed = BALL_SPEED;


        bricks = new boolean[NUM_BRICK_COLS][NUM_BRICKS_PER_COL];
        for (int i = 0; i < NUM_BRICK_COLS; i++) {
            for (int j = 1; j < NUM_BRICKS_PER_COL; j++) {
                bricks[i][j] = true;
            }
        }
    }

    public void resetGame() {
        paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballXSpeed = BALL_SPEED;
        ballYSpeed = BALL_SPEED;
        gameWon = false;

        bricks = new boolean[NUM_BRICK_COLS][NUM_BRICKS_PER_COL];
        for (int i = 0; i < NUM_BRICK_COLS; i++) {
            for (int j = 0; j < NUM_BRICKS_PER_COL; j++) {
                bricks[i][j] = true;
            }
        }
    }


    public void movePaddleUP() {
        paddleX -= PADDLE_SPEED;
        if (paddleX < 25) {
            paddleX = 25;
        }
    }

    public void movePaddleDOWN() {
        paddleX += PADDLE_SPEED;
        if (paddleX > HEIGHT - PADDLE_HEIGHT) {
            paddleX = HEIGHT - PADDLE_HEIGHT;
        }
    }

    public void moveBall() {
        ballX += ballXSpeed;
        ballY += ballYSpeed;
        if (ballX <= 0) {
            resetGame();
        }
        if (ballX <= PADDLE_WIDTH && ballY + BALL_SIZE >= paddleX && ballY <= paddleX + PADDLE_HEIGHT) {
            ballXSpeed = BALL_SPEED;
        } else if (ballX >= WIDTH) {
            ballXSpeed = -BALL_SPEED;
        }
        if (ballY <= 25 || ballY >= HEIGHT - BALL_SIZE) {
            ballYSpeed = -ballYSpeed;
        }

        int brickCol = (WIDTH - ballX - BALL_SIZE) / BRICK_WIDTH;
        int brickRow = ballY / BRICK_HEIGHT;
        if (brickCol < NUM_BRICK_COLS && brickRow < NUM_BRICKS_PER_COL && bricks[brickCol][brickRow]) {
            bricks[brickCol][brickRow] = false;
            ballXSpeed = -ballXSpeed;
        }

        checkGameWon();
    }

    public void checkGameWon() {
        gameWon = true;
        for (int i = 0; i < NUM_BRICK_COLS; i++) {
            for (int j = 1; j < NUM_BRICKS_PER_COL; j++) {
                if (bricks[i][j]) {
                    gameWon = false;
                    return;
                }
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(new Color(0));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.fillRect(PADDLE_WIDTH, paddleX, PADDLE_WIDTH, PADDLE_HEIGHT);

        g.setColor(Color.LIGHT_GRAY);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        for (int i = 0; i < NUM_BRICK_COLS; i++) {
            for (int j = 1; j < NUM_BRICKS_PER_COL; j++) {
                if (bricks[i][j]) {
                    g.setColor(new Color(i * 10 % 244, j * 10 % 244, (i + j) * 10 % 244));
                    g.fillRect(WIDTH - (i + 2) * BRICK_WIDTH, j * BRICK_HEIGHT, BRICK_WIDTH - 2, BRICK_HEIGHT - 2);

                }
            }
        }

        if (gameWon) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("You Won!", WIDTH / 2 - 100, HEIGHT / 2);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    public static void main(String[] args) {
        JBreakOut game = new JBreakOut();
        game.setVisible(true);

        while (true) {
            game.moveBall();
            game.repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            movePaddleUP();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            movePaddleDOWN();
        } else if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}