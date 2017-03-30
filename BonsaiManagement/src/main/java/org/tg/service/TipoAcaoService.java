package org.tg.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.tg.domain.TipoAcaoEntity;

@Named
public class TipoAcaoService extends BaseService<TipoAcaoEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TipoAcaoService(){
        super(TipoAcaoEntity.class);
    }
    
    @Transactional
    public List<TipoAcaoEntity> findAllTipoAcaoEntities() {
        
        return entityManager.createQuery("SELECT o FROM TipoAcao o ", TipoAcaoEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM TipoAcao o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(TipoAcaoEntity tipoAcao) {

        /* This is called before a TipoAcao is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllTipoAgendasAssignments(tipoAcao);
        
        this.cutAllTipoAcoessAssignments(tipoAcao);
        
    }

    // Remove all assignments from all agenda a tipoAcao. Called before delete a tipoAcao.
    @Transactional
    private void cutAllTipoAgendasAssignments(TipoAcaoEntity tipoAcao) {
        entityManager
                .createQuery("UPDATE Agenda c SET c.tipo = NULL WHERE c.tipo = :p")
                .setParameter("p", tipoAcao).executeUpdate();
    }
    
    // Remove all assignments from all acoes a tipoAcao. Called before delete a tipoAcao.
    @Transactional
    private void cutAllTipoAcoessAssignments(TipoAcaoEntity tipoAcao) {
        entityManager
                .createQuery("UPDATE Acoes c SET c.tipo = NULL WHERE c.tipo = :p")
                .setParameter("p", tipoAcao).executeUpdate();
    }
    
}
