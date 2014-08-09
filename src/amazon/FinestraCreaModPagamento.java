/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import amazon.exceptions.NoFormatSelectedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Francesco
 */
public class FinestraCreaModPagamento extends EditForm {

    /**
     * Creates new form FinestraCreaModPagamento
     * @param parent finestra principale
     * @param modal 1 se è un'aggiunta, 2 se è una modifica
     */
    public FinestraCreaModPagamento(java.awt.Frame parent, boolean modal, int utenteID, FinestraModPagamento finestraMod) {
        super(parent, modal);
        this.utenteID=utenteID;
        this.finestraMod = finestraMod;
        initComponents();
        impostaContatti();
        impostaTipoCarta();
        aggiornaRubricaSelezionato();
        aggiornaTipoCartaSelezionato();
    }
    
    /*public FinestraCreaModPagamento(java.awt.Dialog parent, boolean modal, int utenteID) {
        super(parent, modal);
        this.utenteID=utenteID;
        initComponents();
        impostaContatti();
        impostaTipoCarta();
        aggiornaRubricaSelezionato();
        aggiornaTipoCartaSelezionato();
    }*/
    
    private int contattoSelezionato;
    private String tipoCartaSelezionato;
    private final int utenteID;
    private final FinestraModPagamento finestraMod;
    private ArrayList<String[]> contatti = new ArrayList();
    private LinkedList<String> tipiCarta = new LinkedList();
    
