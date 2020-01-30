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

    @EJB(beanName = "EJBArea")
    private EJBAreaInterface ejb;

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Area entity) {
        try {
            ejb.createArea(entity);
        } catch (CreateException ex) {
            Logger.getLogger(DepartmentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(Area entity) {
        try {
            ejb.updateArea(entity);
        } catch (UpdateException ex) {
            Logger.getLogger(DepartmentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        try {
            ejb.deleteArea(id);
        } catch (DeleteException ex) {
            Logger.getLogger(DepartmentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Area find(@PathParam("id") Integer id) throws InternalServerErrorException {
        Area company = null;
        try {
            company = ejb.getCompanyProfile(id);
        } catch (SelectException ex) {
            Logger.getLogger(CompanyFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("No hay area con esa id en la base de datos.");
        }
        return company;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public Set<Area> FindAllArea() throws InternalServerErrorException {
        Set<Area> ret = null;
        try {
            ret = ejb.getAreaList();
        } catch (GetCollectionException ex) {
            Logger.getLogger(DepartmentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("No hay areas en la base de datos.");
        }
        return ret;
    }
}
