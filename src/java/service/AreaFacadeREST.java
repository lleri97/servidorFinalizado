/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entitiesJPA.Area;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.GetCollectionException;
import exceptions.SelectException;
import exceptions.UpdateException;
import interfaces.EJBAreaInterface;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Andoni
 */
@Path("area")
public class AreaFacadeREST {
    
    private static final Logger LOGGER = Logger.getLogger(CompanyFacadeREST.class.getPackage() + "." + CompanyFacadeREST.class.getName());
    
    @EJB(beanName = "EJBArea")
    private EJBAreaInterface ejb;

    /**
     * Metodo que crea un area en la base de datos
     *
     * @param entity para crear en la base de atos
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Area entity) {
        try {
            LOGGER.info("Creando compañia en la base dedatos");
            ejb.createArea(entity);
        } catch (CreateException ex) {
            LOGGER.info("No se pudo crear la compañia");
            throw new InternalServerErrorException("Error al crear la compañia");
        }
    }

    /**
     * Metodo para editar compañia en la base de datos
     *
     * @param entity a modificar en la base de datos
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(Area entity) {
        try {
            LOGGER.info("Modificando area en la base de datos");
            ejb.updateArea(entity);
        } catch (UpdateException ex) {
            LOGGER.info("No se puedo actualizar la compañia");
            throw new InternalServerErrorException("Error al actualizar la compañia");
        }
    }

    /**
     * Metodo para borrar un area de la base de datos
     *
     * @param id to delete area
     */
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        try {
            LOGGER.info("Borrando area de la base de datos");
            ejb.deleteArea(id);
        } catch (DeleteException ex) {
            LOGGER.info("No hay compañias que borrar con ese id");
            throw new InternalServerErrorException("Error al borrar la compañia con ese id");
        }
    }

    /**
     * Metodo que hace una busqueda de area por id
     *
     * @param id objeto de busqueda
     * @return el objeto encontrado
     * @throws InternalServerErrorException si no hay areas con ese id en la
     * base de datos
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Area find(@PathParam("id") Integer id) throws InternalServerErrorException {
        Area company = null;
        try {
            LOGGER.info("Buscando compañia mediante id");
            company = ejb.getCompanyProfile(id);
        } catch (SelectException ex) {
            
            LOGGER.info("No hay area con ese id en la base de datos");
            throw new InternalServerErrorException("No hay area con esa id en la base de datos.");
        }
        LOGGER.info("Compañia devuelta con exito");
        return company;
    }

    /**
     * Metodo que busca una lista de areas
     *
     * @return una lista de areas
     * @throws InternalServerErrorException si no hay areas en la base de datos
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public Set<Area> FindAllArea() throws InternalServerErrorException {
        Set<Area> ret = null;
        try {
            LOGGER.info("Cargando lista de areas");
            ret = ejb.getAreaList();
        } catch (GetCollectionException ex) {
            LOGGER.info("No hay areas en la base de datos");
            throw new InternalServerErrorException("No hay areas en la base de datos.");
        }
        LOGGER.info("Lista de areas devuelta correctamente");
        return ret;
    }
}
