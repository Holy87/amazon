/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import amazon.utility.BoxUtility;
import amazon.utility.Contatto;
import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 *
 * @author Francesco
 */
public class FinestraModificaMetodoPagamento extends EditForm {

    /**
     * Creazione della finestra
     * @param parent finestra chiamante
     * @param modal true per bloccare il genitore
     */
    public FinestraModificaMetodoPagamento(java.awt.Dialog parent, boolean modal, int utenteID) {
        super(parent, modal);
        initComponents();
        genitore = (FinestraModPagamento)parent;
        this.utenteID = utenteID;
    }
    
    private int pagamentoID;
    private final int utenteID;
    private String oldNum;
    private LinkedList<Contatto> contatti;
    private FinestraModPagamento genitore;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tNumero = new javax.swing.JTextField();
        tCiv = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tMese = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tAnno = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cContatto = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        tNome = new javax.swing.JTextField();
        tCognome = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        tCarta = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Aggiungi Metodo di Pagamento");
        setMinimumSize(new java.awt.Dimension(440, 310));
        getContentPane().setLayout(null);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Numero Carta di Credito");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(6, 39, 153, 28);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("CIV");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(6, 215, 21, 16);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Data di Scadenza");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(6, 180, 107, 16);

        tNumero.setToolTipText("Inserire il codice della carta di credito.");
        getContentPane().add(tNumero);
        tNumero.setBounds(177, 39, 257, 28);
        getContentPane().add(tCiv);
        tCiv.setBounds(177, 209, 52, 28);

        jLabel4.setText("mm");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(220, 180, 24, 16);
        getContentPane().add(tMese);
        tMese.setBounds(177, 174, 41, 28);

        jLabel5.setText("aaaa");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(320, 180, 30, 16);
        getContentPane().add(tAnno);
        tAnno.setBounds(260, 174, 60, 28);

        jLabel6.setText("Indirizzo di Fatturazione");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(6, 10, 153, 16);

        cContatto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cContatto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cContattoActionPerformed(evt);
            }
        });
        getContentPane().add(cContatto);
        cContatto.setBounds(177, 6, 257, 27);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Nome Titolare Carta");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(6, 73, 126, 28);
        getContentPane().add(tNome);
        tNome.setBounds(177, 73, 257, 28);
        getContentPane().add(tCognome);
        tCognome.setBounds(177, 107, 257, 28);

        jLabel8.setText("Codice di Sicurezza a 3 cifre");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(235, 215, 177, 16);

        jLabel9.setText("Tipo Carta");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(6, 145, 65, 16);

        jButton1.setText("Annulla");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(329, 251, 81, 29);

        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(256, 251, 67, 29);

        tCarta.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(tCarta);
        tCarta.setBounds(177, 141, 255, 27);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Cognome Titolare Carta");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(6, 107, 149, 28);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        eseguiOk();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        chiudiFinestra();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cContattoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cContattoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cContattoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cContatto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField tAnno;
    private javax.swing.JComboBox tCarta;
    private javax.swing.JTextField tCiv;
    private javax.swing.JTextField tCognome;
    private javax.swing.JTextField tMese;
    private javax.swing.JTextField tNome;
    private javax.swing.JTextField tNumero;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void fillContents() {
        pagamentoID = Integer.parseInt(dati.get(2).toString());
        BoxUtility.impostaTipoCarta(tCarta, dati.get(5).toString());
        oldNum = dati.get(1).toString();
        tNumero.setText(oldNum);
        tCiv.setText(dati.get(7).toString());
        tMese.setText(getMese(dati.get(6).toString()));
        tAnno.setText(getAnno(dati.get(6).toString()));
        tNome.setText(dati.get(3).toString());
        tCognome.setText(dati.get(4).toString());
        try {
            impostaComboBox(Integer.parseInt(dati.get(0).toString()));
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }

    @Override
    protected void cleanContents() {
        BoxUtility.impostaTipoCarta(tCarta);
        tNumero.setText("");
        tCiv.setText("");
        tMese.setText("");
        tAnno.setText("");
        tNome.setText("");
        tCognome.setText("");
        try {
            impostaComboBox();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }

    @Override
    protected String titoloNuovo() {
        return "Nuovo metodo di pagamento";
    }

    @Override
    protected String titoloModifica() {
        return "Modifica metodo di pagamento";
    }
    
    private String getMese(String data) {
        return data.substring(5, 7);
    }
    
    private String getAnno(String data) {
        return data.substring(0, 4);
    }
    
    private void impostaComboBox() throws SQLException {
        BoxUtility.impostaPerIndirizzi(cContatto, utenteID);
    }
    
    private void impostaComboBox(int idContatto) throws SQLException {
        impostaComboBox();
        BoxUtility.setIndirizzoId(cContatto, idContatto);
    }
    
    private void eseguiOk() {
        setVisible(false);
        try {
            if (mode == ADDN)
                DBConnection.creaModPagamento(BoxUtility.getIndirizzoId(cContatto),
                        tNumero.getText(), tNome.getText(), tCognome.getText(),
                        BoxUtility.getTipoCarta(tCarta), getScadenza(),
                        Integer.parseInt(tCiv.getText()));
            else
                DBConnection.aggiornaModPagamento(pagamentoID,
                        BoxUtility.getIndirizzoId(cContatto),
                        tNumero.getText(), tNome.getText(), tCognome.getText(),
                        BoxUtility.getTipoCarta(tCarta), getScadenza(),
                        Integer.parseInt(tCiv.getText()));
            genitore.aggiornaTabella();
            chiudiFinestra();
        } catch (SQLException ex) {
            mostraErrore(ex);
            setVisible(true);
        }
    }
    
    private String getScadenza() {
        String mese = tMese.getText();
        String anno = tAnno.getText();
        return anno+"-"+mese+"01";
    }
    
}
