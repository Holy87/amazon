/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author frbos_000
 */
public class FinestraDettagliLibro extends javax.swing.JDialog {

    /**
     * Creates new form FinestraDettagliLibro
     */
    public FinestraDettagliLibro(java.awt.Frame parent, boolean modal, LinkedList libro) {
        super(parent, modal);
        this.libro = libro;
        initComponents();
        
    }
    
    private LinkedList libro;
    private String isbn, titolo, autore, editore, formato, stato;
    private int prezzo, disponibilita;
    
    private ResultSet rs, rs2;
    private DBTableModel modelloTabellaVenditori;
    private DBTableModel modelloTabellaFormati;
    private int cursore = 1;
    private int cursore2 = 1;
    protected String id;
    
    @SuppressWarnings("Convert2Lambda")
    public final void impostaTabella() {
        modelloTabellaVenditori = new DBTableModel(rs);
        tabellaVenditori.setModel(modelloTabellaVenditori); //metto il modellotabella nel
        tabellaVenditori.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //non possono essere selezionati record multipli
        tabellaVenditori.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            //implemento un evento che chiama tableSelectionChanged quando cambia la selezione della tabella
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        } 
                
        );
        
        modelloTabellaFormati = new DBTableModel(rs2);
        tabellaFormati.setModel(modelloTabellaFormati);
        tabellaFormati.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaFormati.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                tableSelectionChanged2();
            }
        });
        aggiornaTabella();
        aggiornaTabella2();
    }
    
    /**
     * Aggiorna i dati della tabella con tutti i dati del database.
     */
    private void aggiornaTabella()
    {
        try {
            rs = DBConnection.visualizzaVenditoriLibro(isbn);
            modelloTabellaVenditori.setRS(rs);
            rs.absolute(cursore);
            mostraDati();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void aggiornaTabella2() {
        try {
            rs2 = DBConnection.visualizzaFormatoLibroVenditore(isbn, modelloTabellaVenditori.getValueAt(cursore-1, 0).toString());
            modelloTabellaFormati.setRS(rs2);
            rs2.absolute(cursore2);
            mostraDati2();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        
    }
    
    private void aggiornaDatiLibro() {
        tPrezzo.setText("€"+prezzo);
        tFormato.setText("Formato: " + formato);
        tCondizioni.setText("Condizioni: " + stato);
        tDisponibile.setText("Disponibilità: " + getDisponibilita());
    }
    
    private String getDisponibilita() {
        if (disponibilita < 0) {
            return "Illimitata";
        } else if (disponibilita > 10)
            return "più di 10 disponibili";
          else  
            return disponibilita + " disponibili";  
    }
    
    /**
     * Mostra infine i dati sulla tabella dopo un aggiornamento
     */
    private void mostraDati() {
      try {
          cursore = rs.getRow();
          tabellaVenditori.getSelectionModel().setSelectionInterval(cursore - 1,cursore - 1);
          tabellaVenditori.setRowSelectionInterval(cursore - 1, cursore - 1);
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println(ex.getMessage());
      }
    }
    
    private void mostraDati2() {
        try {
          cursore2 = rs2.getRow();
          tabellaFormati.getSelectionModel().setSelectionInterval(cursore2 - 1,cursore2 - 1);
          tabellaFormati.setRowSelectionInterval(cursore2 - 1, cursore2 - 1);
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println(ex.getMessage());
      }
    }
    
    private void mostraErrore(SQLException ex) {
        String errore = "Errore di connessione al database";
        errore += "\nCodice: " + ex.getErrorCode();
        errore += "\nMessaggio: " + ex.getMessage();
        errore += "\n\n" + ex.getSQLState();
        JOptionPane.showMessageDialog(this, "Errore: " + errore, null, ERROR_MESSAGE);
    }
    
    /**
     * Metodo che viene chiamato quando si clica il mouse sulla tabella
     * cambiando la selezione
     */
    private void tableSelectionChanged() 
    {
        try {
            rs.absolute(tabellaVenditori.getSelectionModel().getMinSelectionIndex() + 1);
            mostraDati();
            aggiornaTabella2();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void tableSelectionChanged2() {
        try {
            rs2.absolute(tabellaFormati.getSelectionModel().getMinSelectionIndex() + 1);
            mostraDati2();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tTitolo = new javax.swing.JLabel();
        tAutore = new javax.swing.JLabel();
        tVoto = new javax.swing.JLabel();
        tDisponibile = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        bCarrello = new javax.swing.JButton();
        tPrezzo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tQuantita = new javax.swing.JTextField();
        tEditore = new javax.swing.JLabel();
        tFormato = new javax.swing.JLabel();
        tCodice = new javax.swing.JLabel();
        tAnno = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabellaVenditori = new javax.swing.JTable();
        tCondizioni = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabellaFormati = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        tTitolo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tTitolo.setForeground(new java.awt.Color(150, 200, 25));
        tTitolo.setText("TITOLO");

        tAutore.setText("Autore");

        tVoto.setText("Voto: 5/5");

        tDisponibile.setText("Disponibilità: Disponibile");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Dettagli:");

        bCarrello.setBackground(new java.awt.Color(240, 100, 0));
        bCarrello.setText("Aggiungi al carrello");
        bCarrello.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCarrelloActionPerformed(evt);
            }
        });

        tPrezzo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        tPrezzo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tPrezzo.setText("€10.00");
        tPrezzo.setToolTipText("");

        jLabel3.setText("Quantità:");

        tQuantita.setText("1");

        tEditore.setText("Editore:");

        tFormato.setText("Formato:");

        tCodice.setText("ISBN:");

        tAnno.setText("Anno:");

        tabellaVenditori.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabellaVenditori);

        tCondizioni.setText("Condizioni: Nuovo");

        tabellaFormati.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tabellaFormati);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tTitolo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tAutore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tVoto, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tDisponibile, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)))
                        .addGap(71, 71, 71)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bCarrello, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(tPrezzo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tQuantita, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tEditore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tFormato, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tCodice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tAnno, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tCondizioni, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addGap(10, 10, 10))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tTitolo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tAutore))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tPrezzo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCarrello, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tVoto)
                    .addComponent(tDisponibile, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tQuantita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tEditore)
                    .addComponent(tCodice)
                    .addComponent(tCondizioni))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tFormato)
                    .addComponent(tAnno))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bCarrelloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCarrelloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bCarrelloActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCarrello;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel tAnno;
    private javax.swing.JLabel tAutore;
    private javax.swing.JLabel tCodice;
    private javax.swing.JLabel tCondizioni;
    private javax.swing.JLabel tDisponibile;
    private javax.swing.JLabel tEditore;
    private javax.swing.JLabel tFormato;
    private javax.swing.JLabel tPrezzo;
    private javax.swing.JTextField tQuantita;
    private javax.swing.JLabel tTitolo;
    private javax.swing.JLabel tVoto;
    private javax.swing.JTable tabellaFormati;
    private javax.swing.JTable tabellaVenditori;
    // End of variables declaration//GEN-END:variables
}
