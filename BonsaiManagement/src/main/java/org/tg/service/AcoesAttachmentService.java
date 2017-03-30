package org.tg.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import org.tg.domain.AcoesAttachment;
import org.tg.domain.AcoesEntity;

@Named
public class AcoesAttachmentService extends BaseService<AcoesAttachment> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public AcoesAttachmentService(){
        super(AcoesAttachment.class);
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM AcoesAttachment o", Long.class).getSingleResult();
    }

    @Transactional
    public void deleteAttachmentsByAcoes(AcoesEntity acoes) {
        entityManager
                .createQuery("DELETE FROM AcoesAttachment c WHERE c.acoes = :p")
                .setParameter("p", acoes).executeUpdate();
    }
    
    @Transactional
    public List<AcoesAttachment> getAttachmentsList(AcoesEntity acoes) {
        if (acoes == null || acoes.getId() == null) {
            return new ArrayList<>();
        }
        // The byte streams are not loaded from database with following line. This would cost too much.
        return entityManager.createQuery("SELECT NEW org.tg.domain.AcoesAttachment(o.id, o.fileName) FROM AcoesAttachment o WHERE o.acoes.id = :id", AcoesAttachment.class).setParameter("id", acoes.getId()).getResultList();
    }
}
