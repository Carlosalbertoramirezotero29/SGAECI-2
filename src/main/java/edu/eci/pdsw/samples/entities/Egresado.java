/*
 * To change this licensfae header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.entities;

/**
 *
 * @author 2106796
 */
public class Egresado extends Persona{
    private String semestreGrado,correoPersonal,labora,cargo;
    private Egresado_Empresa emp;
    
    public Egresado(int documentoID, int telefono1, int telefono2,String tipoDocumentoID, String nombre, String direccion, String correo, String genero, String semestreGrado, String correoPersonal, String cargo, String labora,Egresado_Empresa egresadoEmpresa) {
        super(documentoID,telefono1,telefono2,tipoDocumentoID,nombre,direccion,correo,genero);
        this.semestreGrado = semestreGrado;
        this.correoPersonal = correoPersonal;
        this.cargo=cargo;
        this.emp=egresadoEmpresa;
        this.labora=labora;
    }

    public Egresado() {
    }

    public String getSemestreGrado() {
        return semestreGrado;
    }

    public void setSemestreGrado(String semestreGrado) {
        this.semestreGrado = semestreGrado;
    }

    public String getCorreoPersonal() {
        return correoPersonal;
    }

    public void setCorreoPersonal(String correoPersonal) {
        this.correoPersonal = correoPersonal;
    }

    public String getLabora() {
        return labora;
    }

    public void setLabora(String labora) {
        this.labora = labora;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Egresado_Empresa getEmp() {
        return emp;
    }

    public void setEmp(Egresado_Empresa emp) {
        this.emp = emp;
    }

    
    
}
