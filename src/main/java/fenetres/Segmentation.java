/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenetres;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Taleb
 */
class Segmentation extends imagePath {

    /**
     * la dilatation et l erosion nous permette de reparer les forme
     * triangulaire afin de facilité leur detection ainsi elle permet de bien
     * remplir les texts pour qu il sois facilement detectable par les Ocrs
     */
    public void dilate_erose(JLabel jLabelErosion, JLabel jLabelDilated) {
        // on va charger la librairie de opencv qui est une dll dans ce que je vous ai demander de telecharger j expliquerai cela dans l annex en bas
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // lecture de l image a traiter
        Mat source = Imgcodecs.imread(blackAndWhite, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        // preparation de l image resultante
        Mat destination = new Mat(source.rows(), source.cols(), source.type());

        // taille de l erosion a 1 si petit juste pour corriger les contour
        int erosion_size = 1;
        // taille de dilatation inferieur a la moyenne pour evité la destruction des details et des formes 
        // l objectif et de corriger les erreur de la transformation en noire et blan
        int dilation_size = 3;

        destination = source;

        // la matrice de la dilatation on cherche a dilater en forme de rectange ( Imgproc.MORPH_RECT )   
        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(dilation_size + 1, dilation_size + 1));
        // on dilate l image
        Imgproc.dilate(source, destination, element1);
        // on sauvegarde    
        Imgcodecs.imwrite(dilation, destination);

        //on va maintenant eroser l image dilaté ( reparé la finition )
        source = Imgcodecs.imread(dilation, Imgcodecs.CV_LOAD_IMAGE_COLOR);

        destination = source;

        // on repare en ellipse pour reparé les coins de l ecriture    
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(erosion_size + 1, erosion_size + 1));
        Imgproc.erode(source, destination, element);
        Imgcodecs.imwrite(erosion, destination);

        jLabelErosion.setIcon(new ImageIcon(erosion));
        jLabelDilated.setIcon(new ImageIcon(dilation));
    }

    public void applySobelFactor(JLabel jLabelSobelFactor) {
        // init
        Mat frame = Imgcodecs.imread(smoothed);
        Mat grayImage = new Mat();
        Mat detectedEdges = new Mat();

        int ddepth = CvType.CV_16S;
        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_x = new Mat();
        Mat abs_grad_y = new Mat();

        // reduce noise with a 3x3 kernel
        Imgproc.GaussianBlur(frame, frame, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT);

        // convert to grayscale
        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Gradient X
        Imgproc.Sobel(grayImage, grad_x, ddepth, 1, 0);
        Core.convertScaleAbs(grad_x, abs_grad_x);

        // Gradient Y
        Imgproc.Sobel(grayImage, grad_y, ddepth, 0, 1);
        Core.convertScaleAbs(grad_y, abs_grad_y);

        // Total Gradient (approximate)
        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, detectedEdges);
        // Core.addWeighted(grad_x, 0.5, grad_y, 0.5, 0, detectedEdges);

        Imgcodecs.imwrite(sobelImage, detectedEdges);
        jLabelSobelFactor.setIcon(new ImageIcon(sobelImage));

    }
    
    
    private Mat colorImage ;
    public void setboundingBoxes(JLabel jLabelBoundingBoxes) {
        colorImage = Imgcodecs.imread(smoothed);
        Mat cannyInput = Imgcodecs.imread(sobelImage,CvType.CV_8UC1);
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(cannyInput, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
        MatOfPoint2f approxCurve = new MatOfPoint2f();

        for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
            MatOfPoint contour = contours.get(idx);
            Rect rect = Imgproc.boundingRect(contour);
            double contourArea = Imgproc.contourArea(contour);
            matOfPoint2f.fromList(contour.toList());
            Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
            long total = approxCurve.total();
            if (total == 3) { // is triangle
                // do things for triangle
            }
            if (total >= 4 && total <= 6) {
                List<Double> cos = new ArrayList<>();
                Point[] points = approxCurve.toArray();
                for (int j = 2; j < total + 1; j++) {
                    cos.add(angle(points[(int) (j % total)], points[j - 2], points[j - 1]));
                }
                Collections.sort(cos);
                Double minCos = cos.get(0);
                Double maxCos = cos.get(cos.size() - 1);
                boolean isRect = total == 4 && minCos >= -0.1 && maxCos <= 0.3;
                boolean isPolygon = (total == 5 && minCos >= -0.34 && maxCos <= -0.27) || (total == 6 && minCos >= -0.55 && maxCos <= -0.45);
                if (isRect) {
                    double ratio = Math.abs(1 - (double) rect.width / rect.height);
                    drawText(rect.tl(), ratio <= 0.02 ? "SQU" : "RECT");
                }
                if (isPolygon) {
                    drawText(rect.tl(), "Polygon");
                }
            }
        }
        
        Imgcodecs.imwrite(smoothedContour, colorImage);
        jLabelBoundingBoxes.setIcon(new ImageIcon(smoothedContour));
    }

    private Double angle(Point pt1, Point pt2, Point pt0) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10);
    }

    private void drawText(Point ofs, String text) {

        Imgproc.putText(colorImage, text, ofs, Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 25));
    }

}
