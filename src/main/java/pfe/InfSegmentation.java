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
public interface InfSegmentation {

    void applySobelFactor(JLabel jLabelSobelFactor);

    /**
     * la dilatation et l erosion nous permette de reparer les forme
     * triangulaire afin de facilité leur detection ainsi elle permet de bien
     * remplir les texts pour qu il sois facilement detectable par les Ocrs
     * @param jLabelErosion
     * @param jLabelDilated
     */
    void dilate_erose(JLabel jLabelErosion, JLabel jLabelDilated);
void dilate_erose_ocr();
    void toB_A_W(JLabel jLabel);
    
    void detecteCharactersContour(JLabel jLabel);
    
}
