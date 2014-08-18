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
 * @author Francesco Bosso <fr.bosso at outlook.it>
 */
public class FinestraStoricoOrdini extends javax.swing.JDialog {

    /**
     * Creates new form FinestraListaDesideri
     * @param parent
     * @param modal
     * @param utente
     */
    public FinestraStoricoOrdini(java.awt.Frame parent, boolean modal, int utente) {
        super(parent, modal);
        utenteID = utente;
        initComponents();
        impostaTabellaStorico();    //storico ordini
        impostaTabellaArticoli();   //articoli dell'ordine
    }
    
    private final int utenteID;
    private ResultSet rsStorico, rsArticoli, rsIndirizzo, rsFatturazione; //ResultSet su cui si basano i dati della tabella
    private DBTableModel modelloTabellaOrdini; //modello della tabella per i dati
    private DBTableModel modelloArticoli;
    private int cursoreStorico = 1; //memorizza la riga selezionata
    private int cursoreArticoli = 1; //memorizza la riga selezionata negli art.
    
    /**
     * Inizializza i dati della tabella, assegnandogli il modello e il rs.
     */
    @SuppressWarnings("Convert2Lambda")
    public final void impostaTabellaStorico() {
        modelloTabellaOrdini = new DBTableModel(rsStorico);//inserire il resultset nel costr.
        tabellaStorico.setModel(modelloTabellaOrdini); //metto il modellotabella nel
        tabellaStorico.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //non possono essere selezionati record multipli
        tabellaStorico.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            //implemento un evento che chiama tableSelectionChanged quando cambia la selezione della tabella
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        } 
                
        );
        //infine aggiorno il resultset della tabella
        aggiornaTabellaStorico();
    }
    
    /**
     * Inizializza i dati della tabella, assegnandogli il modello e il rs.
     */
    @SuppressWarnings("Convert2Lambda")
    public final void impostaTabellaArticoli() { //In questa tabella non è necessario usare la selezione
        modelloArticoli = new DBTableModel(rsArticoli);//inserire il resultset nel costr.
        tabellaArticoli.setModel(modelloArticoli); //metto il modellotabella nel
        tabellaArticoli.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //non possono essere selezionati record multipli
        tabellaArticoli.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            //implemento un evento che chiama tableSelectionChanged quando cambia la selezione della tabella
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableArticoliSelectionChanged();
            }
        } 
                
        );
        //infine aggiorno il resultset della tabella
        aggiornaTabellaArticoli();
    }
    
    /**
     * Aggiorna i dati della tabella desideri con i dati dell'utente.
     */
    public void aggiornaTabellaStorico()
    {
        try {
            rsStorico = ottieniDati(); //chiama il metodo in basso
                                //non è proprio necessario chiamare un metodo
                                //si può anche direttamente passare il reslts.
            modelloTabellaOrdini.setRS(rsStorico);
            rsStorico.absolute(cursoreStorico);   //attiva la riga del cursore attuale
            if ( rsStorico.getRow() == 0 )
                JOptionPane.showMessageDialog(this, "ERRORE: l'utente selezionato non ha ancora effettuato ordini", null, ERROR_MESSAGE);
            aggiornaSelezioneOrdine();           //imposta la selezione a riga singola
            
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Aggiorna i dati della tabella articoli a seconda della lista selezionata.
     */
    public void aggiornaTabellaArticoli()
    {
        try {
            rsIndirizzo = ottieniIndirizzo(idOrdine());
            rsFatturazione = ottieniFatturazione(idModPag());
            aggiornaDatiOrdine();
            rsArticoli = ottieniArticoli(idOrdine()); //chiama il metodo in basso
                                //non è proprio necessario chiamare un metodo
                                //si può anche direttamente passare il reslts
            modelloArticoli.setRS(rsArticoli);
            rsArticoli.absolute(cursoreArticoli);   //attiva la riga del cursore attuale
            mostraDatiArticoli();           //imposta la selezione a riga singola
            tabellaArticoli.getColumnModel().getColumn(4).setMinWidth(50);
            tabellaArticoli.getColumnModel().getColumn(4).setMaxWidth(50);
            tabellaArticoli.getColumnModel().getColumn(5).setMinWidth(50);
            tabellaArticoli.getColumnModel().getColumn(5).setMaxWidth(50);
        } catch (SQLException ex) {
            mostraErrore(ex);
        } catch (NullPointerException ex) {}
    }
    
    /**
     * Questo metodo restituisce il resultset necessario alla tabella
     * Modificatelo a seconda dei dati che volete visualizzare sulla tabella
     * @return resultset dei dati da mettere in tabella 
     */
    private ResultSet ottieniDati() throws SQLException {
        return DBConnection.visualizzaOrdiniUtente(utenteID);
    }
    
    private ResultSet ottieniArticoli(int idOrdine) throws SQLException {
        return DBConnection.visualizzaArticoliOrdine(idOrdine);
    }
    
    private ResultSet ottieniIndirizzo (int idOrdine) throws SQLException {
        return DBConnection.ottieniIndSpedOrdine(idOrdine);
    }
    
    private ResultSet ottieniFatturazione (int modPagID) throws SQLException {
        return DBConnection.ottieniFattSpedOrdine(modPagID);
    }
    
    private int idOrdine() {
        return Integer.parseInt(modelloTabellaOrdini.getValueAt(cursoreStorico - 1, 0).toString());
    }
    
    private int idModPag () {
        return Integer.parseInt(modelloTabellaOrdini.getValueAt(cursoreStorico - 1, 6).toString());
    }
    
    /**
     * Metodo che viene chiamato quando si clica il mouse sulla tabella
     * cambiando la selezione
     */
    private void tableSelectionChanged() 
    {
        try {
            rsStorico.absolute(tabellaStorico.getSelectionModel().getMinSelectionIndex() + 1);
            aggiornaSelezioneOrdine();
            //
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Metodo che viene chiamato quando si clicca il mouse sulla tabella
     * cambiando la selezione
     */
    private void tableArticoliSelectionChanged() 
    {
        try {
            rsArticoli.absolute(tabellaArticoli.getSelectionModel().getMinSelectionIndex() + 1);
            mostraDatiArticoli();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Mostra infine i dati sulla tabella dopo un aggiornamento
     */
    private void aggiornaSelezioneOrdine() {
      try {
          cursoreStorico = rsStorico.getRow();
          tabellaStorico.getSelectionModel().setSelectionInterval(cursoreStorico - 1,cursoreStorico - 1);
          tabellaStorico.setRowSelectionInterval(cursoreStorico - 1, cursoreStorico - 1);
          tabellaStorico.getColumnModel().getColumn(0).setMinWidth(0);
          tabellaStorico.getColumnModel().getColumn(0).setMaxWidth(0);
          tabellaStorico.getColumnModel().getColumn(2).setMinWidth(0);
          tabellaStorico.getColumnModel().getColumn(2).setMaxWidth(0);
          tabellaStorico.getColumnModel().getColumn(3).setMinWidth(0);
          tabellaStorico.getColumnModel().getColumn(3).setMaxWidth(0);
          tabellaStorico.getColumnModel().getColumn(4).setMinWidth(0);
          tabellaStorico.getColumnModel().getColumn(4).setMaxWidth(0);
          tabellaStorico.getColumnModel().getColumn(5).setMinWidth(0);
          tabellaStorico.getColumnModel().getColumn(5).setMaxWidth(0);
          tabellaStorico.getColumnModel().getColumn(6).setMinWidth(0);
          tabellaStorico.getColumnModel().getColumn(6).setMaxWidth(0);
          tabellaStorico.getColumnModel().getColumn(7).setMinWidth(0);
          tabellaStorico.getColumnModel().getColumn(7).setMaxWidth(0);
          tabellaStorico.getColumnModel().getColumn(8).setMinWidth(0);
          tabellaStorico.getColumnModel().getColumn(8).setMaxWidth(0);
          aggiornaTabellaArticoli();
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println(ex.getMessage());
      }
    }
    
    /**
     * Mostra infine i dati sulla tabella dopo un aggiornamento
     */
    private void mostraDatiArticoli() {
      try {
          cursoreArticoli = rsArticoli.getRow();
          tabellaArticoli.getSelectionModel().setSelectionInterval(cursoreArticoli - 1,cursoreArticoli - 1);
          tabellaArticoli.setRowSelectionInterval(cursoreArticoli - 1, cursoreArticoli - 1);
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println(ex.getMessage());
      }
    }
    
    private void aggiornaDatiOrdine() {
        try {
            rsIndirizzo.first();
            if ( rsIndirizzo.getString(4) != null ) {
                tIndirizzo.setText("<html>"
                    +rsIndirizzo.getString(1)+" "
                    +rsIndirizzo.getString(2)+"<br />"
                    +rsIndirizzo.getString(3)+"<br />"
                    +rsIndirizzo.getString(4)+"<br />"
                    +rsIndirizzo.getString(5)+" "
                    +rsIndirizzo.getString(6)+" ("
                    +rsIndirizzo.getString(7)+")<br />"
                    +rsIndirizzo.getString(8)+
                    "</html>");
            } else {
                tIndirizzo.setText("<html>"
                    +rsIndirizzo.getString(1)+" "
                    +rsIndirizzo.getString(2)+"<br />"
                    +rsIndirizzo.getString(3)+"<br />"
                    +rsIndirizzo.getString(5)+" "
                    +rsIndirizzo.getString(6)+" ("
                    +rsIndirizzo.getString(7)+")<br />"
                    +rsIndirizzo.getString(8)+
                    "</html>");
            }
            rsFatturazione.next();
            if ( rsFatturazione.getString(5) != null ) {
                tFatturazione.setText("<html>"
                    +rsFatturazione.getString(1)+" "
                    +rsFatturazione.getString(2)+"<br />"
                    +"Carta utilizzata: "+rsFatturazione.getString(3)+"<br />"
                    +rsFatturazione.getString(4)+"<br />"
                    +rsFatturazione.getString(5)+"<br />"
                    +rsFatturazione.getString(6)+" "
                    +rsFatturazione.getString(7)+" ("
                    +rsFatturazione.getString(8)+")<br />"
                    +rsFatturazione.getString(9)+
                    "</html>");
            } else {
                tFatturazione.setText("<html>"
                    +rsFatturazione.getString(1)+" "
                    +rsFatturazione.getString(2)+"<br />"
                    +"Carta utilizzata: "+rsFatturazione.getString(3)+"<br />"
                    +rsFatturazione.getString(4)+"<br />"
                    +rsFatturazione.getString(6)+" "
                    +rsFatturazione.getString(7)+" ("
                    +rsFatturazione.getString(8)+")<br />"
                    +rsFatturazione.getString(9)+
                    "</html>");
            }
            tPrezzi.setText("<html>"
                +"Prezzo netto: "+rsStorico.getFloat(4)+"€<br />"
                +"Sconto complessivo: -"+rsStorico.getFloat(5)+"€<br />"
                +"Costi di spedizione: "+rsStorico.getFloat(6)+"€<br />"
                +"Prezzo totale: "+rsStorico.getFloat(3)+"€<html />");
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
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
        tabellaStorico = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabellaArticoli = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tIndirizzo = new javax.swing.JLabel();
        tFatturazione = new javax.swing.JLabel();
        tPrezzi = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Storico degli ordini");
        setLocationByPlatform(true);

        tabellaStorico.setModel(new javax.swing.table.DefaultTableModel(
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
        tabellaStorico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabellaStoricoMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tabellaStorico);

        tabellaArticoli.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tabellaArticoli);

        jLabel1.setText("Storico Ordini:");

        jLabel2.setText("Articoli dell'Ordine:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Indirizzo di Spedizione");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Indirizzo di Fatturazione");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Prezzi al Dettaglio");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tIndirizzo, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tFatturazione, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(tPrezzi, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tIndirizzo, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tFatturazione, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tPrezzi, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tabellaStoricoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabellaStoricoMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tabellaStoricoMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel tFatturazione;
    private javax.swing.JLabel tIndirizzo;
    private javax.swing.JLabel tPrezzi;
    private javax.swing.JTable tabellaArticoli;
    private javax.swing.JTable tabellaStorico;
    // End of variables declaration//GEN-END:variables

}
