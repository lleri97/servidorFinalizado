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
 * Clase que maneja todos los metodos Rest de Department
 * 
 * @author Yeray
 */
@Path("department")
public class DepartmentFacadeREST {
    
        private static final Logger LOGGER = Logger.getLogger(DepartmentFacadeREST.class.getPackage() + "." + DepartmentFacadeREST.class.getName());


    @EJB(beanName = "EJBDepartment")
    private EJBDepartmentInterface ejb;

    /**
     * Metodo que crea un Departmento
     * @param entity objeto de tipo Department
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Department entity) {
        LOGGER.info("Peticion de creacion de departamento");
        try {
            LOGGER.info("Creando documento");
            ejb.createDepartment(entity);
        } catch (CreateException ex) {
            LOGGER.warning("ERROR en la creacion de un departamento");
            throw new InternalServerErrorException("ERROR en la creacion de un departamento");
        }
    }

    /**
     * Metodo para actualizar un departamneto
     * @param entity objeto del tipo Department
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(Department entity) {
        LOGGER.info("Peticion de modificacion de departamento");
        try {
            LOGGER.info("Modificando departamneto");
            ejb.updateDepartment(entity);
            LOGGER.info("Departamento modificado");
        } catch (UpdateException ex) {
            LOGGER.warning("ERROR al modificar un departamento");
            throw new InternalServerErrorException("ERROR al modificar un departamento");
        }
    }

    /**
     * Metodo de borrado de departamento por id
     * @param id Id del Departamento
     */
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        LOGGER.info("Peticion de modificacion de dapartamento");
        try {
            LOGGER.info("Borrandeo departamento");
            ejb.deleteDepartment(id);
            LOGGER.info("Departamento borrado con exito");
        } catch (DeleteException ex) {
            LOGGER.warning("ERROR a la hora de borrar el departamento");
            throw new InternalServerErrorException("ERROR a la hora de borrar el departamento");

        }
    }

    /**
     * Metodo de obtencion de un departamento
     * @param id Id de dicho departamento
     * @return un objeto del tipo Department
     * @throws InternalServerErrorException .Si no existe el departamento
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Department find(@PathParam("id") Integer id) throws InternalServerErrorException {
        LOGGER.info("Peticion de departamento");
        Department department = null;
        try {
            LOGGER.info("Busqueda de departamento");
            department = ejb.getDepartmentProfile(id);
            LOGGER.info("Departamento encontrado");
        } catch (SelectException ex) {
            Logger.getLogger(CompanyFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("No existe departamento con esta id en la base de datos");
        }
        LOGGER.info("Respuesta de objeto department");
        return department;
    }

    /**
     * Metodo que devuelve una lista se departamentos
     * @return lista de Department
     * @throws InternalServerErrorException .Si no hay departamentos en la base de datos
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public Set<Department> FindAllDepartment() throws InternalServerErrorException {
        LOGGER.info("Peticion de todos los departamentos");
        Set<Department> ret = null;
        try {
            LOGGER.info("Obteniendo lista de departamentos");
            ret = ejb.getDepartmentList();
            
        } catch (GetCollectionException ex) {
            Logger.getLogger(DepartmentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("No hay departamentos en la base de datos");
        }
        LOGGER.info("Respuesta lista de departamentos ");
        return ret;
    }

}
