package seam_carving;

import java.awt.Color;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;
//    private int[][] parent;
//    private static final double MAX_ENERGY = 195075.0;
    private static final int BORDER_ENERGY = 1000;


    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        energy = new double[picture.width()][picture.height()];
//        parent = new int[picture.width()][picture.height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[x][y] = energy(x, y);
            }
        }
    }

    /**
     * @return The current picture
     */
    public Picture picture() {
        return picture;
    }

    /**
     * @return The width of the current picture
     */
    public int width() {
        return picture.width();
    }

    /**
     * @return The height of the current picture
     */
    public int height() {
        return picture.height();
    }

    /**
     * The energy of pixel (x, y) is Δx^2(x, y) + Δy^2(x, y)
     * 
     * @param x
     *            The pixel at column x
     * @param y
     *            The pixel at column y
     * @return The energy of pixel at column x and row y in current picture
     */
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1)
            throw new IndexOutOfBoundsException();        
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return BORDER_ENERGY;
        
        Color xminus = picture.get(x-1, y);
        Color xplus = picture.get(x+1, y);
        Color yminus = picture.get(x, y-1);
        Color yplus = picture.get(x, y+1);
        int Rx = xplus.getRed() - xminus.getRed();
        int Gx = xplus.getGreen() - xminus.getGreen();
        int Bx = xplus.getBlue() - xminus.getBlue();
        int Ry = yplus.getRed() - yminus.getRed();
        int Gy = yplus.getGreen() - yminus.getGreen();
        int By = yplus.getBlue() - yminus.getBlue();
        double deltaX2 = Math.pow(Rx, 2) + Math.pow(Gx, 2) + Math.pow(Bx, 2);
        double deltaY2 = Math.pow(Ry, 2) + Math.pow(Gy, 2) + Math.pow(By, 2);
        
        return Math.sqrt(deltaX2 + deltaY2);
    
    }

    /**
     * @return A sequence of indices for horizontal seam in current picture
     */
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    /**
     * @return A sequence of indices for vertical seam in current picture
     */
    public int[] findVerticalSeam() {

        int n = width() * height();
        int[] seamColIndex = new int[height()];
        int[] pixelTo = new int[n];
        double[] distTo = new double[n];
                
        for (int i = 0; i < n; i++) {
            if (i < width())            // start point at first line
                distTo[i] = 0;
            else
                distTo[i] = Double.POSITIVE_INFINITY;
        }
        for (int row = 0; row < height(); row++) {
            for (int index = 0; index < width(); index++) {
                for (int offset = -1; offset <= 1; offset++) {
                    if (index + offset < 0 || index + offset > width() - 1 || row + 1 < 0 || row + 1 > height() - 1) {
                        continue;
                    } else {
                        // relax
                        int v = matrixIndex(index, row);                // from
                        int w = matrixIndex(index + offset, row + 1);   // to
                        if (distTo[w] > distTo[v] + energy[index][row]) {
                            distTo[w] = distTo[v] + energy[index][row];
                            pixelTo[w] = v;
                        }
                    }
                }
            }
        }

        // find min dist in the last row
        double min = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int j = 0; j < width(); j++) {
            if (distTo[j + width() * (height() - 1)] < min) {
                index = j + width() * (height() - 1);
                min = distTo[j + width() * (height() - 1)];
            }
        }

        // find seam one by one
        for (int j = 0; j < height(); j++) {
            int y = height() - j - 1;
            int x = index - y * width();
            seamColIndex[height() - 1 - j] = x;
            index = pixelTo[index];
        }

        return seamColIndex;
    
    }

    private int matrixIndex(int x, int y) {
        return width() * y + x;
    }
    
    /**
     * Removes horizontal seam from the current picture
     * 
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {
        checkValidity(seam);
        if (seam.length > width()) {
            throw new IllegalArgumentException("The seam must not be greater than the image width!");
        }

        this.picture = removeSeam(seam, false);
//        double[][] oldEnergy = energy;
        // recaluculate energy 
        energy = new double[width()][height()];
        // TODO: possible improvement would be to recalculate only the energy that has actually changed
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[x][y] = energy(x, y);
            }
        }
    }

    private void checkValidity(int[] seam) {
        if (width() <= 1 || height() <= 1) {
            throw new IllegalArgumentException("The width and height of the picture must be greatern than 1");
        }
        if (seam.length <= 1) {
            throw new IllegalArgumentException("The seam size must be greater than 1.");
        }

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Removes vertical seam from the current picture
     * 
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        checkValidity(seam);
        if (seam.length > height()) {
            throw new IllegalArgumentException("The seam must not be greater than the image height!");
        }

        this.picture = removeSeam(seam, true);
//        double[][] oldEnergy = energy;
        energy = new double[width()][height()];

        // TODO: possible improvement would be to recalculate only the energy that has actually changed
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[x][y] = energy(x, y);
            }
        }
    }

    /**
     * Removes one entire seam from the picture
     * 
     * @param seam
     * @param vertical
     *            Defines if the seam is vertical or horizontal
     * @return A new picture with the seam removed
     */
    private Picture removeSeam(int[] seam, boolean vertical) {
        if (vertical) {
            Picture p = new Picture(width() - 1, height());
            for (int y = 0; y < height(); y++) {
                int k = 0;
                for (int x = 0; x < width(); x++) {
                    if (x != seam[y]) {
                        p.set(k, y, picture.get(x, y));
                        k++;
                    }
                }
            }
            return p;
        }

        Picture p = new Picture(width(), height() - 1);
        for (int y = 0; y < width(); y++) {
            int k = 0;
            for (int x = 0; x < height(); x++) {
                if (x != seam[y]) {
                    p.set(y, k, picture.get(y, x));
                    k++;
                }
            }
        }
        return p;
    }

    /**
     * Square of the gradient Δ^2(x, y) = R(x, y)^2 + G(x, y)^2 + B(x, y)^2
     * 
     * @param a
     * @param b
     * @return The square of the gradient
     */
    /*private double gradient(Color a, Color b) {
        int red = a.getRed() - b.getRed();
        int green = a.getGreen() - b.getGreen();
        int blue = a.getBlue() - b.getBlue();
        return red * red + green * green + blue * blue;
    }*/

    /*private void relaxVertically(int col, int row, double[] distTo, double[] oldDistTo) {
        if (row == 0) {
            distTo[col] = MAX_ENERGY;
            parent[col][row] = -1;
            return;
        }

        if (col == 0) {
            // we have only 2 edges
            double a = oldDistTo[col];
            double b = oldDistTo[col + 1];
            double min = Math.min(a, b);
            distTo[col] = min + energy[col][row];
            if (a < min) {
                parent[col][row] = col;
            } else {
                parent[col][row] = col + 1;
            }
            return;
        }

        if (col == width() - 1) {
            // we have only 2 edges
            double a = oldDistTo[col];
            double b = oldDistTo[col - 1];
            double min = Math.min(a, b);
            distTo[col] = min + energy[col][row];
            if (a < min) {
                parent[col][row] = col;
            } else {
                parent[col][row] = col - 1;
            }
            return;
        }

        // for 3 edges
        double left = oldDistTo[col - 1];
        double mid = oldDistTo[col];
        double right = oldDistTo[col + 1];

        double min = Math.min(Math.min(left, mid), right);

        distTo[col] = min + energy[col][row];
        if (min == left) {
            parent[col][row] = col - 1;
        } else if (min == mid) {
            parent[col][row] = col;
        } else {
            parent[col][row] = col + 1;
        }
    }*/

    /**
     * Transposes the picture <br>
     * IMPROVEMENT: transpose only the energy matrix when needed. Instead of transposing it back again, check if the energy matrix was actually
     * transposed
     */
    private void transpose() {
        Picture transposedPicture = new Picture(picture.height(), picture.width());
        double[][] newEnergy = new double[picture.height()][picture.width()];
        for (int i = 0; i < picture.width(); i++)
            for (int k = 0; k < picture.height(); k++) {
                transposedPicture.set(k, i, picture.get(i, k));
                newEnergy[k][i] = energy[i][k];
            }
        energy = newEnergy;
        picture = transposedPicture;
//        parent = new int[picture.width()][picture.height()];
    }

}