package org.tg.rest;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.tg.domain.AcoesEntity;
import org.tg.domain.AgendaEntity;
import org.tg.domain.BonsaiEntity;
import org.tg.service.AcoesService;
import org.tg.service.AgendaService;
import org.tg.service.BonsaiService;

@Path("/bonsais")
@Named
public class BonsaiResource implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private BonsaiService bonsaiService;
    
    @Inject
    private AcoesService acoesService;
    
    @Inject
    private AgendaService agendaService;
    
    /**
     * Get the complete list of Bonsai Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /bonsais
     * @return List of BonsaiEntity (JSON)
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BonsaiEntity> getAllBonsais() {
        return bonsaiService.findAllBonsaiEntities();
    }
    
    /**
     * Get the number of Bonsai Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /bonsais/count
     * @return Number of BonsaiEntity
     */
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public long getCount() {
        return bonsaiService.countAllEntries();
    }
    
    /**
     * Get a Bonsai Entity <br/>
     * HTTP Method: GET <br/>
     * Example URL: /bonsais/3
     * @param id
     * @return A Bonsai Entity (JSON)
     */
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BonsaiEntity getBonsaiById(@PathParam("id") Long id) {
        return bonsaiService.find(id);
    }
    
    /**
     * Create a Bonsai Entity <br/>
     * HTTP Method: POST <br/>
     * POST Request Body: New BonsaiEntity (JSON) <br/>
     * Example URL: /bonsais
     * @param bonsai
     * @return A BonsaiEntity (JSON)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BonsaiEntity addBonsai(BonsaiEntity bonsai) {
        return bonsaiService.save(bonsai);
    }
    
    /**
     * Update an existing Bonsai Entity <br/>
     * HTTP Method: PUT <br/>
     * PUT Request Body: Updated BonsaiEntity (JSON) <br/>
     * Example URL: /bonsais
     * @param bonsai
     * @return A BonsaiEntity (JSON)
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BonsaiEntity updateBonsai(BonsaiEntity bonsai) {
        return bonsaiService.update(bonsai);
    }
    
    /**
     * Delete an existing Bonsai Entity <br/>
     * HTTP Method: DELETE <br/>
     * Example URL: /bonsais/3
     * @param id
     */
    @Path("{id}")
    @DELETE
    public void deleteBonsai(@PathParam("id") Long id) {
        BonsaiEntity bonsai = bonsaiService.find(id);
        bonsaiService.delete(bonsai);
    }
    
    /**
     * Get the list of AcoesRealizadas that is assigned to a Bonsai <br/>
     * HTTP Method: GET <br/>
     * Example URL: /bonsais/3/acoesRealizadass
     * @param bonsaiId
     * @return List of AcoesEntity
     */
    @GET
    @Path("{id}/acoesRealizadass")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AcoesEntity> getAcoesRealizadass(@PathParam("id") Long bonsaiId) {
        BonsaiEntity bonsai = bonsaiService.find(bonsaiId);
        return acoesService.findAcoesRealizadassByBonsai(bonsai);
    }
    
    /**
     * Assign an existing AcoesRealizadas to an existing Bonsai <br/>
     * HTTP Method: PUT <br/>
     * PUT Request Body: empty <br/>
     * Example URL: /bonsais/3/acoesRealizadass/8
     * @param bonsaiId
     * @param acoesRealizadasId
     */
    @PUT
    @Path("{id}/acoesRealizadass/{acoesRealizadasId}")
    public void assignAcoesRealizadas(@PathParam("id") Long bonsaiId, @PathParam("acoesRealizadasId") Long acoesRealizadasId) {
        BonsaiEntity bonsai = bonsaiService.find(bonsaiId);
        AcoesEntity acoesRealizadas = acoesService.find(acoesRealizadasId);
        acoesRealizadas.setBonsai(bonsai);
        acoesService.update(acoesRealizadas);
    }
    
    /**
     * Remove a Bonsai-to-AcoesRealizadas Assignment <br/>
     * HTTP Method: DELETE <br/>
     * Example URL: /bonsais/3/acoesRealizadass/8
     * @param bonsaiId
     * @param acoesRealizadasId
     */
    @DELETE
    @Path("{id}/acoesRealizadass/{acoesRealizadasId}")
    public void unassignAcoesRealizadas(@PathParam("id") Long bonsaiId, @PathParam("acoesRealizadasId") Long acoesRealizadasId) {
        AcoesEntity acoesRealizadas = acoesService.find(acoesRealizadasId);
        acoesRealizadas.setBonsai(null);
        acoesService.update(acoesRealizadas);
    }
    
    /**
     * Get the list of Agenda that is assigned to a Bonsai <br/>
     * HTTP Method: GET <br/>
     * Example URL: /bonsais/3/agendas
     * @param bonsaiId
     * @return List of AgendaEntity
     */
    @GET
    @Path("{id}/agendas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AgendaEntity> getAgendas(@PathParam("id") Long bonsaiId) {
        BonsaiEntity bonsai = bonsaiService.find(bonsaiId);
        return agendaService.findAgendasByBonsai(bonsai);
    }
    
    /**
     * Assign an existing Agenda to an existing Bonsai <br/>
     * HTTP Method: PUT <br/>
     * PUT Request Body: empty <br/>
     * Example URL: /bonsais/3/agendas/8
     * @param bonsaiId
     * @param agendaId
     */
    @PUT
    @Path("{id}/agendas/{agendaId}")
    public void assignAgenda(@PathParam("id") Long bonsaiId, @PathParam("agendaId") Long agendaId) {
        BonsaiEntity bonsai = bonsaiService.find(bonsaiId);
        AgendaEntity agenda = agendaService.find(agendaId);
        agenda.setBonsai(bonsai);
        agendaService.update(agenda);
    }
    
    /**
     * Remove a Bonsai-to-Agenda Assignment <br/>
     * HTTP Method: DELETE <br/>
     * Example URL: /bonsais/3/agendas/8
     * @param bonsaiId
     * @param agendaId
     */
    @DELETE
    @Path("{id}/agendas/{agendaId}")
    public void unassignAgenda(@PathParam("id") Long bonsaiId, @PathParam("agendaId") Long agendaId) {
        AgendaEntity agenda = agendaService.find(agendaId);
        agenda.setBonsai(null);
        agendaService.update(agenda);
    }
    
}
