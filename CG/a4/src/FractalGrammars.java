// FractalGrammars.java

import java.awt.*;
import java.awt.event.*;

public class FractalGrammars extends Frame {
    public static void main(String[] args) {
        if (args.length == 0)
            System.out.println("Use filename as program argument.");
        else
            new FractalGrammars(args[0]);
    }

    FractalGrammars(String fileName) {
        super("Click left or right mouse button to change the level");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setSize(800, 600);
        add("Center", new CvFractalGrammars(fileName));
        setVisible(true);
    }
}

class CvFractalGrammars extends Canvas {
    private String axiom, strF, strf, strX, strY;

    private int maxX, maxY, level = 1;
    private double xLast, yLast, dir, rotation, dirStart, fxStart, fyStart, lengthFract, reductFact;
    private double lastDir;
    private boolean first;

    void error(String str) {
        System.out.println(str);
        System.exit(1);
    }

    CvFractalGrammars(String fileName) {
        Input input = new Input(fileName);

        if (input.fails()) {
            error("Cannot open input file.");
        }

        axiom = input.readString();
        input.skipRest();
        strF = input.readString();
        input.skipRest();
        strf = input.readString();
        input.skipRest();
        strX = input.readString();
        input.skipRest();
        strY = input.readString();
        input.skipRest();


        rotation = input.readFloat();
        input.skipRest();
        dirStart = input.readFloat();
        input.skipRest();
        fxStart = input.readFloat();
        input.skipRest();
        fyStart = input.readFloat();
        input.skipRest();
        lengthFract = input.readFloat();
        input.skipRest();
        reductFact = input.readFloat();
        if (input.fails()) {
            error("Input file incorrect.");
        }

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if ((evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0) {
                    level--; // Right mouse button decreases level
                    if (level < 1) level = 1;
                } else {
                    level++; // Left mouse button increases level
                }
                repaint();
            }
        });

    }

    Graphics g;

    private int iX(double x) {
        return (int) Math.round(x);
    }

    private int iY(double y) {
        return (int) Math.round(maxY - y);
    }

    private void drawTo(Graphics g, double x, double y) {
        g.drawLine(iX(xLast), iY(yLast), iX(x), iY(y));
        xLast = x;
        yLast = y;
    }

    private void moveTo(Graphics g, double x, double y) {
        xLast = x;
        yLast = y;
    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        maxX = d.width - 1;
        maxY = d.height - 1;
        xLast = fxStart * maxX;
        yLast = fyStart * maxY;
        dir = dirStart; // Initial direction in degrees
        first = true;
        turtleGraphics(g, axiom, level, lengthFract * maxY);
    }

    private void turtleGraphics(Graphics g, String instruction, int depth, double len) {
        double xMark = 0, yMark = 0, dirMark = 0;
        for (int i = 0; i < instruction.length(); i++) {
            char ch = instruction.charAt(i);
            double rad, dx, dy;
            switch (ch) {
                case 'F':
                    // Step forward and draw
                    // Start: (xLast, yLast), direction: dir, steplength: len
                    if (depth == 0) {
                        double ddir = (int) Math.round((dir - lastDir) / rotation);

                        if (first) {
                            rad = Math.PI / 180 * dir; // Degrees -> radians
                            dx = 2 * len * Math.cos(rad) / 3;
                            dy = 2 * len * Math.sin(rad) / 3;
                            drawTo(g, xLast + dx, yLast + dy);
                            first = false;
                        } else if (ddir == -1) {
                            dir += rotation / 2;
                            rad = Math.PI / 180 * dir; // Degrees -> radians
                            dx = len * Math.cos(rad) / 3;
                            dy = len * Math.sin(rad) / 3;
                            drawTo(g, xLast + dx, yLast + dy);

                            dir -= rotation / 2;
                            rad = Math.PI / 180 * dir; // Degrees -> radians
                            dx = len * Math.cos(rad) / 3;
                            dy = len * Math.sin(rad) / 3;
                            drawTo(g, xLast + dx, yLast + dy);
                        } else if (ddir == 1) {
                            dir -= rotation / 2;
                            rad = Math.PI / 180 * dir; // Degrees -> radians
                            dx = len * Math.cos(rad) / 3;
                            dy = len * Math.sin(rad) / 3;
                            drawTo(g, xLast + dx, yLast + dy);

                            dir += rotation / 2;
                            rad = Math.PI / 180 * dir; // Degrees -> radians
                            dx = len * Math.cos(rad) / 3;
                            dy = len * Math.sin(rad) / 3;
                            drawTo(g, xLast + dx, yLast + dy);
                        } else if (ddir == 0) {
                            rad = Math.PI / 180 * dir; // Degrees -> radians
                            dx = len * Math.cos(rad);
                            dy = len * Math.sin(rad);
                            drawTo(g, xLast + dx, yLast + dy);
                        }

                        lastDir = dir;
                    } else
                        turtleGraphics(g, strF, depth - 1, reductFact * len);
                    break;
                case 'f':
                    // Step forward without drawing
                    // Start: (xLast, yLast), direction: dir, steplength: len
                    if (depth == 0) {
                        rad = Math.PI / 180 * dir; // Degrees -> radians
                        dx = len * Math.cos(rad);
                        dy = len * Math.sin(rad);
                        moveTo(g, xLast + dx, yLast + dy);
                    } else
                        turtleGraphics(g, strf, depth - 1, reductFact * len);
                    break;
                case 'X':
                    if (depth > 0)
                        turtleGraphics(g, strX, depth - 1, reductFact * len);
                    break;
                case 'Y':
                    if (depth > 0)
                        turtleGraphics(g, strY, depth - 1, reductFact * len);
                    break;
                case '+': // Turn right
                    dir -= rotation;
                    break;
                case '-': // Turn left
                    dir += rotation;
                    break;
                case '[': // Save position and direction
                    xMark = xLast;
                    yMark = yLast;
                    dirMark = dir;
                    break;
                case ']': // Back to saved position and direction
                    xLast = xMark;
                    yLast = yMark;
                    dir = dirMark;
                    break;
            }
        }
    }
}