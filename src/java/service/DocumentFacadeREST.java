/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entitiesJPA.Document;
import entitiesJPA.User;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.GetCollectionException;
import exceptions.SelectException;
import exceptions.UpdateException;
import interfaces.EJBDocumentInterface;
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
 * Clase Rest de documentos
 *
 * @author Ruben
 */
@Path("document")
public class DocumentFacadeREST {

    private static final Logger LOGGER = Logger.getLogger(DocumentFacadeREST.class.getPackage() + "." + DocumentFacadeREST.class.getName());

    @EJB(beanName = "EJBDocument")
    private EJBDocumentInterface ejb;

    /**
     * Metodo Rest de creacion de un documento
     *
     * @param document objeto del tipo Document
     * @throws InternalServerErrorException .Si no se pudo hacer persistente el
     * documento
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createNewDocument(Document document) throws InternalServerErrorException {
        LOGGER.info("Peticion para creacion de documento recibida");

        try {
            ejb.createNewDocument(document);
        } catch (CreateException ex) {
            LOGGER.warning("ERROR a la hora de crear documento");
            throw new InternalServerErrorException();
        }
    }

    /**
     * Metodo Rest de actualizacion de documento
     *
     * @param document Objeto del tipo document
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void updateDocument(Document document) throws InternalServerErrorException {
        LOGGER.info("Peticion de actualizacion de documento");
        try {
            ejb.updateDocument(document);
        } catch (UpdateException ex) {
            LOGGER.warning("ERROR a la hora de actualizar un documento");
            throw new InternalServerErrorException();
        }
    }

    /**
     * Metodo rest que borra un documento
     *
     * @param id Id del documento
     */
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) throws InternalServerErrorException {
        LOGGER.info("Peticion de borrado de documento");
        Document document = new Document();
        document.setId(id);
        try {
            ejb.deleteDocument(document);
        } catch (DeleteException ex) {
            LOGGER.warning("ERROR a la hora de borrar un documento");
            throw new InternalServerErrorException("Erro al borrar el documento");
    }
}

/**
 * Metodo rest que encuentra un documento por su id
 *
 * @param id Id del documento
 * @return un documetno con toda su informacion y contenido
 * @throws InternalServerErrorException .Si no existe el documento
 */
@GET
        @Path("{id}")
        @Produces({MediaType.APPLICATION_XML})
        public Document find(@PathParam("id") int id) throws InternalServerErrorException {
        
        LOGGER.info("Peticion de busqueda de un documento");
        Document ret = null;
        try {
            ret = ejb.findDocumentById(id);
        } catch (SelectException e) {
            LOGGER.warning("El documento no existe");
            throw new InternalServerErrorException("No existe documento con esta id en la base de datos.");
        }
        LOGGER.info("Respueesta de busqueda correcta");
        return ret;
    }

    /**
     * Metodo de obtencion de una lista de documentos
     *
     * @return retorna la lista de todos los documentos
     */
    @GET
        @Produces({MediaType.APPLICATION_XML})
        public Set<Document> findAll() throws InternalServerErrorException{
        LOGGER.info("Peticion de todos los documentos");
        Set<Document> collection = null;
        try {
            collection = ejb.getDocumentList();
        } catch (GetCollectionException ex) {
            LOGGER.warning("ERROR en la obtencion de la lista de documentos");
            throw new InternalServerErrorException();
        }
        LOGGER.info("Respuesta de obtencion de lista de documentos");
        return collection;
    }
    
}
