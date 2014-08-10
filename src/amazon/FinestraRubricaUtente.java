/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import amazon.modelliTabelle.DBTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author Francesco
 */
public class FinestraRubricaUtente extends javax.swing.JDialog {

    /**
     * Creates new form EsempioTabella
     * @param parent va inserita la finestra chiamante (un jFrame)
     * @param modal va sempre in false
     */
    public FinestraRubricaUtente(java.awt.Frame parent, boolean modal, int idUtente) {
        super(parent, modal);
        this.idUtente = idUtente;
        initComponents();
        impostaTabella();   // aggiungere al costruttore questo metodo in modo
                            // da impostare il set di dati
    }
    
    private final int idUtente;
    private int contactID;
    private ResultSet rs; //ResultSet su cui si basano i dati della tabella
    private DBTableModel modelloTabella; //modello della tabella per i dati
    private int cursore = 1; //memorizza la riga selezionata
    protected final int EDIT = 1;   //modalità di modifica
    protected final int ADDN = 2;   //modalità di aggiunta
    
    /**
     * Inizializza i dati della tabella, assegnandogli il modello e il rs.
     */
    @SuppressWarnings("Convert2Lambda")
    public final void impostaTabella() {
        modelloTabella = new DBTableModel(rs);//inserire il resultset nel costr.
        tabella.setModel(modelloTabella); //metto il modellotabella nel
        tabella.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //non possono essere selezionati record multipli
        tabella.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            //implemento un evento che chiama tableSelectionChanged quando cambia la selezione della tabella
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        } 
                
        );
        //infine aggiorno il resultset della tabella
        aggiornaTabella();
    }
    
    /**
     * Aggiorna i dati della tabella con tutti i dati del database.
     */
    public void aggiornaTabella()
    {
        try {
            rs = ottieniDati(); //chiama il metodo in basso
                                //non è proprio necessario chiamare un metodo
                                //si può anche direttamente passare il reslts.
            modelloTabella.setRS(rs);   //non credo serva, ma il prof lo mette..
            rs.absolute(cursore);   //attiva la riga del cursore attuale
            mostraDati();           //imposta la selezione a riga singola
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Questo metodo restituisce il resultset necessario alla tabella
     * Modificatelo a seconda dei dati che volete visualizzare sulla tabella
     * @return resultset dei dati da mettere in tabella 
     */
    private ResultSet ottieniDati() throws SQLException {
        return DBConnection.visualizzaRubricaUtente(idUtente);
    }
    
    /**
     * Metodo che viene chiamato quando si clica il mouse sulla tabella
     * cambiando la selezione
     */
    private void tableSelectionChanged() 
    {
        try {
            rs.absolute(tabella.getSelectionModel().getMinSelectionIndex() + 1);
            mostraDati();
            System.out.println("ok");
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Mostra infine i dati sulla tabella dopo un aggiornamento
     */
    private void mostraDati() {
      try {
          cursore = rs.getRow();
          tabella.getSelectionModel().setSelectionInterval(cursore - 1,cursore - 1);
          tabella.setRowSelectionInterval(cursore - 1, cursore - 1);
          contactID = rs.getInt(1);
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println(ex.getMessage());
      }
    }
    
        /**
     * Rimozione di un autore al libro
     */
    private void rimuoviIndirizzo() {
        try {
            DBConnection.rimuoviIndirizzo(contactID);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        aggiornaTabella();
    }
    
    private void aggiungiIndirizzo() {
        
    }
    
    /**
     * Questo metodo stampa l'errore SQL. È facoltativo.
     * @param ex è l'eccezione da stampare
     */
    private void mostraErrore(SQLException ex) {
        String errore = "Errore di connessione al database";
        errore += "\nCodice: " + ex.getErrorCode();
        errore += "\nMessaggio: " + ex.getMessage();
        errore += "\n\n" + ex.getSQLState();
        JOptionPane.showMessageDialog(this, "Errore: " + errore, null, ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabella = new javax.swing.JTable();
        bDeleteAddress = new javax.swing.JButton();
        bAddAddress = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);

        tabella.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabella);

        bDeleteAddress.setText("Elimina indirizzo");
        bDeleteAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDeleteAddressActionPerformed(evt);
            }
        });

        bAddAddress.setText("Aggiungi indirizzo");
        bAddAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddAddressActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1012, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bAddAddress)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bDeleteAddress))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bDeleteAddress)
                    .addComponent(bAddAddress)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bDeleteAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDeleteAddressActionPerformed
        rimuoviIndirizzo();
    }//GEN-LAST:event_bDeleteAddressActionPerformed

    private void bAddAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddAddressActionPerformed
        aggiungiIndirizzo();
    }//GEN-LAST:event_bAddAddressActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAddAddress;
    private javax.swing.JButton bDeleteAddress;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabella;
    // End of variables declaration//GEN-END:variables
}
