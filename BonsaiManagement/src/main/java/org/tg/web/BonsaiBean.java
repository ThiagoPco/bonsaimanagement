package org.tg.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;
import org.tg.domain.AcoesEntity;
import org.tg.domain.AgendaEntity;
import org.tg.domain.BonsaiClassificacao;
import org.tg.domain.BonsaiEntity;
import org.tg.domain.BonsaiEstilo;
import org.tg.domain.BonsaiImage;
import org.tg.service.AcoesService;
import org.tg.service.AgendaService;
import org.tg.service.BonsaiService;
import org.tg.web.generic.GenericLazyDataModel;
import org.tg.web.util.MessageFactory;

@Named("bonsaiBean")
@ViewScoped
public class BonsaiBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(BonsaiBean.class.getName());
    
    private GenericLazyDataModel<BonsaiEntity> lazyModel;
    
    private BonsaiEntity bonsai;
    
    @Inject
    private BonsaiService bonsaiService;
    
    UploadedFile uploadedImage;
    byte[] uploadedImageContents;
    
    @Inject
    private AcoesService acoesService;
    
    @Inject
    private AgendaService agendaService;
    
    private DualListModel<AcoesEntity> acoesRealizadass;
    private List<String> transferedAcoesRealizadasIDs;
    private List<String> removedAcoesRealizadasIDs;
    
    private DualListModel<AgendaEntity> agendas;
    private List<String> transferedAgendaIDs;
    private List<String> removedAgendaIDs;
    
    public void prepareNewBonsai() {
        reset();
        this.bonsai = new BonsaiEntity();
        // set any default values now, if you need
        // Example: this.bonsai.setAnything("test");
    }

    public GenericLazyDataModel<BonsaiEntity> getLazyModel() {
        if (this.lazyModel == null) {
            this.lazyModel = new GenericLazyDataModel<>(bonsaiService);
        }
        return this.lazyModel;
    }
    
    public String persist() {

        String message;
        
        try {
            
            if (this.uploadedImage != null) {
                try {

                    BufferedImage image;
                    try (InputStream in = new ByteArrayInputStream(uploadedImageContents)) {
                        image = ImageIO.read(in);
                    }
                    image = Scalr.resize(image, Method.BALANCED, 300);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageOutputStream imageOS = ImageIO.createImageOutputStream(baos);
                    Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(
                            uploadedImage.getContentType());
                    ImageWriter imageWriter = (ImageWriter) imageWriters.next();
                    imageWriter.setOutput(imageOS);
                    imageWriter.write(image);
                    
                    baos.close();
                    imageOS.close();
                    
                    logger.log(Level.INFO, "Resized uploaded image from {0} to {1}", new Object[]{uploadedImageContents.length, baos.toByteArray().length});
            
                    BonsaiImage bonsaiImage = new BonsaiImage();
                    bonsaiImage.setContent(baos.toByteArray());
                    bonsai.setImage(bonsaiImage);
                } catch (Exception e) {
                    FacesMessage facesMessage = MessageFactory.getMessage(
                            "message_upload_exception");
                    FacesContext.getCurrentInstance().addMessage(null, facesMessage);
                    FacesContext.getCurrentInstance().validationFailed();
                    return null;
                }
            }
            
            if (bonsai.getId() != null) {
                bonsai = bonsaiService.update(bonsai);
                message = "message_successfully_updated";
            } else {
                bonsai = bonsaiService.save(bonsai);
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
        
        FacesMessage facesMessage = MessageFactory.getMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        
        return null;
    }
    
    public String delete() {
        
        String message;
        
        try {
            bonsaiService.delete(bonsai);
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
        bonsai = null;

        acoesRealizadass = null;
        transferedAcoesRealizadasIDs = null;
        removedAcoesRealizadasIDs = null;
        
        agendas = null;
        transferedAgendaIDs = null;
        removedAgendaIDs = null;
        
        uploadedImage = null;
        uploadedImageContents = null;
        
    }

    public DualListModel<AcoesEntity> getAcoesRealizadass() {
        return acoesRealizadass;
    }

    public void setAcoesRealizadass(DualListModel<AcoesEntity> acoess) {
        this.acoesRealizadass = acoess;
    }
    
    public List<AcoesEntity> getFullAcoesRealizadassList() {
        List<AcoesEntity> allList = new ArrayList<>();
        allList.addAll(acoesRealizadass.getSource());
        allList.addAll(acoesRealizadass.getTarget());
        return allList;
    }
    
    public void onAcoesRealizadassDialog(BonsaiEntity bonsai) {
        // Prepare the acoesRealizadas PickList
        this.bonsai = bonsai;
        List<AcoesEntity> selectedAcoessFromDB = acoesService
                .findAcoesRealizadassByBonsai(this.bonsai);
        List<AcoesEntity> availableAcoessFromDB = acoesService
                .findAvailableAcoesRealizadass(this.bonsai);
        this.acoesRealizadass = new DualListModel<>(availableAcoessFromDB, selectedAcoessFromDB);
        
        transferedAcoesRealizadasIDs = new ArrayList<>();
        removedAcoesRealizadasIDs = new ArrayList<>();
    }
    
    public void onAcoesRealizadassPickListTransfer(TransferEvent event) {
        // If a acoesRealizadas is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((AcoesEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedAcoesRealizadasIDs.add(id);
                removedAcoesRealizadasIDs.remove(id);
            } else if (event.isRemove()) {
                removedAcoesRealizadasIDs.add(id);
                transferedAcoesRealizadasIDs.remove(id);
            }
        }
        
    }
    
    public void updateAcoesRealizadas(AcoesEntity acoes) {
        // If a new acoesRealizadas is created, we persist it to the database,
        // but we do not assign it to this bonsai in the database, yet.
        acoesRealizadass.getTarget().add(acoes);
        transferedAcoesRealizadasIDs.add(acoes.getId().toString());
    }
    
    public DualListModel<AgendaEntity> getAgendas() {
        return agendas;
    }

    public void setAgendas(DualListModel<AgendaEntity> agendas) {
        this.agendas = agendas;
    }
    
    public List<AgendaEntity> getFullAgendasList() {
        List<AgendaEntity> allList = new ArrayList<>();
        allList.addAll(agendas.getSource());
        allList.addAll(agendas.getTarget());
        return allList;
    }
    
    public void onAgendasDialog(BonsaiEntity bonsai) {
        // Prepare the agenda PickList
        this.bonsai = bonsai;
        List<AgendaEntity> selectedAgendasFromDB = agendaService
                .findAgendasByBonsai(this.bonsai);
        List<AgendaEntity> availableAgendasFromDB = agendaService
                .findAvailableAgendas(this.bonsai);
        this.agendas = new DualListModel<>(availableAgendasFromDB, selectedAgendasFromDB);
        
        transferedAgendaIDs = new ArrayList<>();
        removedAgendaIDs = new ArrayList<>();
    }
    
    public void onAgendasPickListTransfer(TransferEvent event) {
        // If a agenda is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((AgendaEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedAgendaIDs.add(id);
                removedAgendaIDs.remove(id);
            } else if (event.isRemove()) {
                removedAgendaIDs.add(id);
                transferedAgendaIDs.remove(id);
            }
        }
        
    }
    
    public void updateAgenda(AgendaEntity agenda) {
        // If a new agenda is created, we persist it to the database,
        // but we do not assign it to this bonsai in the database, yet.
        agendas.getTarget().add(agenda);
        transferedAgendaIDs.add(agenda.getId().toString());
    }
    
    public void onAcoesRealizadassSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<AcoesEntity> selectedAcoessFromDB = acoesService
                    .findAcoesRealizadassByBonsai(this.bonsai);
            List<AcoesEntity> availableAcoessFromDB = acoesService
                    .findAvailableAcoesRealizadass(this.bonsai);
            
            for (AcoesEntity acoes : selectedAcoessFromDB) {
                if (removedAcoesRealizadasIDs.contains(acoes.getId().toString())) {
                    acoes.setBonsai(null);
                    acoesService.update(acoes);
                }
            }
    
            for (AcoesEntity acoes : availableAcoessFromDB) {
                if (transferedAcoesRealizadasIDs.contains(acoes.getId().toString())) {
                    acoes.setBonsai(bonsai);
                    acoesService.update(acoes);
                }
            }
            
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_changes_saved");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            
            reset();

        } catch (OptimisticLockException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_optimistic_locking_exception");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        } catch (PersistenceException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_picklist_save_exception");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
    }
    
    public void onAgendasSubmit() {
        // Now we save the changed of the PickList to the database.
        try {
            List<AgendaEntity> selectedAgendasFromDB = agendaService
                    .findAgendasByBonsai(this.bonsai);
            List<AgendaEntity> availableAgendasFromDB = agendaService
                    .findAvailableAgendas(this.bonsai);
            
            for (AgendaEntity agenda : selectedAgendasFromDB) {
                if (removedAgendaIDs.contains(agenda.getId().toString())) {
                    agenda.setBonsai(null);
                    agendaService.update(agenda);
                }
            }
    
            for (AgendaEntity agenda : availableAgendasFromDB) {
                if (transferedAgendaIDs.contains(agenda.getId().toString())) {
                    agenda.setBonsai(bonsai);
                    agendaService.update(agenda);
                }
            }
            
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_changes_saved");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            
            reset();

        } catch (OptimisticLockException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_optimistic_locking_exception");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        } catch (PersistenceException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_picklist_save_exception");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
    }
    
    public SelectItem[] getEstiloSelectItems() {
        SelectItem[] items = new SelectItem[BonsaiEstilo.values().length];

        int i = 0;
        for (BonsaiEstilo estilo : BonsaiEstilo.values()) {
            items[i++] = new SelectItem(estilo, getLabelForEstilo(estilo));
        }
        return items;
    }
    
    public String getLabelForEstilo(BonsaiEstilo value) {
        if (value == null) {
            return "";
        }
        String label = MessageFactory.getMessageString(
                "enum_label_bonsai_estilo_" + value);
        return label == null? value.toString() : label;
    }
    
    public SelectItem[] getClassificacaoSelectItems() {
        SelectItem[] items = new SelectItem[BonsaiClassificacao.values().length];

        int i = 0;
        for (BonsaiClassificacao classificacao : BonsaiClassificacao.values()) {
            items[i++] = new SelectItem(classificacao, getLabelForClassificacao(classificacao));
        }
        return items;
    }
    
    public String getLabelForClassificacao(BonsaiClassificacao value) {
        if (value == null) {
            return "";
        }
        String label = MessageFactory.getMessageString(
                "enum_label_bonsai_classificacao_" + value);
        return label == null? value.toString() : label;
    }
    
    public void handleImageUpload(FileUploadEvent event) {
        
        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(
                event.getFile().getContentType());
        if (!imageWriters.hasNext()) {
            FacesMessage facesMessage = MessageFactory.getMessage(
                    "message_image_type_not_supported");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return;
        }
        
        this.uploadedImage = event.getFile();
        this.uploadedImageContents = event.getFile().getContents();
        
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public byte[] getUploadedImageContents() {
        if (uploadedImageContents != null) {
            return uploadedImageContents;
        } else if (bonsai != null && bonsai.getImage() != null) {
            bonsai = bonsaiService.lazilyLoadImageToBonsai(bonsai);
            return bonsai.getImage().getContent();
        }
        return null;
    }
    
    public BonsaiEntity getBonsai() {
        if (this.bonsai == null) {
            prepareNewBonsai();
        }
        return this.bonsai;
    }
    
    public void setBonsai(BonsaiEntity bonsai) {
        this.bonsai = bonsai;
    }
    
}
