/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfe;

import fenetres.AcceuilBinome;
import fenetres.DesignImpl;
import fenetres.Principal;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Taleb
 */
public class LanceurApplication {

    public static void main(String[] args) {

        // ici pour specifier que l application prendra le designe du systeme d exploitation
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LanceurApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(LanceurApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(LanceurApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(LanceurApplication.class.getName()).log(Level.SEVERE, null, ex);
        }

        // c ets la vrai facon d invoquer une fenetre en java on se sert du thread dispatcher
        // pour lancer des taches graphique
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AcceuilBinome().setVisible(true);
                //new Principal(new DesignImpl()).setVisible(true); // lancer la fenetre principal apré decoration
            }
        });

    }
}
