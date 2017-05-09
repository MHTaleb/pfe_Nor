/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfe;

import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvContour;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import org.bytedeco.javacpp.opencv_core.IplConvKernel;
import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_core.cvScalar;
import org.bytedeco.javacpp.opencv_imgcodecs;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import org.bytedeco.javacpp.opencv_imgproc;
import static org.bytedeco.javacpp.opencv_imgproc.CV_ADAPTIVE_THRESH_GAUSSIAN_C;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_CCOMP;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static org.bytedeco.javacpp.opencv_imgproc.MORPH_RECT;
import static org.bytedeco.javacpp.opencv_imgproc.cvAdaptiveThreshold;
import static org.bytedeco.javacpp.opencv_imgproc.cvBoundingRect;
import static org.bytedeco.javacpp.opencv_imgproc.cvCreateStructuringElementEx;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvFindContours;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;
import org.opencv.core.Core;
import org.opencv.core.CvType;

import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import static org.opencv.imgproc.Imgproc.blur;



/**
 *
 * @author Taleb
 */
public class Logique extends imagePath implements InfLogique {

   
    // l'image original dans une variable de type IntelProcessingImage
    private static opencv_core.IplImage originalImage;
    private static opencv_core.IplImage smouthedImage;
    private static opencv_core.IplImage blackAndWhiteImage;
    private static opencv_core.IplImage lineImage;

    
    private static ArrayList<Rectangle> rectangles = new ArrayList<>();

    public static ArrayList<Rectangle> getRectangles() {
        return rectangles;
    }
    private IplImage blackAndWhiteImageOCR;
    
