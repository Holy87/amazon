/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Francesco
 */
public class MainWindow extends javax.swing.JFrame {
    private final int ADDN = 2;
    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        initCustomComponents();
        
    }
    
    private FinestraUtente finestraUtente;
    private int utenteID;
    private String nomeUtente;
    
    private FinestraAutore finestraAutore;
    private FinestraEditore finestraEditore;
    private FinestraLibro finestraLibro;
    private FinestraVenditore finestraVenditore;
    
    
    
    private void initCustomComponents() {
        jTabbedPane1.setVisible(false);
        finestraUtente = new FinestraUtente(this, false);
        tabUtenti.impostaInterfaccia("UTENTI", finestraUtente, this);
        finestraAutore = new FinestraAutore(this, false);
        tabAutori.impostaInterfaccia("AUTORI", finestraAutore, this);
        finestraEditore = new FinestraEditore(this, false);
        tabEditori.impostaInterfaccia("EDITORI", finestraEditore, this);
        finestraLibro = new FinestraLibro(this, false);
        tabLibri.impostaInterfaccia("LIBRI", finestraLibro, this);
        finestraVenditore = new FinestraVenditore(this, false);
        tabVenditori.impostaInterfaccia("VENDITORI", finestraVenditore, this);
    }
    
    private void connettiTutto()
    {
        tabUtenti.connectTable();
        tabAutori.connectTable();
        tabEditori.connectTable();
        tabLibri.connectTable();
        tabVenditori.connectTable();
    }
    
    public void impostaUtente(int id, String nome) {
        utenteID = id;
        nomeUtente = nome;
        lUtente.setText(nome);
        mAcquisto.setEnabled(true);
        JOptionPane.showMessageDialog(this, nome + " è correttamente impostato come utente attivo.");
    }
    
    public void gestisciUtente(){
        /*Metodo che gestisce l'Utente e visualizza:
        * 1) Visualizza liste desideri
        * 2) Visualizza carrello
        * 3) Visualizza ordini già effettuati
        * 4) Crea ordine
        * 5) Crea recensione
        */
    }
    
    public void visualizzaInfoLibro(){
        /*Metodo che prende un libro e ne visualizza
        **le info complete, tra cui:
        **1) Le immagini di copertina
        **2) Gli autori del libro
        **3) Gli editori del libro
        **4) Le lingue
        **5) Il formato ed il prezzo di listino applicato a quel formato
        **6) FACOLTATIVO: elencare tutte le recensioni di quel libro
        */
    }
            
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        tabUtenti = new amazon.TabOggetti();
        jLabel1 = new javax.swing.JLabel();
        lUtente = new javax.swing.JLabel();
        tabAutori = new amazon.TabOggetti();
        tabEditori = new amazon.TabOggetti();
        tabLibri = new amazon.TabOggetti();
        tabVenditori = new amazon.TabOggetti();
        tabOggetti2 = new amazon.TabOggetti();
        tabOggetti3 = new amazon.TabOggetti();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        mAcquisto = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        Carrello = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gruppo 26 - Amazon");
        setLocationByPlatform(true);

        jLabel1.setText("Utente attivo:");

        lUtente.setText("Nessuno");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabUtenti, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lUtente, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(tabUtenti, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lUtente)))
        );

        jTabbedPane1.addTab("Utenti", jPanel1);
        jTabbedPane1.addTab("Autori", tabAutori);
        jTabbedPane1.addTab("Editori", tabEditori);
        jTabbedPane1.addTab("Libri", tabLibri);
        jTabbedPane1.addTab("Venditori", tabVenditori);

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Connetti");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Disconnetti");
        jMenuItem3.setEnabled(false);
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Esci");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Modifica");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Azioni utente");

        mAcquisto.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mAcquisto.setText("Acquisto libri");
        mAcquisto.setEnabled(false);
        mAcquisto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mAcquistoActionPerformed(evt);
            }
        });
        jMenu3.add(mAcquisto);

        jMenuItem4.setText("Lista desideri");
        jMenuItem4.setEnabled(false);
        jMenu3.add(jMenuItem4);

        Carrello.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        Carrello.setText("Vai al carrello");
        Carrello.setEnabled(false);
        jMenu3.add(Carrello);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("utenti");

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        setVisible(false);
        dispose();
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        FinestraLogin lWindow = new FinestraLogin(this, false);
        lWindow.setVisible(true);
        checkState();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    
    /**
    * AZIONE DI DISCONNESSIONE
    * @param evt 
     */
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if (DBConnection.connected()) {
            try{
                DBConnection.CloseConnection();
                checkState(false);
            }
            catch (SQLException e) {
                JOptionPane.showMessageDialog(rootPane, "Errore nella disconnessione al database: " + e.toString(), null, ERROR_MESSAGE);
            }
        }
        else {
            checkState(false);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void mAcquistoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mAcquistoActionPerformed
        AnagraficaLibri anaLibri = new AnagraficaLibri(this, false);
        anaLibri.setVisible(true);
    }//GEN-LAST:event_mAcquistoActionPerformed

    /**
     * 
     * @param state true: attiva la connessione, disattiva l disconnessione e vicev
     */
    private void checkState(boolean state){
        jMenuItem1.setEnabled(!state);
        jMenuItem3.setEnabled(state);
        jTabbedPane1.setVisible(state);
        if (state == true)
            connettiTutto();
    }
    
    private void checkState(){checkState(DBConnection.connected());}
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        //</editor-fold>
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        try {
            // Set System L&F
        UIManager.setLookAndFeel(
                //"javax.swing.plaf.nimbus.MetalLookAndFeel");
            UIManager.getSystemLookAndFeelClassName());
    } 
    catch (UnsupportedLookAndFeelException e) {
       // handle exception
    }
    catch (ClassNotFoundException e) {
       // handle exception
    }
    catch (InstantiationException e) {
       // handle exception
    }
    catch (IllegalAccessException e) {
       // handle exception
    }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Carrello;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lUtente;
    private javax.swing.JMenuItem mAcquisto;
    private amazon.TabOggetti tabAutori;
    private amazon.TabOggetti tabEditori;
    private amazon.TabOggetti tabLibri;
    private amazon.TabOggetti tabOggetti2;
    private amazon.TabOggetti tabOggetti3;
    private amazon.TabOggetti tabUtenti;
    private amazon.TabOggetti tabVenditori;
    // End of variables declaration//GEN-END:variables
}
