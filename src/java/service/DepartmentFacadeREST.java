/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entitiesJPA.Department;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.GetCollectionException;
import exceptions.SelectException;
import exceptions.UpdateException;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import interfaces.EJBDepartmentInterface;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.InternalServerErrorException;

/**
 *
 * @author 2dam
 */
@Path("department")
public class DepartmentFacadeREST {

    @EJB(beanName = "EJBDepartment")
    private EJBDepartmentInterface ejb;

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Department entity) {
        try {
            ejb.createDepartment(entity);
        } catch (CreateException ex) {
            Logger.getLogger(DepartmentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(Department entity) {
        try {
            ejb.updateDepartment(entity);
        } catch (UpdateException ex) {
            Logger.getLogger(DepartmentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        try {
            ejb.deleteDepartment(id);
        } catch (DeleteException ex) {
            Logger.getLogger(DepartmentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException(ex.getMessage());

        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Department find(@PathParam("id") Integer id) throws InternalServerErrorException {
        Department department = null;
        try {
            department = ejb.getDepartmentProfile(id);
        } catch (SelectException ex) {
            Logger.getLogger(CompanyFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("No existe departamento con esta id en la base de datos");
        }
        return department;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public Set<Department> FindAllDepartment() throws InternalServerErrorException {
        Set<Department> ret = null;
        try {
            ret = ejb.getDepartmentList();
        } catch (GetCollectionException ex) {
            Logger.getLogger(DepartmentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("No hay departamentos en la base de datos");
        }
        return ret;
    }

}
