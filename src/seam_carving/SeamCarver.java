package seam_carving;
import java.awt.Color;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    
    private static int BORDER_ENERGY = 1000;
    private Picture picture;
    private double[][] energy;
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.energy = new double[picture.width()][picture.height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[x][y] = energy(x, y);
            }
        }
    }
    
    // current picture
    public Picture picture() {
        return this.picture;
    }
    
//    // width of current picture
//    public int width() {
//        return this.picture.width();        
//    }
//    
//    // height of current picture
//    public int height() {
//        return this.picture.height();        
//    }
    
    public int width() {
        return this.energy.length;
    }
    
    public int height() {
        return this.energy[0].length;
    }
    
    // energy of pixel at column x and row y
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
    
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
//        StdOut.println("origin: h = " + energy_height() + ", w = " + energy_width());
        this.energy = transpose();
//        StdOut.println("transpose: h = " + energy_height() + ", w = " + energy_width());
        int[] seamRowIndex = findVerticalSeam();
        this.energy = transpose();
        return seamRowIndex;
    }

    // sequence of indices for vertical seam
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

    private double[][] transpose() {
        double [][] trans_energy = new double [energy[0].length][energy.length];
        for (int i = 0; i < energy[0].length; i++) {
            for (int j = 0; j < energy.length; j++) {
                trans_energy[i][j] = energy[j][i];
            }
        }
        return trans_energy;
    }
    
    private int matrixIndex(int x, int y) {
        return width() * y + x;
    }
    
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || picture.width() <= 1 || picture.height() <= 1)
            throw new java.lang.IllegalArgumentException(); 
        
        double[][] new_energy = new double[width()][height()-1];
        for (int i = 0; i < seam.length; i++) {
            if (i == seam.length) {
                for (int j = 0; j < new_energy[i].length; j++) {
                    energy[i][j] = 1000;
                }
            } else {
                // seam left
                System.arraycopy(energy[i], 0, new_energy[i], 0, seam[i]);
                // seam right
                System.arraycopy(energy[i], seam[i] + 1, new_energy[i], seam[i], height() - seam[i] - 1);
            }
        }
//        transpose();
//        removeVerticalSeam(seam);
//        transpose();
    }
    
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || picture.width() <= 1 || picture.height() <= 1)
            throw new java.lang.IllegalArgumentException();
        
        transpose();
        removeHorizontalSeam(seam);
        transpose();
//        double[][] new_energy = new double[width()-1][height()];
//        for (int i = 0; i < seam.length; i++) {
//            // seam left
//            System.arraycopy(energy[i], 0, new_energy[i], 0, seam[i]);
//            // seam right
//            System.arraycopy(energy[i], seam[i] + 1, new_energy[i], seam[i], height() - seam[i] - 1);
//        }
    }
    
}