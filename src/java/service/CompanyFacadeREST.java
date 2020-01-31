/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entitiesJPA.Company;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.GetCollectionException;
import exceptions.SelectException;
import exceptions.UpdateException;
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
import interfaces.EJBCompanyInterface;
import interfaces.EJBUserInterface;
import java.util.Set;
import java.util.logging.Level;

/**
 * Clase que contiene todos los metodos REST de Company
 *
 * @author Ruben
 */
@Path("company")
public class CompanyFacadeREST {

    private static final Logger LOGGER = Logger.getLogger(CompanyFacadeREST.class.getPackage() + "." + CompanyFacadeREST.class.getName());

    @EJB(beanName = "EJBCompany")
    private EJBCompanyInterface ejb;

    @EJB
    private EJBUserInterface ejbUser;

    /**
     * Metodo que crea una compañia
     * @param company objeto del tipo Company
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Company company) {
        try {
            LOGGER.info("Iniciando la creacion de una compañia");
            ejb.createCompany(company);
            LOGGER.info("Compañia creada con exito");
        } catch (CreateException ex) {
            LOGGER.warning("ERROR en la creacion de una compañia");
            throw new InternalServerErrorException("ERROR en la creacion de una compañia");
        }
    }

    /**
     * Metodo de actualizacio de compañia 
     * @param company objeto del tipo Company
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(Company company) {
        try {
            LOGGER.info("Iniciando la modificacion de una compañia");
            ejb.updateCompany(company);
            LOGGER.info("La compañia se actulizo correctamente");
        } catch (UpdateException ex) {
            LOGGER.warning("ERROR en la actualizacion de compañia");
            throw new InternalServerErrorException("ERROR en la actualizacion de compañia");
        }
    }

    /**
     * Metodo que borra una compañia
     * @param id Id de la comapañia
     */
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        try {
            LOGGER.info("Deshabilitando usuarios de esta compañia");
            ejbUser.disabledUserByCompany(id);
            LOGGER.info("Usuarios de la compañia deshabilitados con exito");
            LOGGER.info("Borrando compañia");
            ejb.deleteCompany(id);
            LOGGER.info("Compañia borrada con exito");
        } catch (DeleteException ex) {
            LOGGER.warning("ERROR a la hora de borrar la compañia");
            throw new InternalServerErrorException("ERROR a la hora de borrar la compañia");
        } catch (UpdateException ex) {
            LOGGER.warning("No se pudieron actualizar los usuarios de la compañia");
            throw new InternalServerErrorException("ERROR en la modificaion de usuarios");
        }
    }

    /**
     * Metodo para obtener una compañia
     * @param id Id de la compañia
     * @return objeto del tipo Company
     * @throws InternalServerErrorException  si no se encuentra el usuario
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Company find(@PathParam("id") Integer id) throws InternalServerErrorException {
        Company company = null;
        try {
            LOGGER.info("Cargando compañia");
            company = ejb.getCompanyProfile(id);
        } catch (SelectException ex) {
            LOGGER.info("No hay ninguna compañia con ese id en la base de datos");
            throw new InternalServerErrorException("No hay ninguna compañia con este id en la base de datos.");
        }
        LOGGER.info("Compañia cargada con exito");
        return company;
    }
    /**
     * metodo para obtener una lista de compañias
     * @return lista de compañias
     * @throws InternalServerErrorException  si no hay datos
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public Set<Company> findAll() throws InternalServerErrorException {
        Set<Company> companies = null;
        try {
            LOGGER.info("Cargando lista de compañias");
            companies = ejb.getCompanyList();
        } catch (GetCollectionException ex) {
            LOGGER.info("No hay compañias en la base de datos");
            throw new InternalServerErrorException("No hay compañias en la base de datos.");
        }
        LOGGER.info("Lista de compañias cargada con exito");
        return companies;
    }

}
