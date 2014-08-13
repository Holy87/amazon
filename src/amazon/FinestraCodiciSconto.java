/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Francesco Bosso <fr.bosso@outlook.it>
 */
public class FinestraCodiciSconto extends FinestraConTabella {

    /**
     * Creates new form FinestraCodiciSconto
     * @param parent
     */
    public FinestraCodiciSconto(java.awt.Dialog parent) {
        super(parent, true);
        //initComponents();
    }
    
    public FinestraCodiciSconto(java.awt.Frame parent) {
        super(parent, true);
        //initComponents();
    }
    
    private String codice;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    protected ResultSet ottieniDati() throws SQLException {
        return DBConnection.visualizzaCodiciSconto();
    }

    @Override
    protected void nuovoElemento() {
        finestraModifica = new FinestraCodice(this);
        finestraModifica.show(ADDN, null, null);
        aggiornaTabella();
    }

    @Override
    protected void modificaElemento() {
        finestraModifica = new FinestraCodice(this);
        finestraModifica.show(EDIT, getDataCollection(), null);
        aggiornaTabella();
    }

    @Override
    protected void eliminaElemento() {
        try {
            DBConnection.eliminaCodiceSconto(codice);
            aggiornaTabella();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }

    @Override
    protected void aggiornaIdSelezionato() throws SQLException {
        codice = rs.getString(1);
    }
}
