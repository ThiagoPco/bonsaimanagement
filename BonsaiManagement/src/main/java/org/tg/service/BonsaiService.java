package org.tg.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.primefaces.model.SortOrder;
import org.tg.domain.BonsaiClassificacao;
import org.tg.domain.BonsaiEntity;
import org.tg.domain.BonsaiEstilo;

@Named
public class BonsaiService extends BaseService<BonsaiEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public BonsaiService(){
        super(BonsaiEntity.class);
    }
    
    @Transactional
    public List<BonsaiEntity> findAllBonsaiEntities() {
        
        return entityManager.createQuery("SELECT o FROM Bonsai o ", BonsaiEntity.class).getResultList();
    }
    
    @Override
    @Transactional
    public long countAllEntries() {
        return entityManager.createQuery("SELECT COUNT(o) FROM Bonsai o", Long.class).getSingleResult();
    }
    
    @Override
    protected void handleDependenciesBeforeDelete(BonsaiEntity bonsai) {

        /* This is called before a Bonsai is deleted. Place here all the
           steps to cut dependencies to other entities */
        
        this.cutAllBonsaiAcoesRealizadassAssignments(bonsai);
        
        this.cutAllBonsaiAgendasAssignments(bonsai);
        
    }

    // Remove all assignments from all acoesRealizadas a bonsai. Called before delete a bonsai.
    @Transactional
    private void cutAllBonsaiAcoesRealizadassAssignments(BonsaiEntity bonsai) {
        entityManager
                .createQuery("UPDATE Acoes c SET c.bonsai = NULL WHERE c.bonsai = :p")
                .setParameter("p", bonsai).executeUpdate();
    }
    
    // Remove all assignments from all agenda a bonsai. Called before delete a bonsai.
    @Transactional
    private void cutAllBonsaiAgendasAssignments(BonsaiEntity bonsai) {
        entityManager
                .createQuery("UPDATE Agenda c SET c.bonsai = NULL WHERE c.bonsai = :p")
                .setParameter("p", bonsai).executeUpdate();
    }
    
    @Transactional
    public BonsaiEntity lazilyLoadImageToBonsai(BonsaiEntity bonsai) {
        PersistenceUnitUtil u = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        if (!u.isLoaded(bonsai, "image") && bonsai.getId() != null) {
            bonsai = find(bonsai.getId());
            bonsai.getImage().getId();
        }
        return bonsai;
    }
    
    // This is the central method called by the DataTable
    @Override
    @Transactional
    public List<BonsaiEntity> findEntriesPagedAndFilteredAndSorted(int firstResult, int maxResults, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        
        StringBuilder query = new StringBuilder();

        query.append("SELECT o FROM Bonsai o");
        
        query.append(" LEFT JOIN FETCH o.image");
        
        String nextConnective = " WHERE";
        
        Map<String, Object> queryParameters = new HashMap<>();
        
        if (filters != null && !filters.isEmpty()) {
            
            nextConnective += " ( ";
            
            for(String filterProperty : filters.keySet()) {
                
                if (filters.get(filterProperty) == null) {
                    continue;
                }
                
                switch (filterProperty) {
                
                case "codigo":
                    query.append(nextConnective).append(" o.codigo = :codigo");
                    queryParameters.put("codigo", new Integer(filters.get(filterProperty).toString()));
                    break;

                case "descricao":
                    query.append(nextConnective).append(" o.descricao LIKE :descricao");
                    queryParameters.put("descricao", "%" + filters.get(filterProperty) + "%");
                    break;

                case "dataAquisicao":
                    query.append(nextConnective).append(" o.dataAquisicao = :dataAquisicao");
                    queryParameters.put("dataAquisicao", filters.get(filterProperty));
                    break;

                case "nome":
                    query.append(nextConnective).append(" o.nome LIKE :nome");
                    queryParameters.put("nome", "%" + filters.get(filterProperty) + "%");
                    break;

                case "estilo":
                    query.append(nextConnective).append(" o.estilo = :estilo");
                    queryParameters.put("estilo", BonsaiEstilo.valueOf(filters.get(filterProperty).toString()));
                    break;

                case "tamanho":
                    query.append(nextConnective).append(" o.tamanho = :tamanho");
                    queryParameters.put("tamanho", new BigDecimal(filters.get(filterProperty).toString()));
                    break;

                case "classificacao":
                    query.append(nextConnective).append(" o.classificacao = :classificacao");
                    queryParameters.put("classificacao", BonsaiClassificacao.valueOf(filters.get(filterProperty).toString()));
                    break;

                }
                
                nextConnective = " AND";
            }
            
            query.append(" ) ");
            nextConnective = " AND";
        }
        
        if (sortField != null && !sortField.isEmpty()) {
            query.append(" ORDER BY o.").append(sortField);
            query.append(SortOrder.DESCENDING.equals(sortOrder) ? " DESC" : " ASC");
        }
        
        TypedQuery<BonsaiEntity> q = this.entityManager.createQuery(query.toString(), this.getType());
        
        for(String queryParameter : queryParameters.keySet()) {
            q.setParameter(queryParameter, queryParameters.get(queryParameter));
        }

        return q.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
