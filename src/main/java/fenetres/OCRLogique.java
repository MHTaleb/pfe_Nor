/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenetres;


import pfe.imagePath;
import pfe.Logique;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 *
 * @author Taleb
 */
class OCRLogique extends imagePath {

    Tesseract instance;

    public OCRLogique() {
        instance = Tesseract.getInstance();
       
    }

    void doOCR(JLabel jLabelResultatFinal) {
        try {

        

            instance.setDatapath("C:\\Tess4J-3.3.0-src\\Tess4J\\tessdata");
            instance.setTessVariable("tessedit_char_whitelist", "0123456789");
            instance.setTessVariable("load_system_dwag", "1");
            instance.setTessVariable("load_freq_dwag", "1");
            ArrayList<String> configs = new ArrayList<>();
            configs.add("digits");
            instance.setConfigs(configs);
            ArrayList<Rectangle> rectangles = Logique.getRectangles();
            String doOCR = "";
            String found_Numbers = "";
            
            doOCR = instance.doOCR(new File(ocrReadFrom));
                        System.out.println("doOcr : " + doOCR);
            
            if (rectangles != null) {
                if (rectangles.size() > 0) {
                    for (Rectangle rectangle : rectangles) {

                        for (String image : new String[]{ocrReadFrom,original}) {
                         
                            
//                            rectangle.height=rectangle.height-10;
//                            rectangle.width=rectangle.width-10;
//                            rectangle.x=rectangle.x+10;
//                            rectangle.y=rectangle.y+10;
                        doOCR = instance.doOCR(new File(image), rectangle);
                        System.out.println("doOcr : " + doOCR);
                        doOCR = doOCR.replaceAll(" ", "");
                     
                        
                        if (!doOCR.trim().isEmpty()) {
                            if (doOCR.trim().length() > 7)
                            if (doOCR.trim().length() <= 13)
                            {
                                found_Numbers += " " + doOCR + " AND ";
                            }
                        }
                        }
                    }

                }
            }
            
            if (!found_Numbers.isEmpty()) {
                found_Numbers = found_Numbers.substring(0, found_Numbers.length() - 4);
                jLabelResultatFinal.setText("Matricule : " + found_Numbers);
            }

        } catch (TesseractException ex) {
            Logger.getLogger(OCRLogique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