    private void impostaContatti() {
        try {
            ResultSet rubrica = DBConnection.visualizzaRubricaUtente(utenteID);
            jComboBoxContactID.removeAllItems();
            System.out.println("Contatti della rubrica: "+DBConnection.contaRigheResultSet(rubrica));
            while (rubrica.next()) {
                String[] listaContatto = {rubrica.getString(1),//id metodo
                rubrica.getString(2),//nome
                rubrica.getString(3),//cognome
                rubrica.getString(4),//indirizzo_r1
                rubrica.getString(5),//indirizzo_r2
                rubrica.getString(6),//CAP
                rubrica.getString(7),//città
                rubrica.getString(8),//provincia
                rubrica.getString(9),//paese
                rubrica.getString(10)};//numtelefono
                String stringa = listaContatto[1] + " " + listaContatto[2];
                contatti.add(listaContatto);
                jComboBoxContactID.addItem(stringa);
            }
            jComboBoxContactID.setSelectedIndex(0);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void impostaTipoCarta() {
        //'Visa', 'Mastercard', 'Postepay', 'American_Express', 'CartaSi_Visa', 'CartaSi_Mastercard', 'Maestro'
            jComboBoxTipoCC.removeAllItems();
            
            tipiCarta.add("Visa");
            jComboBoxTipoCC.addItem("VISA");
            tipiCarta.add("Mastercard");
            jComboBoxTipoCC.addItem("Mastercard");
            tipiCarta.add("Postepay");
            jComboBoxTipoCC.addItem("Postepay");
            tipiCarta.add("American_Express");
            jComboBoxTipoCC.addItem("American Express");
            tipiCarta.add("CartaSi_Visa");
            jComboBoxTipoCC.addItem("CartaSi Visa");
            tipiCarta.add("CartaSi_Mastercard");
            jComboBoxTipoCC.addItem("CartaSi Mastercard");
            tipiCarta.add("Maestro");
            jComboBoxTipoCC.addItem("Maestro");
            
            jComboBoxTipoCC.setSelectedIndex(0);
    }
    
    private int contattoRubricaSelezionato() {
        return Integer.parseInt(contatti.get(jComboBoxContactID.getSelectedIndex())[0]);
    }

    private String tipoCartaSelezionato() {
        return tipiCarta.get(jComboBoxTipoCC.getSelectedIndex());
    }
    
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    private void aggiornaRubricaSelezionato() {
        try {
            contattoSelezionato = Integer.parseInt(contatti.get(jComboBoxContactID.getSelectedIndex())[0]);
        } catch (Exception ex) {
            contattoSelezionato = 0;
        }
    }

    private void aggiornaTipoCartaSelezionato() {
        try {
            tipoCartaSelezionato = tipiCarta.get(jComboBoxTipoCC.getSelectedIndex());
        } catch (Exception ex) {
            tipoCartaSelezionato = "";
        }
        //LinkedList pagamento = metodiPagamento.get(cPagamento.getSelectedIndex());
        //pagamentoSelezionato = Integer.parseInt(pagamento.get(0).toString());
        //tIntestatario.setText(pagamento.get(1).toString() + " " + pagamento.get(2).toString());
        //tScadenzaCarta.setText(pagamento.get(5).toString());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tID = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        bEsc = new javax.swing.JButton();
        bOk = new javax.swing.JButton();
        tnumeroCC = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tnomeCC = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        tcognomeCC = new javax.swing.JTextField();
        jComboBoxContactID = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jComboBoxTipoCC = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        tscadenzaCC_mm = new javax.swing.JTextField();
        tscadenzaCC_aa = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        tcodSicurezzaCC = new javax.swing.JTextField();

        jLabel9.setText("Descrizione");

        jLabel12.setText("N. Pagine");

        jLabel3.setText("ID");

        tID.setEnabled(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modifica Libro");

        jLabel1.setText("Intestatario Fatturazione");

        jLabel2.setText("N. Carta di Credito");

        bEsc.setText("Annulla");
        bEsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEscActionPerformed(evt);
            }
        });

        bOk.setText("OK");
        bOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkActionPerformed(evt);
            }
        });

        tnumeroCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tnumeroCCActionPerformed(evt);
            }
        });

        jLabel7.setText("Nome Titolare Carta");

        jLabel15.setText("Cognome Titolare Carta");

        jComboBoxContactID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel16.setText("Tipo Carta");

        jComboBoxTipoCC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel17.setText("Data di Scadenza (mm/aa)");

        jLabel18.setText("Codice di Sicurezza");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bOk, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bEsc))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(43, 43, 43))
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tnumeroCC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                                .addComponent(tnomeCC)
                                .addComponent(tcognomeCC)
                                .addComponent(jComboBoxContactID, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBoxTipoCC, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tscadenzaCC_mm, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tscadenzaCC_aa, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tcodSicurezzaCC, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxContactID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tnumeroCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tnomeCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(tcognomeCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jComboBoxTipoCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tscadenzaCC_mm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tscadenzaCC_aa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tcodSicurezzaCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bEsc)
                    .addComponent(bOk)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOkActionPerformed
        eseguiOk();
    }//GEN-LAST:event_bOkActionPerformed

    private void bEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEscActionPerformed
        chiudiFinestra();
    }//GEN-LAST:event_bEscActionPerformed

    private void tnumeroCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tnumeroCCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tnumeroCCActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bEsc;
    private javax.swing.JButton bOk;
    private javax.swing.JComboBox jComboBoxContactID;
    private javax.swing.JComboBox jComboBoxTipoCC;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField tID;
    private javax.swing.JTextField tcodSicurezzaCC;
    private javax.swing.JTextField tcognomeCC;
    private javax.swing.JTextField tnomeCC;
    private javax.swing.JTextField tnumeroCC;
    private javax.swing.JTextField tscadenzaCC_aa;
    private javax.swing.JTextField tscadenzaCC_mm;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void fillContents() {
        tnumeroCC.setText((String)dati.get(1));
        tnomeCC.setText((String)dati.get(2));
        tcognomeCC.setText((String)dati.get(3));
        tscadenzaCC_mm.setText((String)dati.get(5));
        tscadenzaCC_aa.setText((String)dati.get(6));
        tcodSicurezzaCC.setText((String)dati.get(7));
    }

    @Override
    protected void cleanContents() {
        tnumeroCC.setText("");
        tnomeCC.setText("");
        tcognomeCC.setText("");
        tscadenzaCC_aa.setText("");
        tscadenzaCC_mm.setText("");
        tcodSicurezzaCC.setText("");
    }
    
    private void eseguiOk() {
        String tscadenzaCC = "20"+tscadenzaCC_aa.getText()+"-"+tscadenzaCC_mm.getText()+"-01 00:00:00";
        
        try {
            setVisible(false);
            DBConnection.creaModPagamento(contattoRubricaSelezionato(), tnumeroCC.getText(), tnomeCC.getText(), tcognomeCC.getText(), tipoCartaSelezionato(), tscadenzaCC, Integer.parseInt(tcodSicurezzaCC.getText()));
            finestraMod.aggiornaTabella();
            //chiudiFinestra();
           
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
        
  /*  
    private void eseguiOk()
    {
        
        try {
            setVisible(false);
            if (mode == ADDN)
                DBConnection.creaLibro(tNomeLibro.getText(), Integer.parseInt(tnumeroCC.getText()), tnomeCC.getText(), tcognomeCC.getText(), tGenere.getText(), Integer.parseInt(tscadenzaCC_aa.getText()), Integer.parseInt(tscadenzaCC_mm.getText()), tcodSicurezzaCC.getText());
            else
                DBConnection.aggiornaLibro(oldISBN, tNomeLibro.getText(), Integer.parseInt(tnumeroCC.getText()), tnomeCC.getText(), tcognomeCC.getText(), tGenere.getText(), Integer.parseInt(tscadenzaCC_aa.getText()), Integer.parseInt(tscadenzaCC_mm.getText()), tcodSicurezzaCC.getText());
            chiudiFinestra();
        aggiornaModPagamento();
        }
        catch(SQLException ex){
            mostraErrore(ex);
            setVisible(true);
        } catch (NoFormatSelectedException ex) {
            JOptionPane.showMessageDialog(this, "Devi selezionare almeno un formato", "Errore!", JOptionPane.ERROR_MESSAGE, null);
        }
        
    }
    */
    @Override
    protected String titoloNuovo () {
        return "Nuova Modalità di Pagamento";
    }
    
    @Override
    protected String titoloModifica () {
        return "Modifica Modalità di Pagamento";
    }
}