    public Logique() {
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public opencv_core.IplImage getBlackAndWhiteImage() {
        return blackAndWhiteImage;
    }

    public opencv_core.IplImage getOriginalImage() {
        return originalImage;
    }

    public opencv_core.IplImage getSmouthedImage() {
        return smouthedImage;
    }
    
    
    

    public void quitter() {
        System.exit(0);
    }

    // pour vider le cache de l ancienne image
    public void reset() {
        try {
            opencv_core.cvReleaseImage(originalImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * permet de selectionner une image depuis votre ordinateur puis la charger grace a la
     * methode cvLoadImade qui est une methode dans la bibliotheque opencv qui permet
     * de lire une image et la mettre dans une patrice de type iplImage
     * 
     * JFileChooser est une class java dans le package swing qui gere la selection de fichier grace
     * a sa methode showOpenDialog celle si elle va renvoyer a la fin de votre selection le fichier 
     * selectionner grace a getSelectedFile, ce qui nous permet de recuperer le chemin vers le fichier
     * avec la methode getAbsolutePath
     * 
     * a la fin on donne ce chemin a cvLoadImage qui a besoin du chemin de l image selectionner
     * afin de la cherger
     * 
     * une fois tout cela reussis on va afficher l image selectionner dans le label etant une icone
     * pour cela on se sert de ImageIcon qui est dedier a ce propos
     */
    @Override
    public void loadPlate(JLabel jLabelSelectedImage) {
        JFileChooser chooser = new JFileChooser();// c est l object qui nous permet de rechercher l image
        chooser.showOpenDialog(null);// on demande a l objet de nous donner l interface de recherche d un fichier pour l ouvrir
        //cvLoadImage sert a lire l image selectionner et la mettre dans la variable originalImage
        this.originalImage = opencv_imgcodecs.cvLoadImage(chooser.getSelectedFile().getAbsolutePath());
        //cvSaveImage sert a sauvegarder l image selection au sein du projet
        cvSaveImage(original, originalImage);
        //j affiche l image dans le jlabel dans la fenetre principal
        jLabelSelectedImage.setIcon(new ImageIcon(chooser.getSelectedFile().getAbsolutePath()));
        // jlabel se sert de la class image icon pour afficher des image donc on va instancier un nouveau objet de 
        // type image icon on lui indiquant ou se trouve notre image selectionner , celui la va la charger et l afficher
        // dans notre label
    }

    
    // cette methode est tjr experimental afin de pouvoir faire plusieur simulation
    public void reset(boolean enabled) {
        if (enabled) {
            this.reset();
        }
    }

    
    /**
     * 
     * find lines est une methode introduite dans les tutoriaux de opencv qui permet de
     * transformer une image en ligne ( ou bien disant dessiner l image en ligne )
     * 
     * cela nous aidera enormement dans la detection de la plaque qui devrai avoir 
     * une forme rectangulaire ou proche de la rectangulaire
     * 
     * la documentation sur ce code est dans le liens suivant :
     * http://docs.opencv.org/2.4/doc/tutorials/imgproc/imgtrans/canny_detector/canny_detector.html
     * 
     * 
     * cvCanny a besoin l image original , la destination , la ratio d entré , puis au min 3*la ration d entré , taille de la matrice qui est 3
     * vu par la documentation
     */
    @Override
    public void findLines(JLabel jLabel) {
       
        // l image qui va etre en resultat final en  noire et blan sous forme de ligne
        lineImage = opencv_core.cvCreateImage(opencv_core.cvGetSize(blackAndWhiteImage), blackAndWhiteImage.depth(), 1);
        //colorDst = cvCreateImage(cvGetSize(blackAndWhiteImage), blackAndWhiteImage.depth(), 3);
        opencv_imgproc.cvCanny(blackAndWhiteImage, lineImage, 100, 300, 3); // detection de ligne
        //opencv_core.CvSeq lines = new opencv_core.CvSeq();
        //opencv_core.CvMemStorage storage = cvCreateMemStorage(100000);
        opencv_imgcodecs.cvSaveImage(imagelines, lineImage); // enregistrement du resultat
        jLabel.setIcon(new ImageIcon(imagelines)); // affichage dans le label

    }

    @Override
    public void smoothAndBlur(JLabel jLabelSmouthedImage) {
        
        // on applique un smooth(lissage) qui va rendre l image un pti peu flou mais bcp plus facile a etudier
        opencv_imgproc.cvSmooth(originalImage, originalImage);
        // enregistrement de l image  smouthéé
        opencv_imgcodecs.cvSaveImage(smoothed, originalImage);
        
        // pour le blur (rendre les couleur plus apparante) on se sert d un sous projet de opencv ou il donne des outils matriciel pour des fonction mathematique
        // qui vont etre appliquer au blur
        org.opencv.core.Mat source = Imgcodecs.imread(smoothed); // on recupere l image lissé
        blur(source, source, new Size(3, 3));// on applique le blur pour avoir de meilleur qualité de couleur
        
        // cela nous permetera d avoir un meilleur resultat a la transformation en binaire ( noir et blan seulement )
        
        Imgcodecs.imwrite(smoothed, source); // sauvegarder le resultat
        jLabelSmouthedImage.setIcon(new ImageIcon(smoothed)); // montrer le resultat dans le label
    }

    @Override
    public void toB_W_OCR(JLabel jLabelBlackAndWhiteImage){
             smouthedImage = opencv_imgcodecs.cvLoadImage(ocrReadFrom);
         blackAndWhiteImageOCR = opencv_core.IplImage.create(smouthedImage.width(),
                smouthedImage.height(), IPL_DEPTH_8U, 1);
         // la fonction qui va executé la transformation en noire et blan
        System.out.println("0");
        //cvAdaptiveThreshold(smouthedImage, smouthedImage, 255, CV_ADAPTIVE_THRESH_GAUSSIAN_C, opencv_imgproc.CV_THRESH_MASK, 15, -2);
        opencv_imgproc.cvSmooth(smouthedImage, smouthedImage);
        System.out.println("1");
        cvCvtColor(smouthedImage, blackAndWhiteImageOCR, CV_BGR2GRAY);
        System.out.println("2");
        cvAdaptiveThreshold(blackAndWhiteImageOCR, blackAndWhiteImageOCR, 255, CV_THRESH_BINARY_INV, CV_THRESH_BINARY, 41, 9);
        System.out.println("3");
         opencv_imgproc.cvSmooth(blackAndWhiteImageOCR, blackAndWhiteImageOCR);
        // fin de la transformation 
        cvSaveImage("final_"+ocrReadFrom, blackAndWhiteImageOCR);
        jLabelBlackAndWhiteImage.setIcon(new ImageIcon("final_"+ocrReadFrom));
    }
    
    @Override
    public void toB_W(JLabel jLabelBlackAndWhiteImage) {
        // cella est l image de l ocr
   
        //JOptionPane.showConfirmDialog(null, "");
        //-------------------------------------------------------------------------
        //cella est l image des contours
        // on recherge l image lissé et amelioré dans  smouthedImage
        smouthedImage = opencv_imgcodecs.cvLoadImage(smoothed);
        // on prepare une image en noire et blan qui est a la taille de l image lissé ...
        // IPL_DEPTH_8U va coder l image en 8 bit cela montrera une grande nuance entre le plage de couleur qui nous permettré de bien passer au
        // noire et blan avec un seul chanel pour garatir le passage
         blackAndWhiteImage = opencv_core.IplImage.create(smouthedImage.width(),
                smouthedImage.height(), IPL_DEPTH_8U, 1);
         // la fonction qui va executé la transformation en noire et blan
        cvCvtColor(smouthedImage, blackAndWhiteImage, CV_BGR2GRAY);
        cvSaveImage("grey_"+blackAndWhite, blackAndWhiteImage);
        jLabelBlackAndWhiteImage.setIcon(new ImageIcon("grey_"+blackAndWhite));
        cvAdaptiveThreshold(blackAndWhiteImage, blackAndWhiteImage, 255, CV_ADAPTIVE_THRESH_GAUSSIAN_C, CV_THRESH_BINARY_INV, 5, 5);
        // fin de la transformation 
        cvSaveImage(blackAndWhite, blackAndWhiteImage);
    }

    @Override
    public void findContours(JLabel jLabel){
         IplImage resultImage = opencv_imgcodecs.cvLoadImage(dilation);
         IplImage srcImage = opencv_imgcodecs.cvLoadImage(dilation,CvType.CV_8UC1);
         IplConvKernel kernel = cvCreateStructuringElementEx(1, 1, 0, 0, MORPH_RECT);
         opencv_imgproc.cvDilate(srcImage, srcImage,kernel , 5);
         opencv_imgproc.cvCanny(srcImage, srcImage, 100, 300);

         cvSaveImage("test.jpg", srcImage);
         
    CvMemStorage mem = CvMemStorage.create();
    CvSeq contours = new CvSeq();
    CvSeq ptr = new CvSeq();

    cvFindContours(srcImage, mem, contours, Loader.sizeof(CvContour.class) , CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE, cvPoint(0,0));

    CvRect boundbox;
    
    int logicalWidth=0;
    
    for (ptr = contours; ptr != null; ptr = ptr.h_next()) {
        boundbox = cvBoundingRect(ptr, 0);
        if (logicalWidth<boundbox.width()) {
            logicalWidth=boundbox.width();
        }
    }

    for (ptr = contours; ptr != null; ptr = ptr.h_next()) {
        boundbox = cvBoundingRect(ptr, 0);

//        if (boundbox.width()>logicalWidth/7) // il n est pas tres petit
        if (boundbox.height()>45) // si grand pour qu il sois une plaque
      if (boundbox.width()<boundbox.height()*10) // la hauteur est si petit par rapport a la largeur
      if (boundbox.width()>boundbox.height()*4) // la largeur est au moins 3 fois la hauteur
        {
            
            rectangles.add(new Rectangle(boundbox.x()+15, boundbox.y()+8, boundbox.width()-15, boundbox.height()-10));
            cvRectangle( resultImage , cvPoint( boundbox.x()+15, boundbox.y()+6 ), 
                cvPoint( boundbox.x()-15 + boundbox.width(), boundbox.y()-10 + boundbox.height()),
                cvScalar( 0, 0, 255, 0 ), 1, 0, 0 );
        }
                
    }
        cvSaveImage(imagelinesContour, resultImage);
        jLabel.setIcon(new ImageIcon(imagelinesContour));
    }
}
