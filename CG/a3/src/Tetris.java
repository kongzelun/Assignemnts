import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Tetris extends JFrame {

    public static int grid_size = 25;
    private int width = 29, height = 22;

    public Tetris() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanelTetris game = new JPanelTetris(grid_size, width, height);
        game.setPreferredSize(new Dimension(Tetris.grid_size * width, Tetris.grid_size * height));
        add("Center", game);
        pack();
        setVisible(true);
        game.start();
    }

    public static void main(String[] args) {
        new Tetris();
    }
}

class JPanelTetris extends JPanel implements Runnable {

    private Point[][][] tetromino = {
            // I-Shape
            {
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3)},
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3)}
            },

            // J-Shape
            {
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2)},
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0)}
            },

            // L-Shape
            {
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2)},
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0)},
                    {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0)}
            },

            // O-Shape
            {
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)}
            },

            // S-Shape
            {
                    {new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)},
                    {new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)}
            },

            // T-Shape
            {
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)},
                    {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2)},
                    {new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2)}
            },

            // Z-Shape
            {
                    {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)},
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2)},
                    {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)},
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2)}
            }
    };

    private final Point[][][] additionalTetromino = {
            {
                    {new Point(1, 0), new Point(0, 1), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 1)},
                    {new Point(0, 0), new Point(0, 1), new Point(1, 0)},
                    {new Point(0, 0), new Point(1, 0), new Point(1, 1)},
            },
            {
                    {new Point(0, 0), new Point(1, 1), new Point(2, 1)},
                    {new Point(1, 0), new Point(0, 1), new Point(0, 2)},
                    {new Point(0, 0), new Point(1, 0), new Point(2, 1)},
                    {new Point(1, 0), new Point(1, 1), new Point(0, 2)}
            },
            {
                    {new Point(0, 0), new Point(1, 0), new Point(2, 0)},
                    {new Point(0, 0), new Point(0, 1), new Point(0, 2)},
                    {new Point(0, 0), new Point(1, 0), new Point(2, 0)},
                    {new Point(0, 0), new Point(0, 1), new Point(0, 2)}
            },
            {
                    {new Point(1, 0), new Point(0, 1)},
                    {new Point(0, 0), new Point(1, 1)},
                    {new Point(1, 0), new Point(0, 1)},
                    {new Point(0, 0), new Point(1, 1)}
            },
            {
                    {new Point(0, 0), new Point(0, 1)},
                    {new Point(0, 0), new Point(1, 0)},
                    {new Point(0, 0), new Point(0, 1)},
                    {new Point(0, 0), new Point(1, 0)}
            },
            {
                    {new Point(1, 0), new Point(0, 1), new Point(2, 1)},
                    {new Point(0, 0), new Point(1, 1), new Point(0, 2)},
                    {new Point(0, 0), new Point(0, 2), new Point(1, 1)},
                    {new Point(0, 1), new Point(1, 0), new Point(1, 2)}
            },
            {
                    {new Point(0, 0)},
                    {new Point(0, 0)},
                    {new Point(0, 0)},
                    {new Point(0, 0)}
            },
            {
                    {new Point(2, 0), new Point(1, 1), new Point(0, 2)},
                    {new Point(0, 0), new Point(1, 1), new Point(2, 2)},
                    {new Point(2, 0), new Point(1, 1), new Point(0, 2)},
                    {new Point(0, 0), new Point(1, 1), new Point(2, 2)}
            }

    };

    private final Color[] tetrominoColor = {
            // I-Shape
            Color.cyan,
            // J-Shape
            Color.red,
            // L-Shape
            Color.blue,
            // O-Shape
            Color.green,
            // S-Shape
            Color.yellow,
            // T-Shape
            Color.orange,
            // Z-Shape
            Color.magenta
    };

    private final Color[] additionalTetrominoColor = {
            Color.gray,
            Color.green,
            Color.pink,
            Color.red,
            Color.cyan,
            Color.yellow,
            Color.darkGray,
            Color.magenta
    };

    private List<Point[][]> tetrominoList = new ArrayList<>(Arrays.asList(tetromino));
    private List<Point[][]> additionalTetrominoList = new ArrayList<>(Arrays.asList(additionalTetromino));
    private List<Color> tetrominoColorList = new ArrayList<>(Arrays.asList(tetrominoColor));
    private List<Color> additionalTetrominoColorList = new ArrayList<>(Arrays.asList(additionalTetrominoColor));

    private final int newShapeOriginX = 4;
    private final int newShapeOriginY = 1;
    private Point shapeOrigin;

    private int currentShape;
    private int currentRotation;
    private int nextShape = new Random().nextInt(tetrominoList.size());
    private int nextRotation = new Random().nextInt(4);

    private int additionalShape = 0;

    private Color[][] grids;

    private int grid_size, width, height;

    // scoring factor (range: 1-10).
    private int M = 1;
    // number of rows required for each Level of difficulty (range: 20-50).
    private int N = 20;
    // speed factor (range 0.1-1.0)
    private int S = 1;

    private int level = 1;
    private double FS = 1.0;
    private int lines = 0;
    private int score = 0;

    private boolean pause = false;

    private Thread dropping = null;

    public JPanelTetris(int grid_size, int width, int height) {
        this.grid_size = grid_size;
        this.width = width;
        this.height = height;
        addListeners();
        init();
    }

    private void addListeners() {
        // pause
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                int x = e.getX();
                int y = e.getY();
                if (x < 21 * grid_size && y > 37) {
                    pause = true;
                    if (pointInsidePolygon(x, y)) {
                        changeToNextShape();
                    }
                    repaint();
                } else {
                    pause = false;
                }
            }
        });

        // rotate
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (!pause) {
                    rotate(e.getWheelRotation());
                }
            }
        });

        // quit
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX();
                int y = e.getY();

                if (!pause) {
                    if (x > 22 * grid_size && x < 27 * grid_size && y > 17 * grid_size && y < 19 * grid_size) {
                        System.exit(0);
                    } else if (x > 11 * grid_size) {
                        int button = e.getButton();
                        move(button - 2);
                    }
                } else {
                    if (x > 13 * grid_size && x < 15 * grid_size && y > 6 * grid_size && y < 7 * grid_size) {
                        // previous new shape
                        previousAdditionalShape();
                    } else if (x > 17 * grid_size && x < 19 * grid_size && y > 6 * grid_size && y < 7 * grid_size) {
                        // next new shape
                        nextAdditionalShape();
                    } else if (x > 13 * grid_size && x < 19 * grid_size && y > 7.5 * grid_size && y < 9 * grid_size) {
                        // add new shape
                        addAdditionalShape();
                    } else if (x > 13 * grid_size && x < 14 * grid_size && y > 11 * grid_size && y < 12 * grid_size) {
                        // subtract M
                        if (M > 1) {
                            M -= 1;
                            repaint();
                        }
                    } else if (x > 18 * grid_size && x < 19 * grid_size && y > 11 * grid_size && y < 12 * grid_size) {
                        // add M
                        if (M < 10) {
                            M += 1;
                            repaint();
                        }
                    } else if (x > 13 * grid_size && x < 14 * grid_size && y > 15 * grid_size && y < 16 * grid_size) {
                        // subtract N
                        if (N > 20) {
                            N -= 5;
                            repaint();
                        }
                    } else if (x > 18 * grid_size && x < 19 * grid_size && y > 15 * grid_size && y < 16 * grid_size) {
                        // add N
                        if (N < 50) {
                            N += 5;
                            repaint();
                        }
                    } else if (x > 13 * grid_size && x < 14 * grid_size && y > 19 * grid_size && y < 20 * grid_size) {
                        // subtract S
                        if (S > 1) {
                            S -= 1;
                            FS = (int) (FS * (1 + level * (double) S / 10.0));
                            repaint();
                        }
                    } else if (x > 18 * grid_size && x < 19 * grid_size && y > 19 * grid_size && y < 20 * grid_size) {
                        // add S
                        if (S < 10) {
                            S += 1;
                            FS = FS * (1 + level * (double) S / 10.0);
                            repaint();
                        }
                    }
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Dimension d = getSize();
                grid_size = Math.min(d.width / width, d.height / height);
                repaint();
            }
        });
    }

    private void init() {

        M = 1;
        N = 20;
        S = 1;
        level = 1;
        FS = 1.0;
        lines = 0;
        score = 0;

        grids = new Color[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == 0 || j == 0 || i == 11 || j == height - 1 || i == width - 1 || i == 20) {
                    grids[i][j] = Color.gray;
                } else if (i < 12) {
                    grids[i][j] = Color.white;
                } else {
                    grids[i][j] = Color.white;
                }
            }
        }

        newShape();
    }

    private void newShape() {
        shapeOrigin = new Point(newShapeOriginX, newShapeOriginY);
        currentRotation = nextRotation;
        nextRotation = new Random().nextInt(4);
        currentShape = nextShape;
        nextShape = new Random().nextInt(tetrominoList.size());
    }

    private void drawNextShape(Graphics g) {
        g.setColor(tetrominoColorList.get(nextShape));
        for (Point p : tetrominoList.get(nextShape)[nextRotation]) {
            g.fillRect((p.x + 23) * grid_size, (p.y + 2) * grid_size, grid_size - 1, grid_size - 1);
        }
    }

    // Draw the falling piece
    private void drawCurrentShape(Graphics g) {
        g.setColor(tetrominoColorList.get(currentShape));
        for (Point p : tetrominoList.get(currentShape)[currentRotation]) {
            g.fillRect((p.x + shapeOrigin.x) * grid_size, (p.y + shapeOrigin.y) * grid_size, grid_size - 1, grid_size - 1);
        }
    }

    private void drawAdditionalShape(Graphics g) {
        if (additionalTetrominoList.size() > 0) {
            g.setColor(additionalTetrominoColorList.get(additionalShape));
            for (Point p : additionalTetrominoList.get(additionalShape)[0]) {
                g.fillRect((p.x + 15) * grid_size, (p.y + 2) * grid_size, grid_size - 1, grid_size - 1);
            }
        }
    }

    private void nextAdditionalShape() {
        if (additionalTetrominoList.size() > 0) {
            additionalShape += 1;
            additionalShape %= additionalTetrominoList.size();
            repaint();
        }
    }

    private void previousAdditionalShape() {
        if (additionalTetrominoList.size() > 0) {
            additionalShape -= 1;
            if (additionalShape < 0) {
                additionalShape = additionalTetrominoList.size() - 1;
            }
            repaint();
        }
    }

    private void addAdditionalShape() {
        if (additionalTetrominoList.size() > 0) {
            tetrominoList.add(additionalTetrominoList.remove(additionalShape));
            tetrominoColorList.add(additionalTetrominoColorList.remove(additionalShape));
            repaint();
        }
    }

    // Collision test for the dropping piece
    private boolean collidesAt(int x, int y, int shape, int rotation) {
        for (Point p : tetrominoList.get(shape)[rotation]) {
            if (grids[p.x + x][p.y + y] != Color.white) {
                return true;
            }
        }
        return false;
    }

    // Make the dropping shape part of the grids, so it is available for
    // collision detection.
    private void fixToWell() {
        for (Point p : tetrominoList.get(currentShape)[currentRotation]) {
            grids[shapeOrigin.x + p.x][shapeOrigin.y + p.y] = tetrominoColorList.get(currentShape);
        }
//        clearRows();
    }

    // Drops the piece one line or fixes it to the grids if it can't drop
    private void dropDown() {
        if (!collidesAt(shapeOrigin.x, shapeOrigin.y + 1, currentShape, currentRotation)) {
            shapeOrigin.y += 1;
        } else {
            fixToWell();
            // detect game over conditions
            if (!collidesAt(newShapeOriginX, newShapeOriginY, nextShape, 0)) {
                clearRows();
                newShape();
            } else {
                // game over
                init();
            }
        }
        repaint();
    }

    // Rotate the shape clockwise or counterclockwise
    private void rotate(int i) {
        int newRotation = (currentRotation - i) % 4;
        if (newRotation < 0) {
            newRotation += 4;
        }
        if (!collidesAt(shapeOrigin.x, shapeOrigin.y, currentShape, newRotation)) {
            currentRotation = newRotation;
        }
        repaint();
    }

    // Move the piece left or right
    private void move(int i) {
        if (!collidesAt(shapeOrigin.x + i, shapeOrigin.y, currentShape, currentRotation)) {
            shapeOrigin.x += i;
        }
        repaint();
    }

    private void deleteRow(int row) {
        for (int j = row - 1; j > 0; j--) {
            for (int i = 1; i < 11; i++) {
                grids[i][j + 1] = grids[i][j];
            }
        }
        for (int i = 1; i < 11; i++) {
            grids[i][1] = Color.white;
        }
    }

    // Clear completed rows from the field and award score according to
    // the number of simultaneously cleared rows.
    private void clearRows() {
        boolean full = true;
        int numClears = 0;

        for (int j = 20; j > 0; j--) {
            full = true;
            for (int i = 1; i < 11; i++) {
                if (grids[i][j] == Color.white) {
                    full = false;
                    break;
                }
            }
            if (full) {
                deleteRow(j);
                j++;
                numClears += 1;
            }
        }

//        while (full) {
//            for (int i = 1; i < 11; i++) {
//                if (grids[i][20] == Color.white) {
//                    full = false;
//                    break;
//                }
//            }
//
//            if (full) {
//                deleteRow(20);
//                numClears += 1;
//            }
//        }

        lines += numClears;

        switch (numClears) {
            case 1:
                score += 1 * M * level;
                break;
            case 2:
                score += 3 * M * level;
                break;
            case 3:
                score += 6 * M * level;
                break;
            case 4:
                score += 10 * M * level;
                break;
        }


        level = lines / N + 1;
    }

    // another way
    private boolean pointInsidePolygon(int x, int y) {
        x = x / grid_size - shapeOrigin.x;
        y = y / grid_size - shapeOrigin.y;

        for (Point p : tetrominoList.get(currentShape)[currentRotation]) {
            if (p.x == x && p.y == y) {
                return true;
            }
        }

        return false;
    }

    float area2(Point a, Point b, Point c) {
        return (a.x - c.x) * (b.y - c.y) - (a.y - c.y) * (b.x - c.x);
    }

    private boolean insidePolygon(Point p, Point[] pol) {
        int n = pol.length, j = n - 1;
        boolean b = false;
        float x = p.x, y = p.y;
        for (int i = 0; i < n; i++) {
            if (pol[j].y <= y && y < pol[i].y &&
                    area2(pol[j], pol[i], p) > 0 ||
                    pol[i].y <= y && y < pol[j].y &&
                            area2(pol[i], pol[j], p) > 0) b = !b;
            j = i;
        }
        return b;
    }

    private void changeToNextShape() {
        currentRotation = nextRotation;
        nextRotation = new Random().nextInt(4);
        currentShape = nextShape;
        nextShape = new Random().nextInt(tetrominoList.size());

        score = score - level * M;
        if (score < 0) {
            score = 0;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Dimension d = getSize();
        int maxX = d.width - 1, maxY = d.height - 1;
        g.setColor(Color.white);
        g.fillRect(0, 0, d.width, d.height);

        // Paint the grids
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                g.setColor(grids[i][j]);
                g.fillRect(grid_size * i, grid_size * j, grid_size - 1, grid_size - 1);
            }
        }

        g.setColor(Color.black);
        g.setFont(new Font("Monospaced", Font.BOLD, grid_size));
        g.drawString("Level\t: " + level, 22 * grid_size, 9 * grid_size);
        g.drawString("Lines\t: " + lines, 22 * grid_size, 12 * grid_size);
        g.drawString("Score\t: " + score, 22 * grid_size, 15 * grid_size);

        // quit
        g.drawRect(22 * grid_size, 17 * grid_size, 5 * grid_size, 2 * grid_size);
        g.drawString("QUIT", (int) (23.25 * grid_size), (int) (18.5 * grid_size));

        // customize
