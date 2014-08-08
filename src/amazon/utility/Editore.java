/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon.utility;

/**
 * Classe autori per vari utilizzi
 * @author Francesco
 */
public class Editore {
    public Editore(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    private int id;
    private String nome;
    
    /**
     * Metodo utilizzato nella combobox
     * @return nome dell'editore 
     */
    @Override
    public String toString() {
        return nome;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    

}
