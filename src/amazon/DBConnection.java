/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import amazon.exceptions.CodeNotValidException;
import amazon.utility.Contatto;
import amazon.utility.Scontotemp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import oracle.jdbc.pool.*;
/**
 *
 * @author Francesco
 */
public class DBConnection {
    private static OracleDataSource ods;
    public static Connection conn = null;
    public static String tempUser, tempPass, tempHost, tempPort, tempSchema;
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
     * Avvia la connessione con il server
     */
    public static void StartConnection() throws SQLException {
        ods = new OracleDataSource();
        ods.setDriverType("thin");
        ods.setServerName(tempHost);
        ods.setPortNumber(Integer.parseInt(tempPort));
        ods.setUser(tempUser);//143.225.117.238:1521/xe
        ods.setPassword(tempPass);
        ods.setDatabaseName("xe");
        conn = ods.getConnection();
    }
    
    /**
     * chiude la connessione e riconnette l'applicazione al database con l'ultima
     * configurazione di ID e password
     * @throws SQLException 
     */
    public static void reConnect() throws SQLException {
        if (conn != null){
            StartConnection();
        }
    }
    
    /**
     * Chiude la connessione con il server
     * @throws SQLException 
     */
    public static void CloseConnection() throws SQLException {
        if (conn != null) conn.close();
    }
    
    /**
     * Verifica se la connessione è attiva.
     * @return true se è connesso, false altrimenti.
     */
    public static boolean connected() {
        return conn != null;
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
      } catch (SQLException e) {}
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
   
   /**
    * Restituisce un ResultSet di una query specificata in imput da stringa.
    * @param query
    * @return
    * @throws SQLException 
    */
   public static ResultSet eseguiQuery(String query) throws SQLException
   {
       Statement pstmt;
       pstmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

       ResultSet rs = pstmt.executeQuery(query);
       return rs;
   }
   