//        g.drawRect(13 * grid_size, 17 * grid_size, 6 * grid_size, 2 * grid_size);
//        g.drawString("Customize", (int) (13.25 * grid_size), (int) (18.5 * grid_size));

        // next shape frame
        g.drawRect((int) (21.5 * grid_size), (int) (1.5 * grid_size), (int) (6 * grid_size), (int) (5 * grid_size));

        // new shape frame
        g.drawRect((int) (13.5 * grid_size), (int) (1.5 * grid_size), (int) (5 * grid_size), (int) (4 * grid_size));

        g.drawRect(13 * grid_size, 6 * grid_size, 2 * grid_size, 1 * grid_size);
        g.drawRect(17 * grid_size, 6 * grid_size, 2 * grid_size, 1 * grid_size);
        g.drawString(">", (int) (17.75 * grid_size), (int) (6.8 * grid_size));
        g.drawString("<", (int) (13.75 * grid_size), (int) (6.8 * grid_size));
        g.drawRect(13 * grid_size, (int) (7.5 * grid_size), 6 * grid_size, (int) (1.5 * grid_size));
        g.drawString("Add", 15 * grid_size, (int) (8.5 * grid_size));

        g.setFont(new Font("Monospaced", Font.BOLD, 2 * grid_size / 3));
        // scoring factor (range: 1-10).
        g.drawString("Scoring Factor", 13 * grid_size, (int) (10.5 * grid_size));
        g.drawString("-", (int) (13.35 * grid_size), (int) (11.72 * grid_size));
        g.drawRect(13 * grid_size, 11 * grid_size, grid_size, grid_size);
        g.drawString("" + M, (int) (15.75 * grid_size), (int) (11.72 * grid_size));
        g.drawString("+", (int) (18.35 * grid_size), (int) (11.72 * grid_size));
        g.drawRect(18 * grid_size, 11 * grid_size, grid_size, grid_size);

        // number of rows required for each Level of difficulty (range: 20-50).
        g.drawString("Lines to Upgrade", 13 * grid_size, (int) (14.5 * grid_size));
        g.drawString("-", (int) (13.35 * grid_size), (int) (15.72 * grid_size));
        g.drawRect(13 * grid_size, 15 * grid_size, grid_size, grid_size);
        g.drawString("" + N, (int) (15.75 * grid_size), (int) (15.72 * grid_size));
        g.drawString("+", (int) (18.35 * grid_size), (int) (15.72 * grid_size));
        g.drawRect(18 * grid_size, 15 * grid_size, grid_size, grid_size);

        // speed factor (range 0.1-1.0)
        g.drawString("Speed Factor", 13 * grid_size, (int) (18.5 * grid_size));
        g.drawString("-", (int) (13.35 * grid_size), (int) (19.72 * grid_size));
        g.drawRect(13 * grid_size, 19 * grid_size, grid_size, grid_size);
        g.drawString(String.format("%.1f", (double) S / 10.0), (int) (15.75 * grid_size), (int) (19.72 * grid_size));
        g.drawString("+", (int) (18.35 * grid_size), (int) (19.72 * grid_size));
        g.drawRect(18 * grid_size, 19 * grid_size, grid_size, grid_size);

        drawCurrentShape(g);
        drawNextShape(g);
        drawAdditionalShape(g);

        if (pause) {
            g.setColor(Color.blue);
            g.setFont(new Font("Monospace", Font.BOLD, 48));
            g.drawRect(2 * grid_size, 8 * grid_size, 8 * grid_size, 4 * grid_size);
            g.drawString("PAUSE", (int) (2.5 * grid_size), (int) (10.75 * grid_size));
        }
    }

    // Make the falling piece drop every second
    @Override
    public synchronized void run() {
        while (true) {
            try {
                Thread.sleep((int) (1000 / FS));
                if (!pause) {
                    dropDown();
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public void start() {
        if (dropping == null) {
            dropping = new Thread(this);
            dropping.start();
        }
    }
}
