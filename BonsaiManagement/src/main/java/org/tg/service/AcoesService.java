package org.tg.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.tg.domain.AcoesEntity;
import org.tg.domain.BonsaiEntity;
import org.tg.domain.TipoAcaoEntity;

@Named
public class AcoesService extends BaseService<AcoesEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public AcoesService(){
        super(AcoesEntity.class);
    }
    
    @Inject
    private AcoesAttachmentService attachmentService;
    
    @Transactional
    public List<AcoesEntity> findAllAcoesEntities() {
        
        return entityManager.createQuery("SELECT o FROM Acoes o ", AcoesEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM Acoes o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(AcoesEntity acoes) {

        /* This is called before a Acoes is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.attachmentService.deleteAttachmentsByAcoes(acoes);
        
    }

    @Transactional
    public List<AcoesEntity> findAvailableAcoess(TipoAcaoEntity tipoAcao) {
        return entityManager.createQuery("SELECT o FROM Acoes o WHERE o.tipo IS NULL", AcoesEntity.class).getResultList();
    }

    @Transactional
    public List<AcoesEntity> findAcoessByTipo(TipoAcaoEntity tipoAcao) {
        return entityManager.createQuery("SELECT o FROM Acoes o WHERE o.tipo = :tipoAcao", AcoesEntity.class).setParameter("tipoAcao", tipoAcao).getResultList();
    }

    @Transactional
    public List<AcoesEntity> findAvailableAcoesRealizadass(BonsaiEntity bonsai) {
        return entityManager.createQuery("SELECT o FROM Acoes o WHERE o.bonsai IS NULL", AcoesEntity.class).getResultList();
    }

    @Transactional
    public List<AcoesEntity> findAcoesRealizadassByBonsai(BonsaiEntity bonsai) {
        return entityManager.createQuery("SELECT o FROM Acoes o WHERE o.bonsai = :bonsai", AcoesEntity.class).setParameter("bonsai", bonsai).getResultList();
    }

}
