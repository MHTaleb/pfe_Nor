/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfe;

/**
 *
 * @author Taleb
 */
public class imagePath {
    // ceci est le nom de chemin de sortie de l image qui est transformer en ligne

    protected String blackAndWhite = "blackAndWhite.jpg";
    protected String erosion = "erosion.jpg";
    protected String dilation = "dilation.jpg";
    protected String imagelines = "imageLines.jpg";
    protected String smoothed = "smoothed.jpg";
    protected String sobelImage="SobelImage.jpg";
    protected String original="original.jpg";
    protected String ocrReadFrom="OcrReadFrom.jpg";
    
    
    
    protected String blackAndWhiteContour = "blackAndWhiteContour.jpg";
    protected String erosionContour = "erosionContour.jpg";
    protected String dilationContour = "dilationContour.jpg";
    protected String imagelinesContour = "imageLinesContour.jpg";
    protected String smoothedContour = "smoothedContour.jpg";
    protected String sobelImageContour="SobelImageContour.jpg";

    public String getBlackAndWhite() {
        return blackAndWhite;
    }

    public String getDilation() {
        return dilation;
    }

    public String getErosion() {
        return erosion;
    }

    public String getImagelines() {
        return imagelines;
    }

    public String getSmoothed() {
        return smoothed;
    }

    public String getBlackAndWhiteContour() {
        return blackAndWhiteContour;
    }

    public String getDilationContour() {
        return dilationContour;
    }

    public String getErosionContour() {
        return erosionContour;
    }

    public String getImagelinesContour() {
        return imagelinesContour;
    }

    public String getOcrReadFrom() {
        return ocrReadFrom;
    }

    public String getOriginal() {
        return original;
    }

    public String getSmoothedContour() {
        return smoothedContour;
    }

    public String getSobelImage() {
        return sobelImage;
    }

    public String getSobelImageContour() {
        return sobelImageContour;
    }

    
}