      /**
    * Eliminazione di un record da una tabella secondo i parametri
    * @param id identificatore del record
    * @param tabella nome della tabella da cui risiede il record
    * @param nomeColonna nome dell'attributo dell'ID
    * @throws SQLException 
    */
   public static void eliminaRecord(String id, String tabella, String nomeColonna) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       pstmt = conn.prepareStatement("DELETE FROM " + tabella + " WHERE " + nomeColonna + " = ?");
       pstmt.setString(1, id);
       pstmt.executeUpdate();
   }
   
   /**
    * Ottiene l'ID del prdotto dal magazzino libri passando l'ISBN, venditore e formato
    * @param isbn stringa, isbn dell'entità libri
    * @param venditoreId intero id del venditore
    * @param formatoId intero formato del libro
    * @param tipoCondizione "Nuovo", "Usato" o "Ricondizionato"
    * @return ID del prodotto PROD_ID
    * @throws SQLException 
    */
   public static int ottieniIdProdotto(String isbn, int venditoreId, int formatoId, String tipoCondizione) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT PROD_ID FROM MAGAZZINO_LIBRI WHERE VENDITORE_ID = ?, ISBN = ?, FORMATO_ID = ?, TIPOCONDIZIONE = ?");
       pstmt.setInt(1, venditoreId);
       pstmt.setString(2, isbn);
       pstmt.setInt(3, formatoId);
       pstmt.setString(4, tipoCondizione);
       ResultSet rs = pstmt.executeQuery();
       return Integer.parseInt(rs.getString(1));
   }
      
   /**
    * Visualizza l'attuale carrello dell'utente dato il suo ID
    * @param idUtente
    * @return ISBN, FORMATO_ID, VENDITORE_ID, LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, PREZZOVENDITA, VENDITORE_NOME, QUANTITÀ, PROD_ID
    * @throws SQLException 
    */
   public static ResultSet visualizzaCarrello(int idUtente) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT VIEW_INFO.ISBN, VIEW_INFO.FORMATO_ID, VIEW_INFO.LIBRO_NOME, VIEW_INFO.FORMATO_NOME, VIEW_INFO.TIPOCONDIZIONE, MAGAZZINO_LIBRI.PREZZOVENDITA, VIEW_INFO.VENDITORE_NOME, COMPARTICOLI.QUANTITÀ, MAGAZZINO_LIBRI.PROD_ID FROM COMPARTICOLI LEFT OUTER JOIN MAGAZZINO_LIBRI ON MAGAZZINO_LIBRI.PROD_ID = COMPARTICOLI.PROD_ID JOIN VIEW_INFO ON VIEW_INFO.PROD_ID = COMPARTICOLI.PROD_ID WHERE UTENTE_ID=? AND ORDINE_ID IS NULL",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idUtente);
       return pstmt.executeQuery();
   }
   
   /**
    * Elimina un articolo dal carrello in base ai dati in ingresso specificati nel metodo
    * @param idUtente
    * @param prodID
    * @throws SQLException 
    */
   public static void eliminaArticoloCarrello(int idUtente, int prodID) throws SQLException {
       PreparedStatement pstmt;
        pstmt = conn.prepareStatement("DELETE FROM COMPARTICOLI WHERE UTENTE_ID=? AND PROD_ID=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        pstmt.setInt(1, idUtente);
        pstmt.setInt(2, prodID);
   }
   
   /**
    * Visualizza gli ordini effettuati dall'utente dato il suo ID
    * @param idUtente
    * @return ORDINE_ID, DATAORDINE, PREZZOTOTALE
    * @throws SQLException 
    */
   public static ResultSet visualizzaOrdiniUtente(int idUtente) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT ORDINI.ORDINE_ID, DATAORDINE, PREZZOTOTALE, ORDINI.PREZZONETTO, ORDINI.SCONTOCOMPL, COSTOSPED, MOD_PAGAMENTO_ID, CONTACT_ID, CORRIERE_ID FROM ORDINI JOIN SPEDIZIONI ON ORDINI.ORDINE_ID=SPEDIZIONI.ORDINE_ID WHERE UTENTE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idUtente);
       
       return pstmt.executeQuery();
   }
   
   /**
    * Visualizza gli articoli presenti in un ordine di un utente
    * @param idOrdine
    * @return LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, QUANTITÀ, PREZZOVENDITA
    * @throws SQLException 
    */
   public static ResultSet visualizzaArticoliOrdine(int idOrdine) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT DISTINCT VIEW_INFO.LIBRO_NOME, VIEW_INFO.FORMATO_NOME, VIEW_INFO.VENDITORE_NOME, VIEW_INFO.TIPOCONDIZIONE, COMPARTICOLI.QUANTITÀ, COMPARTICOLI.PREZZOVENDITA FROM COMPARTICOLI JOIN VIEW_INFO ON VIEW_INFO.PROD_ID = COMPARTICOLI.PROD_ID WHERE ORDINE_ID=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idOrdine);
       
       return pstmt.executeQuery();
   }
   
   /**
    * Restituisce le modalità di pagamento di un utente
    * @param idUtente
    * @return *
    * @throws SQLException
    */
   public static ResultSet visualizzaModPagamento (int idUtente) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT * FROM MOD_PAGAMENTO NATURAL JOIN RUBRICA_INDIRIZZI WHERE UTENTE_ID=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idUtente);
       
       return pstmt.executeQuery();
   }
   
   /**
    * Elimina una modalità di pagamento
    * @param pagamentoID pagamento bersaglio
    * @throws SQLException 
    */
   public static void eliminaModPagamento(int pagamentoID) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("DELETE FROM MOD_PAGAMENTO WHERE MOD_PAGAMENTO_ID = ?");
       pstmt.setInt(1,pagamentoID);
       
       pstmt.executeUpdate();
       pstmt.close(); 
   }
   
   /**
    * Inserisce una nuova modalità di pagamento per un utente
     * @param contactID
     * @param numeroCC
     * @param nomeCC
     * @param cognomeCC
     * @param tipoCC
     * @param scadenzaCC
     * @param codSicurezzaCC
    * @throws SQLException
    */
   public static void creaModPagamento (int contactID, String numeroCC, String nomeCC, String cognomeCC, String tipoCC, String scadenzaCC, int codSicurezzaCC) throws SQLException {
       PreparedStatement pstmt;
       
       pstmt = conn.prepareStatement("INSERT INTO MOD_PAGAMENTO (CONTACT_ID,NUMEROCARTACREDITO,TITOLARECARTA_NOME,TITOLARECARTA_COGNOME,TIPOCARTA,DATASCADENZA,CODICESICUREZZA) VALUES (?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?)",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, contactID);
       pstmt.setString(2, numeroCC);
       pstmt.setString(3, nomeCC);
       pstmt.setString(4, cognomeCC);
       pstmt.setString(5, tipoCC);
       pstmt.setString(6, scadenzaCC);
       pstmt.setInt(7, codSicurezzaCC);
       pstmt.executeUpdate();
       pstmt.close();    
   }
   
   /**
    * Aggiorna la modalità di pagamento
    * @param modPagamentoID id della mod pag
    * @param contactID id dell'ID dell'indirizzo di fatturazione
    * @param numeroCC nuovo numero di carta di credito
    * @param nomeCC nome dell'intestatario
    * @param cognomeCC cognome dell'intestatario
    * @param tipoCC VISA, MASTERCARD ecc...
    * @param scadenzaCC in stringa, AAAA-MM-DD
    * @param codSicurezzaCC codice di sicurezza CIV
    * @throws SQLException 
    */
   public static void aggiornaModPagamento (int modPagamentoID, int contactID, String numeroCC, String nomeCC, String cognomeCC, String tipoCC, String scadenzaCC, int codSicurezzaCC) throws SQLException {
       PreparedStatement pstmt;
       
       pstmt = conn.prepareStatement("UPDATE MOD_PAGAMENTO SET CONTACT_ID = ?, NUMEROCARTACREDITO = ?, TITOLARECARTA_NOME = ?, TITOLARECARTA_COGNOME = ?, TIPOCARTA = ?, DATASCADENZA = TO_DATE(?, 'YYYY-MM-DD'), CODICESICUREZZA = ? WHERE MOD_PAGAMENTO_ID = ?");
       
       pstmt.setInt(1, contactID);
       pstmt.setString(2, numeroCC);
       pstmt.setString(3, nomeCC);
       pstmt.setString(4, cognomeCC);
       pstmt.setString(5, tipoCC);
       pstmt.setString(6, scadenzaCC);
       pstmt.setInt(7, codSicurezzaCC);
       pstmt.setInt(8, modPagamentoID);
       pstmt.executeUpdate();
       pstmt.close();
   }
   
      public static ResultSet sceltaModPagamento (int utenteId) throws SQLException {
       //Esempio: UTENTE_ID = 423575;
       /*RISULTATO QUERY:
                    TITOLARECARTA_NOME      TITOLARECARTA_COGNOME   TIPOCARTA   NUMEROCARTACREDITO      DATASCADENZA
                    Roberto                 Di Carlo                Mastercard	4172836483428572	01-GEN-24
       */
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT MOD_PAGAMENTO_ID, TITOLARECARTA_NOME, TITOLARECARTA_COGNOME, TIPOCARTA, NUMEROCARTACREDITO, DATASCADENZA FROM RUBRICA_INDIRIZZI NATURAL JOIN MOD_PAGAMENTO WHERE UTENTE_ID=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, utenteId);
       
       return pstmt.executeQuery();
   }
   
   /**
    * Restituisce il valore di sconto del codice, lancia eccezione se non valido
    * @param codice
    * @return SCONTO
    * @throws SQLException
    * @throws CodeNotValidException se il codice è già stato usato o non è valido
    */
   public static double verificaSconto(String codice) throws SQLException, CodeNotValidException {
        ResultSet rs;
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement("SELECT SCONTO FROM SCONTO_CODICI WHERE CODPROMO LIKE '?' AND ORDINE_ID IS NULL",
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
        pstmt.setString(1, codice);

        rs=pstmt.executeQuery();
        rs.last();
        double ritorno = 0.0;
        try {
            rs.first();
            ritorno = rs.getDouble(1);
        }
        catch(SQLException ex) {
            //MANDA UNA ECCEZIONE DI CODICE GIA USATO.
            throw new CodeNotValidException();
        }     
        return ritorno;
   }
   
   /**
    * Collega i codici promozionali utilizzati all'ordine dell'utente.
    * @param sconti Array di sconti(codice sconto, sconto)
    * @param ordineId id dell'ordine
    * @throws SQLException 
    */
   public static void applicaScontoOrdine(LinkedList<Scontotemp> sconti, int ordineId) throws SQLException {
        PreparedStatement pstmt;
        String codPromo;
        
        for(Scontotemp preso : sconti)    {
            codPromo=preso.getcodPromo();
            
            pstmt = conn.prepareStatement("UPDATE CODICI_SCONTO SET ORDINE_ID=? WHERE CODPROMO='?'");
            pstmt.setInt(1, ordineId);
            pstmt.setString(2, codPromo);
            
            pstmt.executeUpdate();
            pstmt.close();
        }  
   }
   
   /**
    * Converte il carrello dell'utente in un ordine da spedire
    * @param idUtente id dell'utente, intero
    * @param sped costo previsto dalla modalità di spedizione
    * @param sconto valore complessivo dello sconto
    * @param modpagamento id del metodo di pagamento
    * @param contactID id del contatto dell'indirizzo di spedizione
    * @param sconti codici di sconto
    * @throws SQLException 
    */
   public static void creaOrdine (int idUtente, int sped, double sconto, int modpagamento, int contactID, LinkedList<Scontotemp> sconti) throws SQLException {
       //NOTA2 = gestire i pezzi disponibili. Checkare e sottrarre solo se il formato ID è 2001 o 2002.
       
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       ResultSet rs; //Variabile dove inserire i risultati della Query
       int idOrder=0; //id dell'ordine da usare per l'aggiunta in COMPARTICOLI e SPEDIZIONE
       
       pstmt = conn.prepareStatement("INSERT INTO ORDINI(UTENTE_ID, DATAORDINE, COSTOSPED, SCONTOCOMPL, MOD_PAGAMENTO_ID) VALUES(?,SYSDATE,?,?,?)");
       pstmt.setInt(1, idUtente);
       pstmt.setInt(2, sped);
       pstmt.setDouble(3, sconto);
       pstmt.setInt(4, modpagamento);
       
       pstmt.executeUpdate();
       pstmt.close();
       
       PreparedStatement getID; //Statement per ottenere l'ID
       getID = conn.prepareStatement("SELECT ORDINE_ID FROM ORDINI WHERE UTENTE_ID = ? ORDER BY DATAORDINE DESC");
       getID.setInt(1, idUtente);
       rs=getID.executeQuery();
       rs.next();
       
       idOrder=rs.getInt(1); //Ottenimento ORDINE_ID riga inserita
       
       getID.close();
       
       PreparedStatement pstmt2; //Statement per il richiamo della funzione per il completamento
       
       pstmt2 = conn.prepareStatement("BEGIN CREA_ORDINE_PART_2(?,?,?,?,?); END;");
       pstmt2.setInt(1, idUtente);
       pstmt2.setInt(2, idOrder);
       pstmt2.setInt(3, sped);
       pstmt2.setInt(4, modpagamento);
       pstmt2.setInt(5, contactID);
       pstmt2.execute();
       
       if ( sconti.isEmpty() ) //Verifica se l'array non è stato riempito
           applicaScontoOrdine(sconti, idOrder);
       
       pstmt2.close();
       
   }
   
   /**
    * Su un sottomenu a tendina compaiono le modalità di pagamento disponibili per l'utente attivo
    * @param utenteId id dell'utente
    * @return MOD_PAGAMENTO_ID, TITOLARECARTA_NOME, TITOLARECARTA_COGNOME, TIPOCARTA, NUMEROCARTACREDITO, DATASCADENZA
    * @throws SQLException 
    */
   
   /**
    * Si crea la recensione postata da un utente con un certo ID su un certo libro/venditore
    * Se la variabile booleana libroRec è TRUE, inserisce una recensione di un prodotto, altrimenti di un venditore.
    * @param idUtente utente che recensisce
    * @param commento stringa, testo della recensione
    * @param libroRec true: libro, false: venditore
    * @param target id dell'elemento da recensire, venditore o libro
    * @param voto valore da 1 a 5
    * @throws SQLException 
    */
   public static void creaRecensione(int idUtente, String commento, boolean libroRec, String target, int voto) throws SQLException {
       /*Esempio query :INSERT INTO "GRUPPO26"."RECENSIONI" (UTENTE_ID, COMMENTO, ISBN, VOTO)
       **               VALUES ('423572', 'Un libro meraviglioso, con forti spunti di riflessione. Da consigliare a tutti', '1', ‘5’)
       */
       
       if (voto < 1)
           voto = 1;
       else if (voto > 5)
           voto = 5;
       PreparedStatement pstmt;
       
       if ( libroRec )
           pstmt = conn.prepareStatement("INSERT INTO RECENSIONI (UTENTE_ID, COMMENTO, ISBN, VOTO) VALUES (?, ?, ?, ?)");
       else
           pstmt = conn.prepareStatement("INSERT INTO RECENSIONI (UTENTE_ID, COMMENTO, VENDITORE_ID, VOTO) VALUES (?, ?, ?, ?)");
       
       pstmt.setInt(1, idUtente);
       pstmt.setString(2, commento);
       pstmt.setString(3, target);
       pstmt.setInt(4, voto);
       
       pstmt.executeUpdate();
   }
   
   /**
    * Creazione di un nuovo utente
    * @param nome
    * @param cognome
    * @param mail
    * @param password
    * @param cellulare
    * @throws SQLException 
    */
   public static void creaUtente(String nome, String cognome, String mail, String password, String cellulare) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO UTENTI(NOME, COGNOME, EMAIL, PSW, NUMCELLULARE) VALUES(?, ?, ?, ?, ?)");
       pstmt.setString(1, nome);
       pstmt.setString(2, cognome);
       pstmt.setString(3, mail);
       pstmt.setString(4, password);
       pstmt.setString(5, cellulare);
       
       pstmt.executeUpdate();
   }
   
   /**
    * Aggiornamento dei dati dell'utente
    * @param id dell'utente da modificare, non cambia
    * @param nome
    * @param cognome
    * @param mail
    * @param password
    * @param cellulare
    * @throws SQLException 
    */
   public static void aggiornaUtente(int id, String nome, String cognome, String mail, String password, String cellulare) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE UTENTI SET NOME = ?, COGNOME = ?, EMAIL = ?, PSW = ?, NUMCELLULARE = ? WHERE UTENTE_ID = ?");
       pstmt.setString(1, nome);
       pstmt.setString(2, cognome);
       pstmt.setString(3, mail);
       pstmt.setString(4, password);
       pstmt.setString(5, cellulare);
       pstmt.setInt(6, id);
       
       pstmt.executeUpdate();
   }
   
   /**
    * Visualizza i dati di spedizione dell'utente
    * @param idUtente
    * @return CONTACT_ID, CONTACT_NOME, CONTACT_COGNOME, INDIRIZZOR1, INDIRIZZOR2, CAP, città, Provincia, Paese, Numtelefono
    * @throws SQLException 
    */
   public static ResultSet visualizzaRubricaUtente(int idUtente) throws SQLException {
       PreparedStatement pstmt;
       
       pstmt = conn.prepareStatement("SELECT CONTACT_ID, CONTACT_NOME, CONTACT_COGNOME, INDIRIZZOR1, INDIRIZZOR2, CAP, città, Provincia, Paese, Numtelefono FROM RUBRICA_INDIRIZZI WHERE UTENTE_ID=?",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idUtente);
       
       return pstmt.executeQuery();
       
       /*VISUALIZZA:
        1234567	Roberto	Fasullo     Via Roma, 323       80100   Napoli  NA      Italia  5551029387
        1234568	Marco   Carrozzo    Via Dei Sub, 41     80100	Napoli	NA	Italia	5559876543
       */
   }
   
   /**
    * Preleva e crea un oggetto contatto dal database
    * @param idContatto contatto da creare
    * @return contatto creato
    * @throws SQLException 
    */
   public static Contatto ottieniContatto(int idContatto) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT CONTACT_NOME, CONTACT_COGNOME, INDIRIZZOR1, INDIRIZZOR2, CAP, città, Provincia, Paese, Numtelefono FROM RUBRICA_INDIRIZZI WHERE CONTACT_ID=?",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idContatto);
       ResultSet rsIndirizzi = pstmt.executeQuery();
       rsIndirizzi.first();
       Contatto indirizzo = new Contatto(
                    idContatto,
                    rsIndirizzi.getString(1),
                    rsIndirizzi.getString(2)
            );
            indirizzo.setCap(rsIndirizzi.getString(5));
            indirizzo.setCitta(rsIndirizzi.getString(6));
            indirizzo.setProv(rsIndirizzi.getString(7));
       return indirizzo;
   }
   
   public static void rimuoviIndirizzo(int contactID) throws SQLException {
        PreparedStatement pstmt;
        
        pstmt = conn.prepareStatement("DELETE FROM RUBRICA_INDIRIZZI WHERE CONTACT_ID=?");
        pstmt.setInt(1, contactID);
  
        
        pstmt.executeQuery(); 
    }
   
   /**
    * Inserimento di un contatto indirizzo all'utente
    * @param utenteID utente proprietario dell'indirizzo
    * @param nome nome destinatario
    * @param cognome cognome destinatario
    * @param indirizzo1 indirizzo generico
    * @param indirizzo2 dettagli dell'indirizzo
    * @param cap codice avviamento postale
    * @param citta città del cap
    * @param provincia provincia della città
    * @param paese paese della provincia
    * @param telefono del destinatario
    * @throws SQLException 
    */
   public static void aggiungiIndirizzo(int utenteID, String nome, String cognome, String indirizzo1, String indirizzo2, String cap, String citta, String provincia, String paese, String telefono) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("INSERT INTO RUBRICA_INDIRIZZI(UTENTE_ID, CONTACT_NOME, CONTACT_COGNOME, INDIRIZZOR1, INDIRIZZOR2, CAP, città, Provincia, Paese, Numtelefono) VALUES (?,?,?,?,?,?,?,?,?,?)");
       pstmt.setInt(1, utenteID);
       pstmt.setString(2,nome);
       pstmt.setString(3, cognome);
       pstmt.setString(4, indirizzo1);
       pstmt.setString(5, indirizzo2);
       pstmt.setString(6, cap);
       pstmt.setString(7, citta);
       pstmt.setString(8, provincia);
       pstmt.setString(9, paese);
       pstmt.setString(10, telefono);
       pstmt.execute();
   }
   
   /**
    * Aggiornamento di un indirizzo
    * @param contactID id dell'indirizzo
    * @param nome nome destinatario
    * @param cognome cognome destinatario
    * @param indirizzo1 indirizzo generico
    * @param indirizzo2 dettagli dell'indirizzo
    * @param cap codice avviamento postale
    * @param citta città del cap
    * @param provincia provincia della città
    * @param paese paese della provincia
    * @param telefono del destinatario
    * @throws SQLException 
    */
   public static void aggiornaIndirizzo(int contactID, String nome, String cognome, String indirizzo1, String indirizzo2, String cap, String citta, String provincia, String paese, String telefono) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("UPDATE RUBRICA_INDIRIZZI SET CONTACT_NOME=?, CONTACT_COGNOME=?, INDIRIZZOR1=?, INDIRIZZOR2=?, CAP=?, città=?, Provincia=?, Paese=?, Numtelefono=? WHERE CONTACT_ID=?");
       pstmt.setString(1,nome);
       pstmt.setString(2, cognome);
       pstmt.setString(3, indirizzo1);
       pstmt.setString(4, indirizzo2);
       pstmt.setString(5, cap);
       pstmt.setString(6, citta);
       pstmt.setString(7, provincia);
       pstmt.setString(8, paese);
       pstmt.setString(9, telefono);
       pstmt.setInt(10, contactID);

       pstmt.executeUpdate();
   }
   
   /**
    * Metodo di creazione dell'autore
    * @param nome
    * @param cognome
    * @throws SQLException 
    */
   public static void creaAutore(String nome, String cognome) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO AUTORI(AUT_NOME, AUT_COGNOME) VALUES(?, ?)");
       pstmt.setString(1, nome);
       pstmt.setString(2, cognome);

       
       pstmt.executeUpdate();
   }
   
   /**
    * Metodo di modifica di un autore
    * @param id identificativo
    * @param nome nuovo nome
    * @param cognome nuovo cognome
    * @throws SQLException 
    */
   public static void aggiornaAutore(int id, String nome, String cognome) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE AUTORI SET AUT_NOME = ?, AUT_COGNOME = ? WHERE AUTORE_ID = ?");
       pstmt.setString(1, nome);
       pstmt.setString(2, cognome);
       pstmt.setInt(3, id);
       
       pstmt.executeUpdate();
   }
   
   public static void aggiungiAutoreLibro(int autoreID, String isbn) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO AUTORI_LIB VALUES(?, ?)");
       pstmt.setInt(2, autoreID);
       pstmt.setString(1, isbn);
       
       pstmt.execute();
   }
   
   public static void aggiungiEditoreLibro(int editoreID, String isbn) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO EDITORI_LIB VALUES(?, ?)");
       pstmt.setInt(2, editoreID);
       pstmt.setString(1, isbn);
       
       pstmt.execute();
   }
   
   
   /**
    * Creazione di un libro nell'entità LIBRI
    * @param nomeLibro
    * @param nEdizione
    * @param isbn attenzione, è l'identificativo
    * @param descrizione
    * @param genere
    * @param nPagine
    * @param pesoSped
    * @param dataUscita DD-MES-AAAA
    * @throws SQLException 
    */
   public static void creaLibro(String nomeLibro, int nEdizione, String isbn, String descrizione, String genere, int nPagine, int pesoSped, String dataUscita) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       //Quando si crea un libro bisogna aggiungere: COD_AUTORE, EDITORE, LINGUA, PREZZO
       
       pstmt = conn.prepareStatement("INSERT INTO LIBRI(LIBRO_NOME, EDIZIONE_N, ISBN, DESCRIZIONE, GENERE, PAGINE_N, PESOSPED, DATAUSCITA) VALUES(?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY'))");
       pstmt.setString(1, nomeLibro);
       pstmt.setInt(2, nEdizione);
       pstmt.setString(3, isbn);
       pstmt.setString(4, descrizione);
       pstmt.setString(5, genere);
       pstmt.setInt(6, nPagine);
       pstmt.setInt(7, pesoSped);
       pstmt.setString(8, dataUscita);
       
       pstmt.executeUpdate();
   }
   
   /**
    * Metodo di modifica del libro
    * @param oldISBN identificatore del libro
    * @param nomeLibro
    * @param nEdizione
    * @param isbn nuovo ISBN
    * @param descrizione
    * @param genere
    * @param nPagine
    * @param pesoSped
    * @param dataUscita
    * @throws SQLException 
    */
   public static void aggiornaLibro(String oldISBN, String nomeLibro, int nEdizione, String isbn, String descrizione, String genere, int nPagine, int pesoSped, String dataUscita) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE LIBRI SET LIBRO_NOME = ?, EDIZIONE_N = ?, ISBN = ?, DESCRIZIONE = ?, GENERE = ?, PAGINE_N = ?, PESOSPED = ?, DATAUSCITA = TO_DATE(?, 'DD/MM/YYYY') WHERE ISBN LIKE ?");
       pstmt.setString(1, nomeLibro);
       pstmt.setInt(2, nEdizione);
       pstmt.setString(3, isbn);
       pstmt.setString(4, descrizione);
       pstmt.setString(5, genere);
       pstmt.setInt(6, nPagine);
       pstmt.setInt(7, pesoSped);
       pstmt.setString(8, dataUscita);
       pstmt.setString(9, oldISBN);
       
       pstmt.executeUpdate();
   }
   
   public static void aggiungiListino(String isbn, double prezzoFlessibile, double prezzoRigida, double prezzoKindle) throws SQLException {
       
       if ( prezzoFlessibile > 0) {
           PreparedStatement pstmt;
           pstmt = conn.prepareStatement("INSERT INTO LISTINO_PREZZI VALUES(?,2001,?)");
           pstmt.setString(1,isbn);
           pstmt.setDouble(2,prezzoFlessibile);
           
           pstmt.executeUpdate();
       }
       
       if ( prezzoRigida > 0) {
           PreparedStatement pstmt;
           pstmt = conn.prepareStatement("INSERT INTO LISTINO_PREZZI VALUES(?,2002,?)");
           pstmt.setString(1,isbn);
           pstmt.setDouble(2,prezzoRigida);
           
           pstmt.executeUpdate();
       }
       
       if ( prezzoKindle > 0) {
           PreparedStatement pstmt;
           pstmt = conn.prepareStatement("INSERT INTO LISTINO_PREZZI VALUES(?,2003,?)");
           pstmt.setString(1,isbn);
           pstmt.setDouble(2,prezzoKindle);
           
           pstmt.executeUpdate();
       }
   }
   
   public static void aggiornaListino(String isbn, double prezzoRigida, double prezzoFlessibile, double prezzoKindle) throws SQLException {
        
       if ( prezzoFlessibile > 0) {
           PreparedStatement pstmt;
           pstmt = conn.prepareStatement("UPDATE LISTINO_PREZZI SET PREZZOLISTINO=? WHERE ISBN LIKE ? AND FORMATO_ID=2001");
           pstmt.setDouble(1,prezzoFlessibile);
           pstmt.setString(2,isbn);
           
           pstmt.executeUpdate();
       }
       
       if ( prezzoRigida > 0) {
           PreparedStatement pstmt;
           pstmt = conn.prepareStatement("UPDATE LISTINO_PREZZI SET PREZZOLISTINO=? WHERE ISBN LIKE ? AND FORMATO_ID=2002");
           pstmt.setDouble(1,prezzoRigida);
           pstmt.setString(2,isbn);
           
           pstmt.executeUpdate();
       }
       
       if ( prezzoKindle > 0) {
           PreparedStatement pstmt;
           pstmt = conn.prepareStatement("UPDATE LISTINO_PREZZI SET PREZZOLISTINO=? WHERE ISBN LIKE ? AND FORMATO_ID=2003");
           pstmt.setDouble(1,prezzoKindle);
           pstmt.setString(2,isbn);
           
           pstmt.executeUpdate();
       }
   }
   
   public static void creaEditore(String nomeEditore) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO EDITORI(EDI_NOME) VALUES(?)");
       pstmt.setString(1, nomeEditore);

       
       pstmt.executeUpdate();
   }
   
   public static void aggiornaEditore(int idEditore, String nomeEditore) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE EDITORI SET EDI_NOME = ? WHERE EDI_ID = ?");
       pstmt.setString(1, nomeEditore);
       pstmt.setInt(2, idEditore);
       
       pstmt.executeUpdate();
   }
   
   public static void creaCorriere(String nomeCorriere) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO CORRIERI(CORRIERE_NOME) VALUES(?)");
       pstmt.setString(1, nomeCorriere);

       
       pstmt.executeUpdate();
   }
   
   public static void aggiornaCorriere(int idCorriere, String nomeCorriere) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE CORRIERI SET CORRIERE_NOME = ? WHERE CORRIERE_ID = ?");
       pstmt.setString(1, nomeCorriere);
       pstmt.setInt(2, idCorriere);
       
       pstmt.executeUpdate();
   }
   /**
    * Applica le modalità di spedizione che un corriere ha a disposizione
    * @param idCorriere identificatore del corriere
    * @param d1 se dispone della spedizione in un giorno
    * @param d2 se dispone della spedizione in 2-3 giorni
    * @param d3 se dispone della spedizione in 3-5 giorni
    * @throws SQLException 
    */
   public static void creaModSpedizione(int idCorriere, boolean d1, boolean d2, boolean d3) throws SQLException
   {
       if(d1)   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO MOD_SPEDIZIONE VALUES(?, 1_Giorno, 8)");
       pstmt.setInt(1, idCorriere);

       pstmt.executeUpdate();
       }
       
       if(d2)   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO MOD_SPEDIZIONE VALUES(?, 2-3_Giorni, 4)");
       pstmt.setInt(1, idCorriere);

       pstmt.executeUpdate();
       }
       
       if(d3)   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO MOD_SPEDIZIONE VALUES(?, 3-5_Giorni, 0)");
       pstmt.setInt(1, idCorriere);

       pstmt.executeUpdate();
       }
   }
   /**
    * Modifica le modalità di spedizione che un corriere ha a disposizione
    * @param idCorriere identificatore del corriere
    * @param d1 se dispone della spedizione in un giorno
    * @param d2 se dispone della spedizione in 2-3 giorni
    * @param d3 se dispone della spedizione in 3-5 giorni
    * @throws SQLException 
    */
      public static void aggiornaModSpedizione(int idCorriere, boolean d1, boolean d2, boolean d3) throws SQLException
   {
       if(d1)   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE MOD_SPEDIZIONE SET MODSPED=1_Giorno, COSTOSPED=8 WHERE CORRIERE_ID=?");
       pstmt.setInt(1, idCorriere);

       pstmt.executeUpdate();
       }
       
       if(d2)   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE MOD_SPEDIZIONE SET MODSPED=2-3_Giorni, COSTOSPED=4 WHERE CORRIERE_ID=?");
       pstmt.setInt(1, idCorriere);

       pstmt.executeUpdate();
       }
       
       if(d3)   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE MOD_SPEDIZIONE SET MODSPED=3-5_Giorni, COSTOSPED=0 WHERE CORRIERE_ID=?");
       pstmt.setInt(1, idCorriere);

       pstmt.executeUpdate();
       }
   }
   
      public static void creaVenditore(String nomeVenditore) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO VENDITORI(VENDITORE_NOME) VALUES(?)");
       pstmt.setString(1, nomeVenditore);

       
       pstmt.executeUpdate();
   }
   
   public static void aggiornaVenditore(int idVenditore, String nomeVenditore) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE VENDITORI SET VENDITORE_NOME = ? WHERE VENDITORE_ID = ?");
       pstmt.setString(1, nomeVenditore);
       pstmt.setInt(2, idVenditore);
       
       pstmt.executeUpdate();
   }
   
   public static ResultSet visualizzaLibriAutore(int idAutore) throws SQLException {
       /*Metodo che gestisce l'Autore e visualizza 
       **tutti i libri di quell'autore
       */
       
       PreparedStatement pstmt;
       
       pstmt = conn.prepareStatement("SELECT LIBRI.LIBRO_NOME FROM AUTORI_LIB NATURAL JOIN LIBRI WHERE AUTORE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idAutore);
       
       return pstmt.executeQuery();
       /*RISULTATO QUERY: LIBRO_NOME
                          Soffocare
       */
    }
   
   public static ResultSet cercaLibriAutore(int idAutore, String chiave) throws SQLException {
       PreparedStatement pstmt;
       String query = "SELECT LIBRI.LIBRO_NOME FROM AUTORI_LIB NATURAL JOIN LIBRI WHERE AUTORE_ID = ? AND UPPER(LIBRI.LIBRO_NOME) LIKE UPPER(?)";
       pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idAutore);
       pstmt.setString(2, "%"+chiave+"%");
       
       return pstmt.executeQuery();
       /*RISULTATO QUERY: LIBRO_NOME
                          Fight Club
       */
   }
   
   public static ResultSet visualizzaLibriEditore(int idEditore) throws SQLException {
        /*Metodo che gestisce l'Editore e visualizza 
        **tutti i libri di quell'editore
        */
       
       PreparedStatement pstmt;
       
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME FROM LIBRI NATURAL JOIN EDITORI_LIB WHERE EDI_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idEditore);
       
       return pstmt.executeQuery();
    }
   
   /**
    * Cerca il libro di un editore dalla query
    * @param idEditore editore
    * @param query titolo del libro
    * @return LIBRO_NOME
    * @throws SQLException 
    */
   public static ResultSet cercaLibroEditore(int idEditore, String query) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME FROM LIBRI NATURAL JOIN EDITORI_LIB WHERE EDI_ID = ? AND UPPER(LIBRO_NOME) LIKE UPPER(?)",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idEditore);
       pstmt.setString(2,"%"+query+"%");
       
       return pstmt.executeQuery();
   }
   
   /**
    * Visualizza gli autori di un libro
    * @param isbn del libro di cui visualizzare gli autori
    * @return ResultSet con nome e cognome degli autori
    * @throws SQLException 
    */
   public static ResultSet visualizzaAutoriLibro(String isbn) throws SQLException {
       PreparedStatement pstmt;
       
       pstmt = conn.prepareStatement("SELECT AUTORI.AUTORE_ID, AUT_NOME, AUT_COGNOME FROM AUTORI INNER JOIN AUTORI_LIB ON AUTORI.AUTORE_ID=AUTORI_LIB.AUTORE_ID WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, isbn);
       
       return pstmt.executeQuery();
   }
   
   /**
    * Visualizza gli editori di un libro
    * @param isbn del libro di cui visualizzare gli editori
    * @return ResultSet con nome dell'editore
    * @throws SQLException 
    */
   public static ResultSet visualizzaEditoriLibro(String isbn) throws SQLException {
       PreparedStatement pstmt;
       
       pstmt = conn.prepareStatement("SELECT EDITORI_LIB.EDI_ID, EDI_NOME FROM EDITORI INNER JOIN EDITORI_LIB ON EDITORI.EDI_ID=EDITORI_LIB.EDI_ID WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, isbn);
       
       return pstmt.executeQuery();
   }
   
   public static void rimuoviAutoreLibro(int autoreID, String isbn) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("DELETE FROM AUTORI_LIB WHERE AUTORE_ID=? AND ISBN LIKE ?");
       pstmt.setInt(1, autoreID);
       pstmt.setString(2, isbn);
        
        pstmt.executeQuery(); 
   }
   
   public static void rimuoviEditoreLibro(int editoreID, String isbn) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("DELETE FROM EDITORI_LIB WHERE EDI_ID=? AND ISBN LIKE ?");
       pstmt.setInt(1, editoreID);
       pstmt.setString(2, isbn);
        
        pstmt.executeQuery(); 
   }
   
   /**
    * Lista dei libri dettagliata che il venditore ha a disposizione
    * @param venditoreID
    * @return LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, PEZZIDISPONIBILI, PREZZOVENDITA
    * @throws SQLException 
    */
   public static ResultSet visualizzaMagazzino (int venditoreID) throws SQLException {       
       //Esempio: VENDITORE_ID = 6317;
       /*RISULTATO QUERY:
                        LIBRO_NOME                      FORMATO_NOME            TIPOCONDIZIONE  PEZZIDISPONIBILI    PREZZOVENDITA
                        I racconti di Nené              Copertina Flessibile	Ricondizionato	2                   2,99
                        I racconti di Nené              Copertina Flessibile	Nuovo           10                  4,99
                        Avatar                          Copertina Flessibile	Nuovo           0                   8,99
                        Soffocare                       Copertina Flessibile	Usato           1                   5,99
                        Invito alla Biologia            Copertina Flessibile	Usato           1                   8,47
                        Hunger Games                    Copertina Flessibile	Nuovo           20                  12,99
                        La Danza delle Stelle           Copertina Rigida	Nuovo           10                  9,99
                        La Solitudine dei Numeri Primi	Copertina Rigida	Nuovo           10                  11,99
                        Fight Club                      Copertina Rigida	Nuovo           10	            7,4
       
       */
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT DISTINCT LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, PEZZIDISPONIBILI, PREZZOVENDITA FROM MAGAZZINO_LIBRI NATURAL JOIN VIEW_INFO WHERE VENDITORE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       
       pstmt.setInt(1, venditoreID);
       
       return pstmt.executeQuery();
   }

   /**
    * Ricerca di un libro in un magazzino di un venditore
    * serve per la ricerca su un nome
    * @param idVenditore
    * @param query parola da cercare
    * @return VIEWMAGAZZINO
    * @throws SQLException 
    */
   public static ResultSet visualizzaMagazzino(int idVenditore, String query) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT DISTINCT LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, PEZZIDISPONIBILI, PREZZOVENDITA FROM MAGAZZINO_LIBRI NATURAL JOIN VIEW_INFO WHERE VENDITORE_ID = ? AND UPPER(LIBRO_NOME) LIKE UPPER(?)",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idVenditore);
       pstmt.setString(2, "%"+query+"%");
       
       return pstmt.executeQuery();
       /*RISULTATO QUERY: LIBRO_NOME    AUT_NOME    AUT_COGNOME     EDI_NOME    ISBN            VENDITORE_ID
                          Fight Club	Chuck       Palahniuk       Mondadori	9788804508359	6318
       */
   }
   
   /**
    * Visualizza tutti i libri disponibili per l'acquisto
    * @return result set LIBRO_NOME, ISBN, FORMATO_NOME, PREZZOLISTINO
    * @throws SQLException 
    */
   public static ResultSet visualizzaListinoLibri() throws SQLException {
        //Lista completa di tutti i libri presenti nell'archivio completo (non nei magazzini dei venditori)
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME, ISBN, FORMATO_NOME, PREZZOLISTINO FROM LIBRI NATURAL JOIN LISTINO_PREZZI NATURAL JOIN IMPOSTAZIONI",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       
       return pstmt.executeQuery();
       
   }
   
   /**
    * Lista completa di tutti i libri presenti nell'archivio completo (non nei magazzini dei venditori)
    * @param formato
    * @return
    * @throws SQLException 
    */
   public static ResultSet visualizzaListinoLibri(int formato) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME, ISBN, PREZZOLISTINO FROM LIBRI NATURAL JOIN LISTINO_PREZZI WHERE FORMATO_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, formato);
       return pstmt.executeQuery();
       
   }
   
   /**
    * Visualizza il listino dei libri secondo una ricerca avanzata dall'utente
    * @param query          parola cercata (% vengono messe automaticamente)
    * @param formato        2001, 2002 o 2003
    * @param prezzoMin      prezzo minimo richiesto (DOUBLE)
    * @param prezzoMax      limite massimo del prezzo (DOUBLE)
    * @return LIBRO_NOME, ISBN, FORMATO_NOME, PREZZOLISTINO
    * @throws SQLException 
    */
   public static ResultSet visualizzaListinoLibri(String query, int formato, double prezzoMin, double prezzoMax) throws SQLException {
       //Con una stringa possiamo cercare il nome di un libro presente nell'archivio
       boolean cercaParola = false;
       boolean cercaFormato = false;
       boolean cercaPrezzoMin = false;
       boolean cercaPrezzoMax = false;
       int controllo = 0;
       String ricerca = "SELECT LIBRO_NOME, ISBN, FORMATO_NOME, PREZZOLISTINO FROM LIBRI NATURAL JOIN LISTINO_PREZZI NATURAL JOIN IMPOSTAZIONI WHERE ";
       if (!"".equals(query)) {
           ricerca += "LIBRI.LIBRO_NOME LIKE ?";
           cercaParola = true;
       } else
           ricerca += "1=1";
       if (formato > 0) {
           cercaFormato = true;
           ricerca += " AND FORMATO_ID = ?";
       }
       if (prezzoMin > 0) {
           cercaPrezzoMin = true;
           ricerca += " AND PREZZOLISTINO >= ?";
       }
       if (prezzoMax > 0) {
           cercaPrezzoMax = true;
           ricerca += " AND PREZZOLISTINO <= ?";
       }
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement(ricerca,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       if (cercaParola == true) {
           controllo++;
           pstmt.setString(controllo, "%"+query+"%");
       }
       if (cercaFormato == true) {
           controllo++;
           pstmt.setInt(controllo, formato);
       }    
       if (cercaPrezzoMin == true) {
           controllo++;
           pstmt.setDouble(controllo, prezzoMin);
       }
       if (cercaPrezzoMax == true) {
           controllo++;
           pstmt.setDouble(controllo, prezzoMax);
       }
       return pstmt.executeQuery();
   }
   
   public static ResultSet visualizzaRecensioniLibro (String isbn) throws SQLException {
       
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT NOME, COGNOME, COMMENTO, VOTO FROM RECENSIONI INNER JOIN UTENTI ON UTENTI.UTENTE_ID=RECENSIONI.UTENTE_ID WHERE ISBN=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, isbn);
       return pstmt.executeQuery();
       
   }
   
   public static ResultSet visualizzaRecensioniVenditori (int venditoreID) throws SQLException {
       
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT NOME, COGNOME, COMMENTO, VOTO FROM RECENSIONI INNER JOIN UTENTI ON UTENTI.UTENTE_ID=RECENSIONI.UTENTE_ID WHERE VENDITORE_ID=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, venditoreID);
       return pstmt.executeQuery();
       
   }
   
   
   /**
    * Compaiono le informazioni dettagliate dei libri presenti nell'archivio
    * @param isbn
    * @return ISBN, NOME_LIBRO, PROD_ID, VENDITORE_ID, VENDITORE_NOME, AUTORE_ID, AUT_NOME, AUT_COGNOME, EDI_ID, EDI_NOME, FORMATO_ID, FORMATO NOME, TIPOCONDIZIONE, DESCRIZIONE, GENERE, PAGINE_N, PESOSPED, DATAUSCITA, 
    * @throws SQLException 
    */
   public static ResultSet visualizzaInfoLibro (String isbn) throws SQLException {
        //Esempio: ISBN = 9788804632238;
       /*RISULTATO QUERY:
            ISBN                NOME_LIBRO              PROD_ID     VENDITORE_ID  VENDITORE_NOME    AUTORE_DI   AUT_NOME    AUT_COGNOME     EDI_ID      EDI_NOME        FORMATO_ID  FORMATO NOME    TIPOCONDIZIONE      DESCRIZIONE                             GENERE      PAGINE_N    PESOSPED    DATAUSCITA
            5558845230455	La Danza delle Stelle	28	    6294	  Amazon.it         1006	Giuseppe    Senese          2779	Bompiani	2003        Kindle          Nuovo		Libro inedito che forse verrà scritto.	Fantasy     328         200         10-FEB-13
       */
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT * FROM VIEW_INFO NATURAL JOIN LIBRI WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, isbn);
       
       return pstmt.executeQuery();
       
   }
   
   /**
    * In questo campo compaiono i venditori che hanno a disposizione il libro scelto, qualsiasi formato abbiano.
    * VENDITORE_ID, VENDITORE_NOME, PREZZOVENDITA_MINIMO
    * @param isbn
    * @return ResultSet di informazioni
    * @throws SQLException 
    */
   public static ResultSet visualizzaVenditoriLibro(String isbn) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT VENDITORE_ID, VENDITORE_NOME, PREZZOVENDITA_MINIMO FROM VIEW_LIBRIDISPONIBILI NATURAL JOIN VENDITORI WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, isbn);
       return pstmt.executeQuery();
   }
   
   /**
    * In questo campo compaiono i formati che il venditore ha a disposizione per il libro in questione
    * FORMATO_ID, FORMATO_NOME, TIPOCONDIZIONE, PEZZIDISPONIBILI, PREZZOVENDITA
    * @param isbn libro
    * @param venditoreID id del venditore
    * @return ResultSet elenco dei formati con prezzo e tutto
    * @throws SQLException 
    */
   public static ResultSet visualizzaFormatoLibroVenditore(String isbn, int venditoreID) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT DISTINCT PROD_ID, FORMATO_ID, FORMATO_NOME, TIPOCONDIZIONE, PEZZIDISPONIBILI, PREZZOVENDITA, PERCSCONTO FROM MAGAZZINO_LIBRI NATURAL JOIN VIEW_INFO NATURAL JOIN VIEW_PERCSCONTO WHERE ISBN = ? AND VENDITORE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, isbn);
       pstmt.setInt(2, venditoreID);
       return pstmt.executeQuery();
   }
   
      /**
    * In questo campo compaiono i voti medi dei libri presenti nell'archivio
    * ISBN, VOTO_MEDIO
    * @param isbn libro
    * @return double valore del voto
    * @throws SQLException 
    */
   public static double visualizzaVotomedioLibro(String isbn) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT VOTO_MEDIO FROM VOTO_LIBRI WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, isbn);
       ResultSet rs = pstmt.executeQuery();
       rs.first();
       return rs.getDouble(1);
   }
   
     /**
    * In questo campo compaiono i voti medi dei venditori
    * ISBN, VOTO_MEDIO
    * @param venditoreId venditore
    * @return double valore del voto
    * @throws SQLException 
    */
   public static double visualizzaVotomedioVenditore(String venditoreId) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT VOTO_MEDIO FROM VOTO_VENDITORI WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, venditoreId);

       return pstmt.executeQuery().getDouble(2);
   }
   
   
   /**
    * Viene effettuato l'inserimento nel carrello di un articolo
    * @param utenteID id dell'utente
    * @param prodID id del prodotto
    * @param quantita quantità richiesta
    * @throws SQLException 
    */

   public static void inserisciArticoloCarrello(int utenteID, int prodID, int quantita) throws SQLException {
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement("INSERT INTO COMPARTICOLI(UTENTE_ID, PROD_ID, Quantità) VALUES(?,?,?)");
        pstmt.setInt(1, utenteID);
        pstmt.setInt(2, prodID);
        pstmt.setInt(3, quantita);   
        
        pstmt.executeQuery(); 
 }
   
   /**
    * Viene modificata la quantità di un articolo in un carrello
    * @param utenteID id dell'utente
    * @param prodID id del prodotto
    * @param quantita quantità da modificare
    * @throws SQLException 
    */

   public static void modificaQuantitaArticolo(int utenteID, int prodID, int quantita) throws SQLException {
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement("UPDATE COMPARTICOLI SET Quantità = ? WHERE UTENTE_ID = ? AND PROD_ID = ?");
        pstmt.setInt(1, quantita);  
        pstmt.setInt(2, utenteID);
        pstmt.setInt(3, prodID);
         
        pstmt.executeQuery(); 
 }
   
   /**
    * Creazione della lista dei desideri
    * @param utenteID intero, id del proprietario della lista
    * @param nomeLista stringa, nome della lista
    * @param privacy 0: privata, 1: condivisa, 2: pubblica
    * @throws SQLException 
    */
   public static void creaListaDesideri(int utenteID, String nomeLista, int privacy) throws SQLException {
       String nomePrivacy;
       switch (privacy) {
           case 0: nomePrivacy = "Privata";
           break;
           case 1: nomePrivacy = "Condivisa";
           break;
           case 2: nomePrivacy = "Pubblica";
           break;
           default: nomePrivacy = "Privata";
       }
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("INSERT INTO LISTA_DESIDERI (UTENTE_ID, LISTA_NOME, LISTA_PRIVACY) VALUES(?,?,?)");
       pstmt.setInt(1, utenteID);
       pstmt.setString(2, nomeLista);
       pstmt.setString(3, nomePrivacy);
       
       pstmt.executeQuery();
   }
   
   public static int ultimaListaDesideri() throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LISTA_ID FROM LISTA_DESIDERI",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       ResultSet rs = pstmt.executeQuery();
       rs.last();
       return rs.getInt(1);
   }
   
   /**
    * Modifica della privacy della lista dei desideri
    * @param listaID id lista da modificare
    * @param privacy 0: privata, 1: condivisa, 2: pubblica
    * @throws SQLException 
    */
   public static void modificaListaDesideri(int listaID, int privacy) throws SQLException {
       
       PreparedStatement pstmt;
       String nomePrivacy;
       switch (privacy) {
           case 0: nomePrivacy = "Privata";
           break;
           case 1: nomePrivacy = "Condivisa";
           break;
           case 2: nomePrivacy = "Pubblica";
           break;
           default: nomePrivacy = "Privata";
       }
       pstmt = conn.prepareStatement("UPDATE LISTA_DESIDERI SET LISTA_PRIVACY=? WHERE LISTA_ID = ?");
       pstmt.setString(1, nomePrivacy);
       pstmt.setInt(2, listaID);
       
       pstmt.executeUpdate();
   }
   
   /**
    * Eliminazione della lista dei desideri
    * @param listaID
    * @throws SQLException 
    */
   public static void eliminaListaDesideri(int listaID) throws SQLException {
       
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("DELETE FROM LISTA_DESIDERI WHERE LISTA_ID = ?");
       pstmt.setInt(1, listaID);
       
       pstmt.execute();
   }   
   
   /**
    * Rinomina della lista dei desideri
    * @param listaID
    * @param nuovoNome
    * @throws SQLException 
    */
   public static void rinominaListaDesideri(int listaID, String nuovoNome) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("UPDATE LISTA_DESIDERI SET LISTA_NOME = ? WHERE LISTA_ID = ?");
       pstmt.setString(1, nuovoNome);
       pstmt.setInt(2, listaID);
       
       pstmt.executeQuery();
    }
   
   /**
    * Inserimento dell'articolo nella lista desideri
    * @param listaID intero, id della lista desideri
    * @param prodID id del prodotto
    * @param dataPrezzo stringa, in formato dd-MES-aaaa
    * @throws SQLException 
    */
   public static void inserisciArticoloLista(int listaID, int prodID, double dataPrezzo) throws SQLException {
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement("INSERT INTO COMPLISTA_DESIDERI(LISTA_ID, PROD_ID, DATAAGGIUNTA_PREZZO) VALUES(?,?,?)",
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
        pstmt.setInt(1, listaID);
        pstmt.setInt(2, prodID);
        pstmt.setDouble(3, dataPrezzo);   
        
        pstmt.executeQuery(); 
    }
   
/**
 * Rimozione di un elemento nella lista desideri
 * @param listaID intero, id della lista
 * @param prodID ID del prodotto
 * @throws SQLException 
 */
   public static void rimuoviArticoloLista(int listaID, int prodID) throws SQLException {
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement("DELETE FROM COMPLISTA_DESIDERI WHERE LISTA_ID=? AND PROD_ID=?");
        pstmt.setInt(1, listaID);
        pstmt.setInt(2, prodID);
        
        pstmt.executeQuery(); 
    }
   
   
   /**
    * Inserisce in un determinato venditore un libro selezionato precedentemente con determinate informazioni
    * @param venditoreID venditore
    * @param isbn libro
    * @param formatoID formato (2001, 2002 o 2003, rispettivamente copertina flessibile, copertina rigida o kindle)
    * @param tipoCondizione "Nuovo", "Usato" o "Ricondizionato"
    * @param pezziDisp null o -1 se kindle
    * @param prezzo dev'essere minore del listino (non è obbligatorio, ma una prassi)
    * @throws SQLException 
    */
    public static void inserisciLibroMagazzino(int venditoreID, String isbn, int formatoID, String tipoCondizione, int pezziDisp, double prezzo) throws SQLException {
        PreparedStatement pstmt;
 
        pstmt = conn.prepareStatement("INSERT INTO MAGAZZINO_LIBRI(VENDITORE_ID, ISBN, FORMATO_ID, TIPOCONDIZIONE, PEZZIDISPONIBILI, PREZZOVENDITA) VALUES(?, ?, ?, ?, ?, ?)");
        pstmt.setInt(1, venditoreID);
        pstmt.setString(2, isbn);
        pstmt.setInt(3, formatoID);
        pstmt.setString(4, tipoCondizione);
        pstmt.setInt(5, pezziDisp);
        pstmt.setDouble(6, prezzo);
        // try { 
            pstmt.executeUpdate();
        // } catch (SQLException ex) {
            // if (ex.getErrorCode() == 9000)
            // aggiungiLibriMagazzino(venditoreID, isbn, formatoID, tipoCondizione, pezziDisp, prezzo)
        // } 
 }
   
   /**
    * Query (ridondante ma) utile per controllare la disponibilità di un libro nei magazzini
    * @return VIEW_LIBRIDISPONIBILI
    * @throws SQLException 
    */
   public static ResultSet visualizzaLibriDisponibili() throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT * FROM VIEW_LIBRIDISPONIBILI",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       return pstmt.executeQuery();
   }
   

      /**
       * In questo campo compaiono le liste dei desideri di un singolo utente
       * LISTA_ID, LISTA_NOME, LISTA_PRIVACY
       * @param utenteId intero, identificatore dell'utente
       * @return resultset delle liste desideri dell'utente LISTA_ID, LISTA_NOME, LISTA_PRIVACY
       * @throws SQLException 
       */
   public static ResultSet visualizzaListeUtente(int utenteId) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LISTA_ID, LISTA_NOME, LISTA_PRIVACY FROM LISTA_DESIDERI WHERE UTENTE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, utenteId);
       ResultSet rs = pstmt.executeQuery();
       rs.first();
       return rs;
   }
   
   /**
    * In questo campo compaiono i libri di una lista di un utente
    * @param listaID id della ista
    * @return PROD_ID, ISBN, LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, VENDITORE_NOME, PREZZOVENDITA
    * @throws SQLException 
    */
   public static ResultSet visualizzaArticoliListaUtente(int listaID) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT DISTINCT PROD_ID, ISBN, LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, VENDITORE_NOME, PREZZOVENDITA FROM COMPLISTA_DESIDERI NATURAL JOIN VIEW_INFO NATURAL JOIN MAGAZZINO_LIBRI WHERE LISTA_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, listaID);
       
       return pstmt.executeQuery();
   }
   
   /**
    * Inserendo un result set, restituisce il numero di righe
    * @param resultSet
    * @return intero, numero di righe
    */
   public static int contaRigheResultSet(ResultSet resultSet) {
       int size;
    try {
        resultSet.last();
        size = resultSet.getRow();
        resultSet.beforeFirst();
    }
    catch(Exception ex) {
        return 0;
    }
    return size;
   }
}

