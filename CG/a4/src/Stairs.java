import java.io.FileWriter;
import java.io.IOException;

public class Stairs {

    private int number;
    private double degree;
    private FileWriter fw;

    private int r = 1;

    private double stairThickness = 0.2;
    private double stairLength = 6.0;
    private double stairHalfWidth = 1.0;
    private double height = 1.0;


    public Stairs(int number, double degree, String filename) throws IOException {
        this.number = number;
        this.degree = degree;
        this.fw = new FileWriter(filename);
    }

    public void genStairs() throws IOException {
        int n = (int) (360 / degree);
        for (int i = 1; i <= n; i++) {
            double alpha = (i - 1) * degree / 180 * Math.PI;
            double x = r * Math.cos(alpha);
            double y = r * Math.sin(alpha);

            writeVertex(i, x, y, 0);

            for (int j = i; j <= number; j += n) {
                int startIndex = 2 * n + (j - 1) * 10;

                double z = (j - 1) * height;

                double x1 = x + stairHalfWidth * y / r;
                double y1 = y - stairHalfWidth * x / r;
                double x2 = x - stairHalfWidth * y / r;
                double y2 = y + stairHalfWidth * x / r;
                double x3 = x2 + stairLength * x / r;
                double y3 = y2 + stairLength * y / r;
                double x4 = x1 + stairLength * x / r;
                double y4 = y1 + stairLength * y / r;
                double x5 = x + stairLength * x / r;
                double y5 = y + stairLength * y / r;
                double x6 = x5;
                double y6 = y5;

                writeVertex(startIndex + 1, x1, y1, z);
                writeVertex(startIndex + 2, x2, y2, z);
                writeVertex(startIndex + 3, x3, y3, z);
                writeVertex(startIndex + 4, x4, y4, z);
                writeVertex(startIndex + 5, x1, y1, z + stairThickness);
                writeVertex(startIndex + 6, x2, y2, z + stairThickness);
                writeVertex(startIndex + 7, x3, y3, z + stairThickness);
                writeVertex(startIndex + 8, x4, y4, z + stairThickness);
                writeVertex(startIndex + 9, x5, y5, z + stairThickness / 2);
                writeVertex(startIndex + 10, x6, y6, z + 6.0);

            }

            writeVertex(i + n, x, y, (number + 5) * height);
        }

        fw.write("Faces:\n");

        // Top boundary face
        for (int i = n; i >= 1; i--) {
            wi(i);
        }
        fw.write(".\n");

        // Bottom boundary face
        for (int i = n + 1; i <= 2 * n; i++) {
            wi(i);
        }
        fw.write(".\n");

        // Vertical, rectangular faces
        for (int i = 1; i <= n; i++) {
            int j = i % n + 1;
            wi(i);
            wi(j);
            wi(j + n);
            wi(i + n);
            fw.write(".\r\n");
        }

        // Stair faces
        for (int i = 1; i <= n; i++) {
            for (int j = i; j <= number; j += n) {
                int startIndex = 2 * n + (j - 1) * 10;
                wi(startIndex + 1);
                wi(startIndex + 2);
                wi(startIndex + 3);
                wi(startIndex + 4);
                fw.write(".\n");
                wi(startIndex + 8);
                wi(startIndex + 7);
                wi(startIndex + 6);
                wi(startIndex + 5);
                fw.write(".\n");
                wi(startIndex + 3);
                wi(startIndex + 7);
                wi(startIndex + 8);
                wi(startIndex + 4);
                fw.write(".\n");
                wi(startIndex + 1);
                wi(startIndex + 5);
                wi(startIndex + 6);
                wi(startIndex + 2);
                fw.write(".\n");
                wi(startIndex + 4);
                wi(startIndex + 8);
                wi(startIndex + 5);
                wi(startIndex + 1);
                fw.write(".\n");
                wi(startIndex + 2);
                wi(startIndex + 6);
                wi(startIndex + 7);
                wi(startIndex + 3);
                fw.write(".\n");
                wi(startIndex + 9);
                wi(startIndex + 10);
                fw.write(".\n");
            }
        }

        for (int i = 1; i < number; i++) {
            int startIndex = 2 * n + i * 10;
            wi(startIndex);
            wi(startIndex + 10);
            fw.write(".\n");
        }

        fw.close();
    }

    private void wi(int i) throws IOException {
        fw.write(" " + String.valueOf(i));
    }

    private void wr(double r) throws IOException {
        if (Math.abs(r) < 1e-9) r = 0;
        fw.write(" " + String.valueOf((float) r));
        // float instead of double to reduce the file size
    }

    private void writeVertex(int index, double x, double y, double z) throws IOException {
        wi(index);
        wr(x);
        wr(y);
        wr(z);
        fw.write("\n");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Arguments Error!");
            System.exit(1);
        } else {
            Stairs s = new Stairs(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2]);
            s.genStairs();
        }
    }
}
