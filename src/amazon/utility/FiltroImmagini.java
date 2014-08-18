/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.utility;

import java.io.File;

/**
 * 
 * @author Francesco Bosso <fr.bosso@outlook.it>
 */
public class FiltroImmagini extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File file) {
        return file.isDirectory() || file.getAbsolutePath().toLowerCase().endsWith(".jpeg")
                || file.getAbsolutePath().toLowerCase().endsWith(".jpg");
    }

    @Override
    public String getDescription() {
        return "Immagini JPEG";
    }

}
