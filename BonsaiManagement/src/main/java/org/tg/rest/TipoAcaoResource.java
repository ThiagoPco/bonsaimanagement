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

import org.tg.domain.TipoAcaoEntity;
import org.tg.service.TipoAcaoService;

@Path("/tipoAcaos")
@Named
public class TipoAcaoResource implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private TipoAcaoService tipoAcaoService;
    
    /**
     * Get the complete list of TipoAcao Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /tipoAcaos
     * @return List of TipoAcaoEntity (JSON)
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TipoAcaoEntity> getAllTipoAcaos() {
        return tipoAcaoService.findAllTipoAcaoEntities();
    }
    
    /**
     * Get the number of TipoAcao Entries <br/>
     * HTTP Method: GET <br/>
     * Example URL: /tipoAcaos/count
     * @return Number of TipoAcaoEntity
     */
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public long getCount() {
        return tipoAcaoService.countAllEntries();
    }
    
    /**
     * Get a TipoAcao Entity <br/>
     * HTTP Method: GET <br/>
     * Example URL: /tipoAcaos/3
     * @param id
     * @return A TipoAcao Entity (JSON)
     */
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TipoAcaoEntity getTipoAcaoById(@PathParam("id") Long id) {
        return tipoAcaoService.find(id);
    }
    
    /**
     * Create a TipoAcao Entity <br/>
     * HTTP Method: POST <br/>
     * POST Request Body: New TipoAcaoEntity (JSON) <br/>
     * Example URL: /tipoAcaos
     * @param tipoAcao
     * @return A TipoAcaoEntity (JSON)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TipoAcaoEntity addTipoAcao(TipoAcaoEntity tipoAcao) {
        return tipoAcaoService.save(tipoAcao);
    }
    
    /**
     * Update an existing TipoAcao Entity <br/>
     * HTTP Method: PUT <br/>
     * PUT Request Body: Updated TipoAcaoEntity (JSON) <br/>
     * Example URL: /tipoAcaos
     * @param tipoAcao
     * @return A TipoAcaoEntity (JSON)
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TipoAcaoEntity updateTipoAcao(TipoAcaoEntity tipoAcao) {
        return tipoAcaoService.update(tipoAcao);
    }
    
    /**
     * Delete an existing TipoAcao Entity <br/>
     * HTTP Method: DELETE <br/>
     * Example URL: /tipoAcaos/3
     * @param id
     */
    @Path("{id}")
    @DELETE
    public void deleteTipoAcao(@PathParam("id") Long id) {
        TipoAcaoEntity tipoAcao = tipoAcaoService.find(id);
        tipoAcaoService.delete(tipoAcao);
    }
    
}
