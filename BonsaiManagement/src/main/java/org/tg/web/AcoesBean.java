package org.tg.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.tg.domain.AcoesAttachment;
import org.tg.domain.AcoesEntity;
import org.tg.domain.AgendaEntity;
import org.tg.domain.TipoAcaoEntity;
import org.tg.service.AcoesAttachmentService;
import org.tg.service.AcoesService;
import org.tg.service.AgendaService;
import org.tg.service.TipoAcaoService;
import org.tg.web.util.MessageFactory;

@Named("acoesBean")
@ViewScoped
public class AcoesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(AcoesBean.class.getName());
    
    private List<AcoesEntity> acoesList;

    private AcoesEntity acoes;
    
    private List<AcoesAttachment> acoesAttachments;
    
    @Inject
    private AcoesService acoesService;
    
    @Inject
    private AcoesAttachmentService acoesAttachmentService;
    
    @Inject
    private TipoAcaoService tipoAcaoService;
    
    @Inject
    private AgendaService agendaService;
    
    private List<TipoAcaoEntity> allTiposList;
    
    private List<AgendaEntity> availableAgendaList;
    
    public void prepareNewAcoes() {
        reset();
        this.acoes = new AcoesEntity();
        // set any default values now, if you need
        // Example: this.acoes.setAnything("test");
    }

    public String persist() {

        String message;
        
        try {
            
            if (acoes.getId() != null) {
                acoes = acoesService.update(acoes);
                message = "message_successfully_updated";
            } else {
                acoes = acoesService.save(acoes);
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
        
        acoesList = null;

        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            acoesService.delete(acoes);
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
        acoes = null;
        acoesList = null;
        
        acoesAttachments = null;
        
        allTiposList = null;
        
        availableAgendaList = null;
        
    }

    // Get a List of all tipo
    public List<TipoAcaoEntity> getTipos() {
        if (this.allTiposList == null) {
            this.allTiposList = tipoAcaoService.findAllTipoAcaoEntities();
        }
        return this.allTiposList;
    }
    
    // Update tipo of the current acoes
    public void updateTipo(TipoAcaoEntity tipoAcao) {
        this.acoes.setTipo(tipoAcao);
        // Maybe we just created and assigned a new tipoAcao. So reset the allTipoList.
        allTiposList = null;
    }
    
    // Get a List of all agenda
    public List<AgendaEntity> getAvailableAgenda() {
        if (this.availableAgendaList == null) {
            this.availableAgendaList = agendaService.findAvailableAgenda(this.acoes);
        }
        return this.availableAgendaList;
    }
    
    // Update agenda of the current acoes
    public void updateAgenda(AgendaEntity agenda) {
        this.acoes.setAgenda(agenda);
        // Maybe we just created and assigned a new agenda. So reset the availableAgendaList.
        availableAgendaList = null;
    }
    
    public List<AcoesAttachment> getAcoesAttachments() {
        if (this.acoesAttachments == null && this.acoes != null && this.acoes.getId() != null) {
            // The byte streams are not loaded from database with following line. This would cost too much.
            this.acoesAttachments = this.acoesAttachmentService.getAttachmentsList(acoes);
        }
        return this.acoesAttachments;
    }
    
    public void handleAttachmentUpload(FileUploadEvent event) {
        
        AcoesAttachment acoesAttachment = new AcoesAttachment();
        
        try {
            // Would be better to use ...getFile().getContents(), but does not work on every environment
            acoesAttachment.setContent(IOUtils.toByteArray(event.getFile().getInputstream()));
        
            acoesAttachment.setFileName(event.getFile().getFileName());
            acoesAttachment.setAcoes(acoes);
            acoesAttachmentService.save(acoesAttachment);
            
            // set acoesAttachment to null, will be refreshed on next demand
            this.acoesAttachments = null;
            
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_successfully_uploaded");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_upload_exception");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    public StreamedContent getAttachmentStreamedContent(
            AcoesAttachment acoesAttachment) {
        if (acoesAttachment != null && acoesAttachment.getContent() == null) {
            acoesAttachment = acoesAttachmentService
                    .find(acoesAttachment.getId());
        }
        return new DefaultStreamedContent(new ByteArrayInputStream(
                acoesAttachment.getContent()),
                new MimetypesFileTypeMap().getContentType(acoesAttachment
                        .getFileName()), acoesAttachment.getFileName());
    }

    public String deleteAttachment(AcoesAttachment attachment) {
        
        acoesAttachmentService.delete(attachment);
        
        // set acoesAttachment to null, will be refreshed on next demand
        this.acoesAttachments = null;
        
        FacesMessage facesMessage = MessageFactory.getMessage(
                "message_successfully_deleted", "Attachment");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        return null;
    }
    
    public AcoesEntity getAcoes() {
        if (this.acoes == null) {
            prepareNewAcoes();
        }
        return this.acoes;
    }
    
    public void setAcoes(AcoesEntity acoes) {
        this.acoes = acoes;
    }
    
    public List<AcoesEntity> getAcoesList() {
        if (acoesList == null) {
            acoesList = acoesService.findAllAcoesEntities();
        }
        return acoesList;
    }

    public void setAcoesList(List<AcoesEntity> acoesList) {
        this.acoesList = acoesList;
    }
    
}
