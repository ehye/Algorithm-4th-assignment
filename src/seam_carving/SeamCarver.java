package seam_carving;

import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    
    private static final int BORDER_ENERGY = 1000;
    private Picture picture;
    private double[][] energy;
    private int[][] colors;
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.colors = new int[picture.width()][picture.height()];
        this.energy = new double[picture.width()][picture.height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[x][y] = energy(x, y);
                colors[x][y] = picture.get(x,y).getRGB();
            }
        }
    }
    
    private Color getRGB(int rgb) {
        return new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
    }
    
    // current picture
    public Picture picture() {
        Picture newPic = new Picture(width(), height());
        for (int row = 0; row < height(); row++)
            for (int col = 0; col < width(); col++)
                newPic.set(col, row, getRGB(colors[col][row]));
        return newPic;
    }
    
    // width of current picture
    public int width() {
        return this.energy.length;
    }
    
    // height of current picture
    public int height() {
        return this.energy[0].length;
    }
    
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1)
            throw new IllegalArgumentException();        
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return BORDER_ENERGY;
        
        Color xminus = picture.get(x-1, y);
        Color xplus = picture.get(x+1, y);
        Color yminus = picture.get(x, y-1);
        Color yplus = picture.get(x, y+1);
        int rX = xplus.getRed() - xminus.getRed();
        int gX = xplus.getGreen() - xminus.getGreen();
        int bX = xplus.getBlue() - xminus.getBlue();
        int rY = yplus.getRed() - yminus.getRed();
        int gY = yplus.getGreen() - yminus.getGreen();
        int bY = yplus.getBlue() - yminus.getBlue();
        double deltaX2 = Math.pow(rX, 2) + Math.pow(gX, 2) + Math.pow(bX, 2);
        double deltaY2 = Math.pow(rY, 2) + Math.pow(gY, 2) + Math.pow(bY, 2);
        
        return Math.sqrt(deltaX2 + deltaY2);
    }
    
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seamRowIndex = findVerticalSeam();
        transpose();
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

    private void transpose() {
        double [][] transEnergy = new double [energy[0].length][energy.length];
        int [][] transColor = new int [energy[0].length][energy.length];
        for (int i = 0; i < energy[0].length; i++) {
            for (int j = 0; j < energy.length; j++) {
                transEnergy[i][j] = energy[j][i];
                transColor[i][j] = colors[j][i]; 
            }
        }
        this.energy = transEnergy;
        this.colors = transColor;
    }
    
    private int matrixIndex(int x, int y) {
        return width() * y + x;
    }
    
    private void checkValid(int[] seam, String orientation) {
        if (seam == null || seam.length < 1)
            throw new IllegalArgumentException();
        if (orientation.equals("vertical") && picture.width() <= 1)
            throw new IllegalArgumentException();    
        if (orientation.equals("horizontal") && picture.height() < 1)
            throw new IllegalArgumentException();
            
        // two adjacent entries differ by more than 1
        for (int i = 0; i < seam.length - 1; i++) {
            if (seam[i] < 0)
                throw new IllegalArgumentException();
            if (Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException();
        }
    }
    
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkValid(seam, "horizontal"); 
        
        double[][] newEnergy = new double[width()][height()-1];
        for (int i = 0; i < seam.length; i++)
            for (int j = 0; j < height()-1; j++)
                if (j != seam[i])
                    newEnergy[i][j] = energy[i][seam[i]-1];
                else {
                    // the source array.
                    double[] src = energy[i];
                    // starting position in the source array.
                    int srcPos = seam[i]+1;
                    // the destination array.
                    double[] dest = newEnergy[i];
                    // starting position in the destination data.
                    int destPos = seam[i];
                    // the number of array elements to be copied.
                    int length = energy[i].length - seam[i] - 1;
                    // remove energy matrix
                    System.arraycopy(src, srcPos, dest, destPos, length);
                    // remove color matrix
                    System.arraycopy(colors[i], srcPos, colors[i], destPos, length);
                    break;
        }
        this.energy = newEnergy;
    }
    
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkValid(seam, "vertical");
        
        transpose();
        removeHorizontalSeam(seam);
        transpose();
    }
    
}
