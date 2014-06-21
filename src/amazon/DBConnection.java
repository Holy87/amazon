/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import oracle.jdbc.pool.*;
/**
 *
 * @author Francesco
 */
public class DBConnection {
    private static OracleDataSource ods;
    public static Connection conn = null;
    private static DatabaseMetaData meta;
    private static String tempUser, tempPass, tempDomain, tempPort;
    /**
    * Indica che il frame si trova nello stato di inserimento di un nuovo record
    * o dei parametri di ricerca.
    */
   final static public int APPEND_QUERY = 1;
   /**
    * Indica che il frame si trova nello stato di navigazione (ricerca
    * gi&agrave; effettuata).
    */
   final static public int BROWSE = 2;
   /**
    * Indica che il frame si trova nello stato di modifica dei dati.
    */
   final static public int UPDATE = 3;
   /**
    * indica che l'eccezione &egrave; stata sollevata eseguendo un comando
    * <i>SELECT</i>.
    */
   final static public int CONTESTO_ESEGUI_QUERY = 1;
    
    /**
     * 
     * @throws SQLException 
     * @param usernm nome utente per il collegamento
     * @param passwd password da immettere per il collegamento
     * Avvia la connessione con il server
     */
    public static void StartConnection(String usernm, String passwd, String domain, String port) throws SQLException {
        tempUser = usernm;
        tempPass = passwd;
        tempDomain = domain;
        tempPort = port;
        ods = new oracle.jdbc.pool.OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@//"+domain+":"+port+"/xe");//143.225.117.238:1521/xe");
        //Stampa Versione Driver
        conn = ods.getConnection(usernm, passwd);
        meta = conn.getMetaData();
    }
    
    public static void reConnect() throws SQLException {
        if (conn != null){
            StartConnection(tempUser, tempPass, tempDomain, tempPort);
        }
    }
    
    /**
     * Chiude la connessione con il server
     * @throws SQLException 
     */
    public static void CloseConnection() throws SQLException {
        if (conn != null) conn.close();
    }
    
    public static boolean connected() {
        return conn != null;
    }
    
    public static List getAuthors() throws SQLException {
        List autori = new LinkedList();
        if (conn != null) {
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT * FROM AUTORI");
            while (rset.next()) {
                Author autore = new Author();
                autore.setId(rset.getInt(1));
                autore.setName(rset.getString(2));
                autore.setSurname(rset.getString(3));
                autori.add(autore);
            } 
            
        }
        return autori;
    }
    
    /**
    * Effettua una query e restituisce il primo valore.
    * 
    * @param query String contenente l'interrogazione
    * @return oggetto contenente la prima colonna della prima riga del risultato
    */
   static public Object getValue(String query) {
      Object obj;
      Statement st;
      ResultSet rs;
      obj = null;
      try {
         st = conn.createStatement();
         rs = st.executeQuery(query);
         rs.next();
         obj = rs.getObject(1);
      } catch (SQLException e) {  //nessuna azione
      }
      return obj;
   }

   /**
    * Effettua una query e restituisce il primo valore.
    * 
    * @param query String contenente l'interrogazione con segnaposto
    * @param codice int per rimpiazzare il segnaposto
    * @return oggetto contenente la prima colonna della prima riga del risultato
    */
   static public Object getValue(String query, int codice) {
      Object ret;
      PreparedStatement st;
      ResultSet rs;
      ret = null;
      System.out.println(query + codice); //debug
      try {
         st = conn.prepareStatement(query);
         st.setInt(1, codice);
         rs = st.executeQuery();
         rs.next();
         ret = rs.getObject(1);
      } catch (SQLException e) {
      }
      return ret;
   }
   
   public static void creaOrdine (int idUtente, int costospedin, int scontocomplin, int idContatto) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       ResultSet rs; //Variabile dove inserire i risultati della Query
       int idOrder; //id dell'ordine da usare per l'aggiunta in COMPARTICOLI e SPEDIZIONE
       int countCourier;
       
       pstmt = conn.prepareStatement("INSERT INTO ORDINI(UTENTE_ID, DATAORDINE, PREZZONETTO, COSTOSPED, SCONTOCOMPL) VALUES(?, SYSDATE, (SELECT SUM(PREZZOVENDITA) FROM COMPARTICOLI WHERE (?=UTENTE_ID AND ORDINE_ID=NULL)), ?, ?)");
       pstmt.setInt(1, idUtente);
       pstmt.setInt(2, idUtente);
       pstmt.setInt(3, costospedin);
       pstmt.setInt(4, scontocomplin);
       
       pstmt.executeUpdate();
       pstmt.close();
       
       //Query per visualizzare l'ultimo record inserito
       PreparedStatement lastorder;
       lastorder = conn.prepareStatement("SELECT ORDINE_ID FROM UTENTI WHERE ROWNUM <=1 ORDER BY UTENTE_ID DESC;");
       rs = pstmt.executeQuery();
       rs.next();
       idOrder = rs.getInt(1);
       lastorder.close();
       
       //Aggiornamento di COMPARTICOLI
       PreparedStatement updateCompArticoli;
       updateCompArticoli = conn.prepareStatement("UPDATE COMPARTICOLI SET ORDINE_ID=? WHERE (UTENTE_ID=? AND ORDINE_ID=NULL);");
       updateCompArticoli.setInt(1, idOrder);
       updateCompArticoli.setInt(2, idUtente);
       updateCompArticoli.executeUpdate();
       updateCompArticoli.close();
       
       //Calcolo di PREZZO TOTALE
       PreparedStatement totalPrice;
       totalPrice = conn.prepareStatement("UPDATE ORDINI SET PREZZOTOTALE=(PREZZONETTO+COSTOSPED)-SCONTOCOMPL WHERE (ORDINE_ID=?);");
       totalPrice.setInt(1, idOrder);
       totalPrice.executeUpdate();
       totalPrice.close();
       
       //Immissione corriere casuale (provare ad usare un solo tipo Statement)
       PreparedStatement countDelivery; //conta quanti corrieri hanno quel tipo di spedizione
       countDelivery = conn.prepareStatement("SELECT COUNT(*) AS NUMCORRIERI FROM MOD_SPEDIZIONE WHERE COSTOSPED=?;");
       countDelivery.setInt(1, costospedin);
       ResultSet numDelivery = countDelivery.executeQuery();
       numDelivery.next();
       countCourier = numDelivery.getInt(1);
       countDelivery.close();
       
       PreparedStatement showDelivery; //sceglie il corriere da uno casuale;
       ResultSet choiceDelivery;
       showDelivery = conn.prepareStatement("SELECT CORRIERE_ID FROM MOD_SPEDIZIONE WHERE COSTOSPED=?");
       showDelivery.setInt(1, costospedin);
       choiceDelivery = showDelivery.executeQuery();
       int randCourier = (int) ((Math.random() * countCourier) + 1);
       int i = 0; //indice contatore del while
       int idCourier = 0;
       
       while (choiceDelivery.next() && i <= randCourier) {
           idCourier = choiceDelivery.getInt(1);
           i++;
       }
       showDelivery.close();
       
       //Inserimento spedizioni
       PreparedStatement insertDelivery;
       insertDelivery = conn.prepareStatement("INSERT INTO SPEDIZIONI(CORRIERE_ID, ORDINE_ID, CONTACT_ID, DATACONSEGNA) VALUES (?, ?, ?, SYSDATE);");
       insertDelivery.setInt(1, idCourier);
       insertDelivery.setInt(2, idOrder);
       insertDelivery.setInt(3, idContatto);
       insertDelivery.close();
       
   }
}
