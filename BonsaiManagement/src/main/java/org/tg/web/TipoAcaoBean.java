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

import org.tg.domain.TipoAcaoEntity;
import org.tg.service.TipoAcaoService;
import org.tg.web.util.MessageFactory;

@Named("tipoAcaoBean")
@ViewScoped
public class TipoAcaoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TipoAcaoBean.class.getName());
    
    private List<TipoAcaoEntity> tipoAcaoList;

    private TipoAcaoEntity tipoAcao;
    
    @Inject
    private TipoAcaoService tipoAcaoService;
    
    public void prepareNewTipoAcao() {
        reset();
        this.tipoAcao = new TipoAcaoEntity();
        // set any default values now, if you need
        // Example: this.tipoAcao.setAnything("test");
    }

    public String persist() {

        String message;
        
        try {
            
            if (tipoAcao.getId() != null) {
                tipoAcao = tipoAcaoService.update(tipoAcao);
                message = "message_successfully_updated";
            } else {
                tipoAcao = tipoAcaoService.save(tipoAcao);
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
        
        tipoAcaoList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            tipoAcaoService.delete(tipoAcao);
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
        tipoAcao = null;
        tipoAcaoList = null;
        
    }

    public TipoAcaoEntity getTipoAcao() {
        if (this.tipoAcao == null) {
            prepareNewTipoAcao();
        }
        return this.tipoAcao;
    }
    
    public void setTipoAcao(TipoAcaoEntity tipoAcao) {
        this.tipoAcao = tipoAcao;
    }
    
    public List<TipoAcaoEntity> getTipoAcaoList() {
        if (tipoAcaoList == null) {
            tipoAcaoList = tipoAcaoService.findAllTipoAcaoEntities();
        }
        return tipoAcaoList;
    }

    public void setTipoAcaoList(List<TipoAcaoEntity> tipoAcaoList) {
        this.tipoAcaoList = tipoAcaoList;
    }
    
}
