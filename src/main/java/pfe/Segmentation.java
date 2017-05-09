/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfe;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 *
 * @author Taleb
 */
public class Segmentation extends imagePath implements InfSegmentation {

    public Segmentation() {
        // on va charger la librairie de opencv qui est une dll dans ce que je vous ai demander de telecharger j expliquerai cela dans l annex en bas
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Override
    public void dilate_erose_ocr(){
                // pour le ocr
        Mat rgbImage = Imgcodecs.imread(ocrReadFrom);
        Mat destination2 = new Mat(rgbImage.rows(), rgbImage.cols(), rgbImage.type());
        // l objectif et de corriger les erreur de la transformation en noire et blan
        int dilation_size1 = 2;

        // la matrice de la dilatation on cherche a dilater en forme de rectange ( Imgproc.MORPH_RECT )   
        Mat element11 = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(dilation_size1 + 1, dilation_size1 + 1));
        // on dilate l image
        Imgproc.dilate(rgbImage, destination2, element11);
        Imgcodecs.imwrite(ocrReadFrom, destination2);

    }
    
    /**
     * la dilatation et l erosion nous permette de reparer les forme
     * triangulaire afin de facilité leur detection ainsi elle permet de bien
     * remplir les texts pour qu il sois facilement detectable par les Ocrs
     */
    @Override
    public void dilate_erose(JLabel jLabelErosion, JLabel jLabelDilated) {

        // lecture de l image a traiter  Pour la segmentation
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

    @Override
    public void toB_A_W(JLabel jLabel) {
        Mat rgbImage = Imgcodecs.imread(original);
        Mat destination = new Mat(rgbImage.rows(), rgbImage.cols(), rgbImage.type());
        // l objectif et de corriger les erreur de la transformation en noire et blan
        int dilation_size = 2;

        // la matrice de la dilatation on cherche a dilater en forme de rectange ( Imgproc.MORPH_RECT ) puis a eroser   
        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_ERODE, new Size(dilation_size + 1, dilation_size + 1));
        // on dilate l image puis elle erose
        Imgproc.dilate(rgbImage, destination, element1);

        // on prepare une nouvelle matrice pour l image noire et blan
        Mat labImage = new Mat();
        // ici on va convertir les couleur vers bgr2gray ( noire et blan ) et mettre le resultat dans labimage
        cvtColor(destination, labImage, Imgproc.COLOR_BGR2GRAY);
        Imgcodecs.imwrite(ocrReadFrom, labImage);
        jLabel.setIcon(new ImageIcon(ocrReadFrom));
        // JOptionPane.showConfirmDialog(null, "");
    }

    @Override
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

//    @Override
//    public void detecteCharactersContour(JLabel jLabel) {
//        Mat source_image = Imgcodecs.imread(ocrReadFrom);
//        Mat gray_image = new Mat();
//        Mat blur_image = new Mat();
//        Mat threshold = new Mat();
//        Mat result = new Mat();
//
//        cvtColor(source_image, gray_image, opencv_imgproc.CV_BGR2GRAY);
//        medianBlur(gray_image, blur_image, 3);
//        adaptiveThreshold(blur_image, threshold, 255, 1, 1, 11, 2);
//        Mat element = getStructuringElement(MORPH_ELLIPSE, new Size(3, 3), new Point(1, 1));
//        morphologyEx(threshold, result, MORPH_CLOSE, element);
//
//        //-----------------------------------------------------------------------
//        ArrayList< MatOfPoint> contours = new ArrayList<>();
//        Mat hierarchy = new Mat();
//        findContours(result, contours, hierarchy, opencv_imgproc.CV_RETR_CCOMP,
//                opencv_imgproc.CV_CHAIN_APPROX_SIMPLE);
//        int i = 0;
//        for (MatOfPoint contour : contours) {
//            System.out.println("processing"+i);
//            Rect r = Imgproc.boundingRect(contour);
//            double area0 = contourArea(contour);
//            if (area0 < 120) {
//                drawContours(threshold, contours, i, new Scalar(0, 255, 0), 1, opencv_imgproc.CV_FILLED, hierarchy, 8, new Point(1, 1));
//                continue;
//            }
//            i++;
//        }
//
//        //------------------------------------------------------------------------
//        Imgcodecs.imwrite("contours22.jpg", threshold);
//        Imgcodecs.imwrite("contours2.jpg", result);
//                
//
//    }

    @Override
    public void detecteCharactersContour(JLabel jLabel) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
