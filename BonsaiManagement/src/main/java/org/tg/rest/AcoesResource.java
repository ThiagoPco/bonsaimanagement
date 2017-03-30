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
import org.tg.service.AcoesService;

@Path("/acoess")
@Named
public class AcoesResource implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private AcoesService acoesService;
    
    /**
     * Get the complete list of Acoes Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /acoess
     * @return List of AcoesEntity (JSON)
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AcoesEntity> getAllAcoess() {
        return acoesService.findAllAcoesEntities();
    }
    
    /**
     * Get the number of Acoes Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /acoess/count
     * @return Number of AcoesEntity
     */
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public long getCount() {
        return acoesService.countAllEntries();
    }
    
    /**
     * Get a Acoes Entity <br/>
     * HTTP Method: GET <br/>
     * Example URL: /acoess/3
     * @param id
     * @return A Acoes Entity (JSON)
     */
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AcoesEntity getAcoesById(@PathParam("id") Long id) {
        return acoesService.find(id);
    }
    
    /**
     * Create a Acoes Entity <br/>
     * HTTP Method: POST <br/>
     * POST Request Body: New AcoesEntity (JSON) <br/>
     * Example URL: /acoess
     * @param acoes
     * @return A AcoesEntity (JSON)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AcoesEntity addAcoes(AcoesEntity acoes) {
        return acoesService.save(acoes);
    }
    
    /**
     * Update an existing Acoes Entity <br/>
     * HTTP Method: PUT <br/>
     * PUT Request Body: Updated AcoesEntity (JSON) <br/>
     * Example URL: /acoess
     * @param acoes
     * @return A AcoesEntity (JSON)
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AcoesEntity updateAcoes(AcoesEntity acoes) {
        return acoesService.update(acoes);
    }
    
    /**
     * Delete an existing Acoes Entity <br/>
     * HTTP Method: DELETE <br/>
     * Example URL: /acoess/3
     * @param id
     */
    @Path("{id}")
    @DELETE
    public void deleteAcoes(@PathParam("id") Long id) {
        AcoesEntity acoes = acoesService.find(id);
        acoesService.delete(acoes);
    }
    
}
