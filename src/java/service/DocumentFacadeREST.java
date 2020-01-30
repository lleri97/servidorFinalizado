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
 *
 * @author 2dam
 */
@Path("document")
public class DocumentFacadeREST {

    @EJB(beanName = "EJBDocument")
    private EJBDocumentInterface ejb;

    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void createNewDocument(Document document) {
        try {
            ejb.createNewDocument(document);
        } catch (CreateException ex) {
            Logger.getLogger(DocumentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void updateDocument(Document document) {
        try {
            ejb.updateDocument(document);
        } catch (UpdateException ex) {
            Logger.getLogger(DocumentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        Document document = new Document();
        document.setId(id);
        try {
            ejb.deleteDocument(document);
        } catch (DeleteException ex) {
            Logger.getLogger(DocumentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Document find(@PathParam("id") int id) throws InternalServerErrorException {
        Document ret = null;
        try {
            ret = ejb.findDocumentById(id);
        } catch (SelectException e) {
            throw new InternalServerErrorException("No existe documento con esta id en la base de datos.");
        }
        return ret;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public Set<Document> findAll() {
        Set<Document> collection = null;
        try {
            collection = ejb.getDocumentList();
        } catch (GetCollectionException ex) {
            Logger.getLogger(DocumentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return collection;
    }

}
