package org.tg.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.tg.domain.AcoesEntity;
import org.tg.domain.AgendaEntity;
import org.tg.domain.BonsaiEntity;
import org.tg.domain.TipoAcaoEntity;

@Named
public class AgendaService extends BaseService<AgendaEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public AgendaService(){
        super(AgendaEntity.class);
    }
    
    @Transactional
    public List<AgendaEntity> findAllAgendaEntities() {
        
        return entityManager.createQuery("SELECT o FROM Agenda o ", AgendaEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM Agenda o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(AgendaEntity agenda) {

        /* This is called before a Agenda is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllAgendaAcoessAssignments(agenda);
        
    }

    // Remove all assignments from all acoes a agenda. Called before delete a agenda.
    @Transactional
    private void cutAllAgendaAcoessAssignments(AgendaEntity agenda) {
        entityManager
                .createQuery("UPDATE Acoes c SET c.agenda = NULL WHERE c.agenda = :p")
                .setParameter("p", agenda).executeUpdate();
    }
    
    @Transactional
    public List<AgendaEntity> findAvailableAgendas(TipoAcaoEntity tipoAcao) {
        return entityManager.createQuery("SELECT o FROM Agenda o WHERE o.tipo IS NULL", AgendaEntity.class).getResultList();
    }

    @Transactional
    public List<AgendaEntity> findAgendasByTipo(TipoAcaoEntity tipoAcao) {
        return entityManager.createQuery("SELECT o FROM Agenda o WHERE o.tipo = :tipoAcao", AgendaEntity.class).setParameter("tipoAcao", tipoAcao).getResultList();
    }

    @Transactional
    public List<AgendaEntity> findAvailableAgendas(BonsaiEntity bonsai) {
        return entityManager.createQuery("SELECT o FROM Agenda o WHERE o.bonsai IS NULL", AgendaEntity.class).getResultList();
    }

    @Transactional
    public List<AgendaEntity> findAgendasByBonsai(BonsaiEntity bonsai) {
        return entityManager.createQuery("SELECT o FROM Agenda o WHERE o.bonsai = :bonsai", AgendaEntity.class).setParameter("bonsai", bonsai).getResultList();
    }

    // Find all acoes which are not yet assigned to a agenda
    @Transactional
    public List<AgendaEntity> findAvailableAgenda(AcoesEntity acoes) {
        Long id = -1L;
        if (acoes != null && acoes.getAgenda() != null && acoes.getAgenda().getId() != null) {
            id = acoes.getAgenda().getId();
        }
        return entityManager.createQuery(
                "SELECT o FROM Agenda o where o.id NOT IN (SELECT p.agenda.id FROM Acoes p where p.agenda.id != :id)", AgendaEntity.class)
                .setParameter("id", id).getResultList();    
    }

}
