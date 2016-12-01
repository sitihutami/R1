/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package r1;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author ai
 */
public class R1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(new com.seaglasslookandfeel.SeaGlassLookAndFeel());
            init();
            new ui.Dash().setVisible(true);
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(R1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void init() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        if(!util.Work.f.exists())util.Work.init();
    }
    
}
