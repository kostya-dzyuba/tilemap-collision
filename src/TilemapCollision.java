import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TilemapCollision extends JFrame {
    private static final int SIZE = 32;
    private static final int WIDTH = 640;
    private static final int FRAME_HEIGHT = 480;
    private static final int BORDER_HEIGHT = 28;
    private static final int PANEL_HEIGHT = FRAME_HEIGHT - BORDER_HEIGHT;
    private static final boolean[][] TILEMAP = new boolean[FRAME_HEIGHT / SIZE][WIDTH / SIZE];
    private static final int JUMP = 16;

    private static int playerX = (WIDTH - SIZE) / 2;
    private static int playerY = (PANEL_HEIGHT - SIZE) / 2;
    private static int direction = 0;
    private static int velocity = 0;

    public TilemapCollision() {
        setSize(WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = (JPanel) add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GREEN);
                g.fillRect(playerX, playerY, SIZE, SIZE);
                g.setColor(Color.BLACK);
                for (int i = 0; i < TILEMAP.length; i++) {
                    for (int j = 0; j < TILEMAP[i].length; j++) {
                        if (TILEMAP[i][j]) {
                            g.fillRect(j * SIZE, i * SIZE - BORDER_HEIGHT, SIZE, SIZE);
                        }
                    }
                }
            }
        });

        new Timer(15, e -> {
            int dx = direction * 2;
            int dy = velocity;

            int playerRight = playerX + SIZE;
            int playerBottom = playerY + SIZE;

            if (velocity < 16) {
                velocity++;
            }

            if (playerX + dx < 0 || playerRight + dx > WIDTH) {
                dx = 0;
            }

            if (playerBottom + dy > PANEL_HEIGHT) {
                dy = PANEL_HEIGHT - playerY - SIZE;
                velocity = 0;
            }

            for (int i = 0; i < TILEMAP.length; i++) {
                for (int j = 0; j < TILEMAP[i].length; j++) {
                    if (TILEMAP[i][j]) {
                        int tileX = j * SIZE;
                        int tileRight = tileX + SIZE;
                        int tileY = i * SIZE - BORDER_HEIGHT;
                        int tileBottom = tileY + SIZE;

                        if (playerRight + dx > tileX && playerX + dx < tileRight && playerBottom > tileY && playerY < tileBottom) {
                            dx = 0;
                        }

                        if (playerRight > tileX && playerX < tileRight && playerBottom + dy > tileY && playerY + dy < tileBottom) {
                            if (velocity > 0) {
                                dy = tileY - playerBottom;
                            } else {
                                dy = tileBottom - playerY;
                            }
                            velocity = 0;
                        }
                    }
                }
            }

            playerX += dx;
            playerY += dy;

            panel.repaint();
        }).start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_A) {
                    direction = -1;
                } else if (keyCode == KeyEvent.VK_D) {
                    direction = 1;
                }

                if (keyCode == KeyEvent.VK_SPACE) {
                    velocity = -JUMP;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (direction == -1 && keyCode == KeyEvent.VK_A || direction == 1 && keyCode == KeyEvent.VK_D) {
                    direction = 0;
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int y = e.getY() / SIZE;
                int x = e.getX() / SIZE;
                TILEMAP[y][x] = !TILEMAP[y][x];
            }
        });
    }

    public static void main(String[] args) {
        new TilemapCollision().setVisible(true);
    }
}