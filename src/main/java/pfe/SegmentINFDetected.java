/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfe;

import javax.swing.JLabel;

/**
 *
 * @author Taleb
 */
public interface SegmentINFDetected {

    void applySobelFactor(JLabel jLabelSobelFactor);

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
    void detecteCharactersContour(JLabel jLabel);

    /**
     * la dilatation et l erosion nous permette de reparer les forme
     * triangulaire afin de facilité leur detection ainsi elle permet de bien
     * remplir les texts pour qu il sois facilement detectable par les Ocrs
     */
    void dilate_erose(JLabel jLabelErosion, JLabel jLabelDilated);

    void dilate_erose_ocr();

    void toB_A_W(JLabel jLabel);
    
}
