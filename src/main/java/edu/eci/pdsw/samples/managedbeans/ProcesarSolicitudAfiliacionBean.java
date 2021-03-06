/*
 * Copyright (C) 2016 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.pdsw.samples.managedbeans;

import edu.eci.pdsw.samples.Security.SHA1;
import edu.eci.pdsw.samples.entities.Egresado;
import edu.eci.pdsw.samples.entities.Estudiante;
import edu.eci.pdsw.samples.entities.Persona;
import edu.eci.pdsw.samples.entities.SolicitudAfiliacion;
import edu.eci.pdsw.samples.services.ExcepcionServiciosSAGECI;
import edu.eci.pdsw.samples.services.ServiciosSAGECI;
import edu.eci.pdsw.samples.javamail.core.*;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.mail.MessagingException;
import org.primefaces.context.RequestContext;
import java.lang.Exception;

/**
 *
 * @author hcadavid
 */
@ManagedBean(name = "ProcesarSolicitudAfiliacion")
@SessionScoped
public class ProcesarSolicitudAfiliacionBean implements Serializable {

    ServiciosSAGECI SAGECI = ServiciosSAGECI.getInstance();
    SolicitudAfiliacion solicitudSelection;
    String Comentario = "";
    EmailSender sender = new SimpleEmailSender(new EmailConfiguration());
    Email email = null;
    Persona e;
    private boolean b = true;
    private SHA1 sh;
    final String from = "5d8dd682c0-c92f3e@inbox.mailtrap.io";
    final String subjectAprobado = "Solicitud de Ingreso AECI: Aprobada";
    final String messageRechazado = "Su solicitud ha sido Rechazada por lo siguiente: " + Comentario;

    public ProcesarSolicitudAfiliacionBean() {

    }

    public void showMessage(boolean m, String tipo) {
        FacesMessage message;
        if (tipo.equals("m")) {
            if (m) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "La Solicitud fue aceptada correctamente.");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Incorrecto", "Hubo un error y no se realizo ningun cambio.");
            }
        }else{
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Incorrecto", "Si desea recharla debe ingresar la razón.");
        }

        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public List<SolicitudAfiliacion> getSolicitudes() throws ExcepcionServiciosSAGECI {
        List<SolicitudAfiliacion> temp = SAGECI.consultarSolicitudAfiliaciones();
        for (SolicitudAfiliacion s : temp) {
            if (s.getE1().getSemestreGrado() == null) {
                s.setTipoSol("Estudiante");
                s.setE1(null);
            } else {
                s.setTipoSol("Egresado");
                s.setE2(null);
            }
        }
        return temp;
    }

    public Persona getE() {
        return e;
    }

    public void setE(Persona e) {
        this.e = e;
    }

    public ServiciosSAGECI getServicios() {
        return SAGECI;
    }

    public void setServicios(ServiciosSAGECI servicios) {
        this.SAGECI = servicios;
    }

    public SolicitudAfiliacion getSolicitudSelection() {
        return this.solicitudSelection;
    }

    public void setSolicitudSelection(SolicitudAfiliacion solicitudSelection) {
        this.solicitudSelection = solicitudSelection;
        if (solicitudSelection.getE1() == null) {
            this.solicitudSelection.setE1(null);
            this.e = (Persona) solicitudSelection.getE2();
        } else {
            this.solicitudSelection.setE2(null);
            this.e = (Persona) solicitudSelection.getE1();
        }
    }

    public void aceptarSolicitudAfiliacion(ActionEvent actionEvent) throws ExcepcionServiciosSAGECI {

        try {
            Egresado e1 = solicitudSelection.getE1();
            Estudiante e2 = solicitudSelection.getE2();
            String messageAprobado = "Su solicitud ha sido Aprobada: " + Comentario;
            solicitudSelection.setEstadoSolicitud("ACEPTADA");
            solicitudSelection.setComentario(Comentario);
            SAGECI.actualizarSolicitudAfliliacion(solicitudSelection);
            String shacontrasena;
            if (e1 == null) {
                String toEstudiante = e2.getCorreo_Personal();
                shacontrasena = sh.generateHash(Integer.toString(e2.getDocumentoID()));
                email = new SimpleEmail(from, toEstudiante, subjectAprobado, messageAprobado + ", Su Usuario y la contraseña es su Documento de identidad");
                SAGECI.agregarRolPersona(e2.getDocumentoID(), 3, shacontrasena);
                sender.send(email);
            } else {
                String toEgresado = e1.getCorreo_Personal();
                shacontrasena = sh.generateHash(Integer.toString(e1.getDocumentoID()));
                email = new SimpleEmail(from, toEgresado, subjectAprobado, messageAprobado + ", Su Usuario y la contraseña es su Documento de identidad");
                SAGECI.agregarRolPersona(e1.getDocumentoID(), 2, shacontrasena);
                sender.send(email);
            }
        } catch (Exception e) {
            showMessage(false,"m");
            b = false;
        }
        if (b) {
            showMessage(true,"m");
        }
        Comentario="";
    }

    public void rechazarSolicitudAfiliacion(ActionEvent actionEvent) throws ExcepcionServiciosSAGECI {
        if (!Comentario.equals("")) {
            try {
                Egresado e1 = solicitudSelection.getE1();
                Estudiante e2 = solicitudSelection.getE2();
                String messageAprobado = "Su solicitud ha sido Rechazada: " + Comentario;
                String subjectRechazado = "Solicitud de Ingreso AECI: Rechazada";
                solicitudSelection.setEstadoSolicitud("RECHAZADA");
                solicitudSelection.setComentario(Comentario);
                SAGECI.actualizarSolicitudAfliliacion(solicitudSelection);
                if (e1 == null) {
                    String toEstudiante = e2.getCorreo_Personal();
                    email = new SimpleEmail(from, toEstudiante, subjectRechazado, messageAprobado);
                    sender.send(email);
                } else {
                    String toEgresado = e1.getCorreo_Personal();
                    email = new SimpleEmail(from, toEgresado, subjectRechazado, messageAprobado);
                    sender.send(email);
                }
            } catch (Exception e) {
                showMessage(false,"m");
                b = false;
            }
            if (b) {
                showMessage(true,"m");
            }
        } else {
            showMessage(false,"c");
        }
        Comentario="";b=true;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String Comentario) {
        this.Comentario = Comentario;
    }

}
