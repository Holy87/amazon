/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author frbos_000
 */
public class AnagraficaLibri extends javax.swing.JDialog {

    /**
     * Creates new form AnagraficaLibri
     */
    public AnagraficaLibri(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        impostaTabella();
        bPulisci.setVisible(false);
    }
    
    private ResultSet rs;
    private DBTableModel modelloTabella;
    private int cursore = 1;
    
    private final int FLESSIBILE = 2001;
    private final int RIGIDA = 2002;
    private final int EBOOK = 2003;
    private final int ALL = 0;
    
    private String parolaCercata = "";
    
    @SuppressWarnings("Convert2Lambda")
    public void impostaTabella() {
        modelloTabella = new DBTableModel(rs);
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
        aggiornaTabella();
    }
    
    /**
     * Aggiorna i dati della tabella con tutti i dati del database.
     */
    public void aggiornaTabella()
    {
        try {
            rs = resultSetAggiorna();
            modelloTabella.setRS(rs);
            rs.absolute(cursore);
            mostraDati();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        
    }
    
    private void eseguiRicerca(){
        parolaCercata = searchBox.getText();
        searchBox.setText(null);
        searchButton.setEnabled(false);
        tTitoloRicerca.setText("Ricerca su " + parolaCercata);
        bPulisci.setVisible(true);
        try {
            modelloTabella.setRS(resultSetRicerca());
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void aggiornaRicerca() {
        try {
            modelloTabella.setRS(resultSetRicerca());
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private ResultSet resultSetRicerca() throws SQLException {
        return DBConnection.visualizzaListinoLibri(parolaCercata, getSelezioneFormato(), getPrezzoMinimo(), getPrezzoMassimo());
    }
    
    protected ResultSet resultSetAggiorna() throws SQLException {
        return DBConnection.visualizzaListinoLibri();//.visualizzaLibriDisponibili();
    }
    
    /**
     * Mostra infine i dati sulla tabella dopo un aggiornamento
     */
    private void mostraDati() {
      try {
          cursore = rs.getRow();
          tabella.getSelectionModel().setSelectionInterval(cursore - 1,cursore - 1);
          tabella.setRowSelectionInterval(cursore - 1, cursore - 1);
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
            rs.absolute(tabella.getSelectionModel().getMinSelectionIndex() + 1);
            mostraDati();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void apriLibro() {
        FinestraDettagliLibro finestraDettagli = new FinestraDettagliLibro(null, false, modelloTabella.getValueAt(cursore-1, 1).toString());
        finestraDettagli.setVisible(true);
    }
    
    private int getPrezzoMinimo() {
        int prezzo = 0;
        try {
            prezzo = Integer.parseInt(tPrezzoMin.getText());
        } catch (NumberFormatException ex) {
            System.out.println(tPrezzoMin.getText()+" non è un valore accettabile");
            tPrezzoMin.setText("");
        }
        return prezzo;
    }
    
    private int getPrezzoMassimo() {
        int prezzo = 0;
        try {
            prezzo = Integer.parseInt(tPrezzoMax.getText());
            if (prezzo < getPrezzoMinimo())
                prezzo = getPrezzoMinimo();
        } catch (NumberFormatException ex) {
            tPrezzoMax.setText("");
        }
        return prezzo;
    }
    
    private int getSelezioneFormato() {
        if (rbFlessibile.isSelected())
            return FLESSIBILE;
        else if (rbRigida.isSelected())
            return RIGIDA;
        else if (rbEBook.isSelected())
            return EBOOK;
        else return ALL;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formatoLibri = new javax.swing.ButtonGroup();
        condizioniLibri = new javax.swing.ButtonGroup();
        searchBox = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabella = new javax.swing.JTable();
        rbFlessibile = new javax.swing.JRadioButton();
        rbEBook = new javax.swing.JRadioButton();
        rbTutti = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        rbRigida = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tPrezzoMin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tPrezzoMax = new javax.swing.JTextField();
        tTitoloRicerca = new javax.swing.JLabel();
        bPulisci = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Listino");

        searchBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBoxActionPerformed(evt);
            }
        });
        searchBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchBoxKeyTyped(evt);
            }
        });

        searchButton.setText("Cerca");
        searchButton.setEnabled(false);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

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
        tabella.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabellaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabella);

        formatoLibri.add(rbFlessibile);
        rbFlessibile.setText("Copertina flessibile");
        rbFlessibile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbFlessibileActionPerformed(evt);
            }
        });

        formatoLibri.add(rbEBook);
        rbEBook.setText("eBook digitale");
        rbEBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbEBookActionPerformed(evt);
            }
        });

        formatoLibri.add(rbTutti);
        rbTutti.setSelected(true);
        rbTutti.setText("Tutti");
        rbTutti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTuttiActionPerformed(evt);
            }
        });

        jLabel1.setText("Formato");

        formatoLibri.add(rbRigida);
        rbRigida.setText("Copertina rigida");
        rbRigida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRigidaActionPerformed(evt);
            }
        });

        jLabel2.setText("Prezzi");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Da €");

        tPrezzoMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tPrezzoMinActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("a €");

        tPrezzoMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tPrezzoMaxActionPerformed(evt);
            }
        });

        tTitoloRicerca.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N

        bPulisci.setText("Pulisci");
        bPulisci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPulisciActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbTutti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbFlessibile)
                            .addComponent(rbRigida)
                            .addComponent(rbEBook)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tPrezzoMin, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tPrezzoMax)))))
                        .addGap(0, 54, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tTitoloRicerca, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bPulisci, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchButton))
                        .addGap(0, 3, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rbFlessibile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbRigida)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbEBook)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbTutti)
                        .addGap(1, 1, 1)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(tPrezzoMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(tPrezzoMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tTitoloRicerca, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bPulisci))
                        .addGap(11, 11, 11)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        eseguiRicerca();
    }//GEN-LAST:event_searchButtonActionPerformed

    private void searchBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBoxKeyTyped
        searchButton.setEnabled(searchBox.getText().length() > 0);
    }//GEN-LAST:event_searchBoxKeyTyped

    private void tabellaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabellaMouseClicked
        apriLibro();
    }//GEN-LAST:event_tabellaMouseClicked

    private void searchBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBoxActionPerformed
        eseguiRicerca();
    }//GEN-LAST:event_searchBoxActionPerformed

    private void bPulisciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPulisciActionPerformed
        bPulisci.setVisible(false);
        tTitoloRicerca.setText("");
        aggiornaTabella();
        rbTutti.setSelected(true);
        parolaCercata = "";
    }//GEN-LAST:event_bPulisciActionPerformed

    private void rbFlessibileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbFlessibileActionPerformed
        aggiornaRicerca();
    }//GEN-LAST:event_rbFlessibileActionPerformed

    private void rbRigidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRigidaActionPerformed
        aggiornaRicerca();
    }//GEN-LAST:event_rbRigidaActionPerformed

    private void rbEBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbEBookActionPerformed
        aggiornaRicerca();
    }//GEN-LAST:event_rbEBookActionPerformed

    private void rbTuttiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTuttiActionPerformed
        aggiornaRicerca();
    }//GEN-LAST:event_rbTuttiActionPerformed

    private void tPrezzoMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tPrezzoMinActionPerformed
        aggiornaRicerca();
    }//GEN-LAST:event_tPrezzoMinActionPerformed

    private void tPrezzoMaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tPrezzoMaxActionPerformed
        aggiornaRicerca();
    }//GEN-LAST:event_tPrezzoMaxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bPulisci;
    private javax.swing.ButtonGroup condizioniLibri;
    private javax.swing.ButtonGroup formatoLibri;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JRadioButton rbEBook;
    private javax.swing.JRadioButton rbFlessibile;
    private javax.swing.JRadioButton rbRigida;
    private javax.swing.JRadioButton rbTutti;
    private javax.swing.JTextField searchBox;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField tPrezzoMax;
    private javax.swing.JTextField tPrezzoMin;
    private javax.swing.JLabel tTitoloRicerca;
    private javax.swing.JTable tabella;
    // End of variables declaration//GEN-END:variables
}
