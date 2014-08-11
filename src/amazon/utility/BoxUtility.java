/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.utility;

import amazon.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;

/**
 * Questa classe si occupa di impostare le combo box.
 * @author Francesco Bosso <fr.bosso at outlook.it>
 */
public class BoxUtility {
    
    /**
     * Imposta una JComboBox con gli indirizzi di un utente
     * @param menu JComboBox bersaglio
     * @param idUtente id utente da cui prelevare gli indirizzi
     * @throws SQLException 
     */
    public static void impostaPerIndirizzi(JComboBox menu, int idUtente) throws SQLException {
        ResultSet rsIndirizzi = DBConnection.visualizzaRubricaUtente(idUtente);
        menu.removeAllItems();
        while (rsIndirizzi.next()) {
            Contatto indirizzo = DBConnection.ottieniContatto(rsIndirizzi.getInt(1));
            menu.addItem(indirizzo);
        }
        //menu.setSelectedIndex(0);
    }
    
    /**
     * Ottenimento dell'indirizzo selezionato
     * @param menu JComboBox bersaglio
     * @return id del contatto
     */
    public static int getIndirizzoId(JComboBox menu) {
        Contatto con = (Contatto)menu.getSelectedItem();
        return con.getId();
    }
    
    /**
     * Imposta la JComboBox ad una selezione specifica a seconda dell'ID
     * dell'indirizzo specifico.
     * @param menu JComboBox bersaglio
     * @param id id dell'indirizzo
     */
    public static void setIndirizzoId(JComboBox menu, int id) {
        for (int i = 0; i < menu.getItemCount(); i++) {
            Contatto contatto = (Contatto)menu.getItemAt(i);
            if (contatto.getId() == id)
                menu.setSelectedIndex(i);
        }
    }
    
    /**
     * Imposta una JComboBox secondo i tipi di pagamento possibili
     * @param menu JComboBox bersaglio
     */
    public static void impostaTipoCarta(JComboBox menu) {
        //'Visa', 'Mastercard', 'Postepay', 'American_Express', 'CartaSi_Visa', 'CartaSi_Mastercard', 'Maestro'
            menu.removeAllItems();
            menu.addItem("VISA");
            menu.addItem("Mastercard");
            menu.addItem("Postepay");
            menu.addItem("American Express");
            menu.addItem("CartaSi Visa");
            menu.addItem("CartaSi Mastercard");
            menu.addItem("Maestro");
            menu.setSelectedIndex(0);
    }
    
    /**
     * Inizializza la JComboBox e imposta ad un elemento designato
     * @param menu
     * @param tipo 
     */
    public static void impostaTipoCarta(JComboBox menu, String tipo) {
        impostaTipoCarta(menu);
        setTipoCarta(menu, tipo);
    }
    
    /**
     * Imposta l'indice della JComboBox all'elemento selezionato
     * @param menu
     * @param tipo 
     */
    public static void setTipoCarta(JComboBox menu, String tipo) {
        menu.setSelectedItem(tipo);
    }
    
    /**
     * Non che serva poi tanto
     * @param menu
     * @return 
     */
    public static String getTipoCarta(JComboBox menu) {
        return menu.getSelectedItem().toString();
    }
    
    public static void impostaModPagamento(JComboBox menu, int utenteId) throws SQLException {
        menu.removeAllItems();
        ResultSet modPagamenti = DBConnection.visualizzaModPagamento(utenteId);
        while (modPagamenti.next()) {
            Contatto indirizzo = DBConnection.ottieniContatto(modPagamenti.getInt(1));
            int id = modPagamenti.getInt(2);
            String nome = modPagamenti.getString(4);
            String cognome = modPagamenti.getString(5);
            String tipo = modPagamenti.getString(6);
            String numero = modPagamenti.getString(3);
            String dataScadenza = modPagamenti.getString(7);
            int civ = modPagamenti.getInt(8);
            ModPagamento pagamento = new ModPagamento(id, numero, tipo, indirizzo);
            pagamento.setData(dataScadenza);
            pagamento.setNomeIntestatario(nome);
            pagamento.setCognomeIntestatario(cognome);
            pagamento.setCiv(civ);
            menu.addItem(pagamento);
        }
        //menu.setSelectedIndex(0);
    }
    
}
