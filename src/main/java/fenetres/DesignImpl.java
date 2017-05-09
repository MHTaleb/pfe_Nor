/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenetres;

import java.awt.Frame;
import javax.swing.JFrame;


public class DesignImpl implements Design{

    @Override
    public void doDesign(JFrame frame) {
        frame.setLocationRelativeTo(null); // pour centrer l application
      //  frame.setExtendedState(Frame.MAXIMIZED_BOTH);//pour maximiser l application
     //   frame.setUndecorated(true);// pour enlever la bare des taches
        //frame.setVisible(true);
        
    }

    
}
