/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.entities;

/**
 *
 * @author Martin
 */
public class Egresado_Empresa {

    private int telempre;
    private String nombreempre,dirempre;
    
    public Egresado_Empresa( String nombreempre, String dirempre, int telempre) {
        this.dirempre=dirempre;
        this.nombreempre=nombreempre;
        this.telempre=telempre;
    }

    public Egresado_Empresa() {
    }

    public int getTelempre() {
        return telempre;
    }

    public void setTelempre(int telempre) {
        this.telempre = telempre;
    }

    public String getNombreempre() {
        return nombreempre;
    }

    public void setNombreempre(String nombreempre) {
        this.nombreempre = nombreempre;
    }

    public String getDirempre() {
        return dirempre;
    }

    public void setDirempre(String dirempre) {
        this.dirempre = dirempre;
    }

    
}
