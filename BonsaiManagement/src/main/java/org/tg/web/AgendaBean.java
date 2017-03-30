package org.tg.web;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.tg.domain.AgendaEntity;
import org.tg.domain.TipoAcaoEntity;
import org.tg.service.AgendaService;
import org.tg.service.TipoAcaoService;
import org.tg.web.util.MessageFactory;

@Named("agendaBean")
@ViewScoped
public class AgendaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(AgendaBean.class.getName());
    
    private List<AgendaEntity> agendaList;

    private AgendaEntity agenda;
    
    @Inject
    private AgendaService agendaService;
    
    @Inject
    private TipoAcaoService tipoAcaoService;
    
    private List<TipoAcaoEntity> allTiposList;
    
    public void prepareNewAgenda() {
        reset();
        this.agenda = new AgendaEntity();
        // set any default values now, if you need
        // Example: this.agenda.setAnything("test");
    }

    public String persist() {

        String message;
        
        try {
            
            if (agenda.getId() != null) {
                agenda = agendaService.update(agenda);
                message = "message_successfully_updated";
            } else {
                agenda = agendaService.save(agenda);
                message = "message_successfully_created";
            }
        } catch (OptimisticLockException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "message_optimistic_locking_exception";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        } catch (PersistenceException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "message_save_exception";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
        
        agendaList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            agendaService.delete(agenda);
            message = "message_successfully_deleted";
            reset();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "message_delete_exception";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
        FacesContext.getCurrentInstance().addMessage(null, MessageFactory.getMessage(message));
        
        return null;
    }
    
    public void reset() {
        agenda = null;
        agendaList = null;
        
        allTiposList = null;
        
    }

    // Get a List of all tipo
    public List<TipoAcaoEntity> getTipos() {
        if (this.allTiposList == null) {
            this.allTiposList = tipoAcaoService.findAllTipoAcaoEntities();
        }
        return this.allTiposList;
    }
    
    // Update tipo of the current agenda
    public void updateTipo(TipoAcaoEntity tipoAcao) {
        this.agenda.setTipo(tipoAcao);
        // Maybe we just created and assigned a new tipoAcao. So reset the allTipoList.
        allTiposList = null;
    }
    
    public AgendaEntity getAgenda() {
        if (this.agenda == null) {
            prepareNewAgenda();
        }
        return this.agenda;
    }
    
    public void setAgenda(AgendaEntity agenda) {
        this.agenda = agenda;
    }
    
    public List<AgendaEntity> getAgendaList() {
        if (agendaList == null) {
            agendaList = agendaService.findAllAgendaEntities();
        }
        return agendaList;
    }

    public void setAgendaList(List<AgendaEntity> agendaList) {
        this.agendaList = agendaList;
    }
    
}
