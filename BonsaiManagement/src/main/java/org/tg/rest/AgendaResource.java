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

import org.tg.domain.AgendaEntity;
import org.tg.service.AgendaService;

@Path("/agendas")
@Named
public class AgendaResource implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private AgendaService agendaService;
    
    /**
     * Get the complete list of Agenda Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /agendas
     * @return List of AgendaEntity (JSON)
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AgendaEntity> getAllAgendas() {
        return agendaService.findAllAgendaEntities();
    }
    
    /**
     * Get the number of Agenda Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /agendas/count
     * @return Number of AgendaEntity
     */
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public long getCount() {
        return agendaService.countAllEntries();
    }
    
    /**
     * Get a Agenda Entity <br/>
     * HTTP Method: GET <br/>
     * Example URL: /agendas/3
     * @param id
     * @return A Agenda Entity (JSON)
     */
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AgendaEntity getAgendaById(@PathParam("id") Long id) {
        return agendaService.find(id);
    }
    
    /**
     * Create a Agenda Entity <br/>
     * HTTP Method: POST <br/>
     * POST Request Body: New AgendaEntity (JSON) <br/>
     * Example URL: /agendas
     * @param agenda
     * @return A AgendaEntity (JSON)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AgendaEntity addAgenda(AgendaEntity agenda) {
        return agendaService.save(agenda);
    }
    
    /**
     * Update an existing Agenda Entity <br/>
     * HTTP Method: PUT <br/>
     * PUT Request Body: Updated AgendaEntity (JSON) <br/>
     * Example URL: /agendas
     * @param agenda
     * @return A AgendaEntity (JSON)
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AgendaEntity updateAgenda(AgendaEntity agenda) {
        return agendaService.update(agenda);
    }
    
    /**
     * Delete an existing Agenda Entity <br/>
     * HTTP Method: DELETE <br/>
     * Example URL: /agendas/3
     * @param id
     */
    @Path("{id}")
    @DELETE
    public void deleteAgenda(@PathParam("id") Long id) {
        AgendaEntity agenda = agendaService.find(id);
        agendaService.delete(agenda);
    }
    
}
