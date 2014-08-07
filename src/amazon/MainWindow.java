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
     * Avvio della finestra senza parametri in ingresso
     */
    public MainWindow() {
        initComponents();
        initCustomComponents();
        
    }
    
    /**
     * Avvio dell'applicazione con parametri di collegamento in ingresso
     * per la connessione automatica
     * @param username nome utente
     * @param password password della connessione
     */
    public MainWindow(String username, String password) {
        initComponents();
        initCustomComponents();
        connessioneAutomatica(username, password);
    }
    
    private FinestraUtente finestraUtente;
    public int utenteID;
    private String nomeUtente;
    
    private FinestraAutore finestraAutore;
    private FinestraEditore finestraEditore;
    private FinestraLibro finestraLibro;
    private FinestraVenditore finestraVenditore;
    
    /**
     * Inizializzazione dei componenti:
     * finestraUtente: scheda dell'elenco degli utenti
     * finestraAutore: scheda dell'elenco degli autori
     * finestraEditore: scheda dell'elenco degli editori
     * finestraLibri: scheda dell'elenco dei libri
     * finestraVenditore: scheda dell'elenco dei venditori
     */
    private void initCustomComponents() {
        jTabbedPane1.setVisible(false);
        finestraUtente = new FinestraUtente(this, true);
        tabUtenti.impostaInterfaccia("UTENTI", finestraUtente, this);
        finestraAutore = new FinestraAutore(this, true);
        tabAutori.impostaInterfaccia("AUTORI", finestraAutore, this);
        finestraEditore = new FinestraEditore(this, true);
        tabEditori.impostaInterfaccia("EDITORI", finestraEditore, this);
        finestraLibro = new FinestraLibro(this, true);
        tabLibri.impostaInterfaccia("LIBRI", finestraLibro, this);
        finestraVenditore = new FinestraVenditore(this, true);
        tabVenditori.impostaInterfaccia("VENDITORI", finestraVenditore, this);
    }
    
    /**
     * Connessione delle tabelle
     */
    private void connettiTutto()
    {
        tabUtenti.connectTable();
        tabAutori.connectTable();
        tabEditori.connectTable();
        tabLibri.connectTable();
        tabVenditori.connectTable();
    }
    
    /**
     * Il metodo seleziona l'utente attivo per le operazioni di acquisto,
     * visualizzazione del carrello e voto ai prodotti
     * @param id id dell'utente
     * @param nome nome per la visualizzazione
     */
    public void impostaUtente(int id, String nome) {
        if (id == 0) {
            utenteID = 0;
            nomeUtente = null;
            lUtente.setText("");
            mAcquisto.setEnabled(false);
            mCarrello.setEnabled(false);
            mDesideri.setEnabled(false);
            mOrdini.setEnabled(false);
        } else {
            utenteID = id;
            nomeUtente = nome;
            lUtente.setText(nome);
            mAcquisto.setEnabled(true);
            mCarrello.setEnabled(true);
            mDesideri.setEnabled(true);
            mOrdini.setEnabled(true);
            tabLibri.attivaServiceButton2();
            JOptionPane.showMessageDialog(this, nome + " è correttamente impostato come utente attivo.");
        }
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
    
    /**
     * Il metodo viene eseguito automaticamente quando vengono dati parametri
     * all'avvio dell'applicazione
     * @param user stringa dell'username
     * @param pass stringa della password
     */
    private void connessioneAutomatica(String user, String pass) {
        DBConnection.tempUser = user;
        DBConnection.tempPass = pass;
        DBConnection.tempHost = "188.9.189.147";//"143.225.117.238";
        DBConnection.tempPort = "1521";
        try {
            DBConnection.StartConnection();
            checkState();
        }
        catch (SQLException exc){
        }
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
        mNuovo = new javax.swing.JMenu();
        mUtente = new javax.swing.JMenuItem();
        mLibro = new javax.swing.JMenuItem();
        mEditore = new javax.swing.JMenuItem();
        mVenditore = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mAcquisto = new javax.swing.JMenuItem();
        mDesideri = new javax.swing.JMenuItem();
        mOrdini = new javax.swing.JMenuItem();
        mCarrello = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gruppo 26 - Amazon");
        setLocationByPlatform(true);

        jLabel1.setText("Utente attivo:");

        lUtente.setText("Nessuno");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabUtenti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        mNuovo.setText("Nuovo");
        mNuovo.setEnabled(false);

        mUtente.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mUtente.setText("Utente");
        mUtente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mUtenteActionPerformed(evt);
            }
        });
        mNuovo.add(mUtente);

        mLibro.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mLibro.setText("Libro");
        mLibro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mLibroActionPerformed(evt);
            }
        });
        mNuovo.add(mLibro);

        mEditore.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mEditore.setText("Editore");
        mEditore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mEditoreActionPerformed(evt);
            }
        });
        mNuovo.add(mEditore);

        mVenditore.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mVenditore.setText("Venditore");
        mVenditore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mVenditoreActionPerformed(evt);
            }
        });
        mNuovo.add(mVenditore);

        jMenu1.add(mNuovo);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem2.setText("Esci");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Azioni utente");

        mAcquisto.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mAcquisto.setText("Acquisto Libri");
        mAcquisto.setEnabled(false);
        mAcquisto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mAcquistoActionPerformed(evt);
            }
        });
        jMenu3.add(mAcquisto);

        mDesideri.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mDesideri.setText("Lista Desideri");
        mDesideri.setEnabled(false);
        mDesideri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mDesideriActionPerformed(evt);
            }
        });
        jMenu3.add(mDesideri);

        mOrdini.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mOrdini.setText("Storico Ordini");
        mOrdini.setEnabled(false);
        mOrdini.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mOrdiniActionPerformed(evt);
            }
        });
        jMenu3.add(mOrdini);

        mCarrello.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mCarrello.setText("Vai al Carrello");
        mCarrello.setEnabled(false);
        mCarrello.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCarrelloActionPerformed(evt);
            }
        });
        jMenu3.add(mCarrello);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
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
        FinestraLogin lWindow = new FinestraLogin(this, true);
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
                impostaUtente(0, null);
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
        AnagraficaLibri anaLibri = new AnagraficaLibri(this, true, utenteID);
        anaLibri.setVisible(true);
    }//GEN-LAST:event_mAcquistoActionPerformed

    private void mCarrelloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCarrelloActionPerformed
        FinestraOrdine finestra = new FinestraOrdine(this, true, utenteID);
        finestra.setVisible(true);
    }//GEN-LAST:event_mCarrelloActionPerformed

    private void mUtenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mUtenteActionPerformed
        finestraUtente.show(ADDN, null, tabUtenti);
    }//GEN-LAST:event_mUtenteActionPerformed

    private void mLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mLibroActionPerformed
        finestraLibro.show(ADDN, null, tabLibri);
    }//GEN-LAST:event_mLibroActionPerformed

    private void mEditoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mEditoreActionPerformed
        finestraEditore.show(ADDN, null, tabEditori);
    }//GEN-LAST:event_mEditoreActionPerformed

    private void mVenditoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mVenditoreActionPerformed
        finestraVenditore.show(ADDN, null, tabVenditori);
    }//GEN-LAST:event_mVenditoreActionPerformed

    private void mOrdiniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mOrdiniActionPerformed
        FinestraStoricoOrdini finestraSO = new FinestraStoricoOrdini(this, true, utenteID);
        finestraSO.setVisible(true);
    }//GEN-LAST:event_mOrdiniActionPerformed

    private void mDesideriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mDesideriActionPerformed
        FinestraListaDesideri finestraLD = new FinestraListaDesideri(this, true, utenteID);
        finestraLD.setVisible(true);
    }//GEN-LAST:event_mDesideriActionPerformed

    /**
     * 
     * @param state true: attiva la connessione, disattiva l disconnessione e vicev
     */
    private void checkState(boolean state){
        jMenuItem1.setEnabled(!state);
        jMenuItem3.setEnabled(state);
        jTabbedPane1.setVisible(state);
        mNuovo.setEnabled(state);
        if (state == true)
            connettiTutto();
    }
    
    private void checkState(){checkState(DBConnection.connected());}
    
    /**
     * @param args nome utente e password
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
                if (args.length == 2)
                    new MainWindow(args[0], args[1]).setVisible(true);
                else
                    new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lUtente;
    private javax.swing.JMenuItem mAcquisto;
    private javax.swing.JMenuItem mCarrello;
    private javax.swing.JMenuItem mDesideri;
    private javax.swing.JMenuItem mEditore;
    private javax.swing.JMenuItem mLibro;
    private javax.swing.JMenu mNuovo;
    private javax.swing.JMenuItem mOrdini;
    private javax.swing.JMenuItem mUtente;
    private javax.swing.JMenuItem mVenditore;
    private amazon.TabOggetti tabAutori;
    private amazon.TabOggetti tabEditori;
    private amazon.TabOggetti tabLibri;
    private amazon.TabOggetti tabOggetti2;
    private amazon.TabOggetti tabOggetti3;
    private amazon.TabOggetti tabUtenti;
    private amazon.TabOggetti tabVenditori;
    // End of variables declaration//GEN-END:variables
}
