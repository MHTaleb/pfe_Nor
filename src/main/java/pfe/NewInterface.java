/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfe;

import javax.swing.JLabel;
import org.bytedeco.javacpp.opencv_core;

/**
 *
 * @author Taleb
 */
public interface NewInterface {

    void findContours(JLabel jLabel);

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
    void findLines(JLabel jLabel);

    opencv_core.IplImage getBlackAndWhiteImage();

    opencv_core.IplImage getOriginalImage();

    opencv_core.IplImage getSmouthedImage();

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
    void loadPlate(JLabel jLabelSelectedImage);

    void smoothAndBlur(JLabel jLabelSmouthedImage);

    void toB_W(JLabel jLabelBlackAndWhiteImage);

    void toB_W_OCR(JLabel jLabelBlackAndWhiteImage);
    
}
