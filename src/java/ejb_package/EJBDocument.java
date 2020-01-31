/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb_package;

import entitiesJPA.Document;
import entitiesJPA.User;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.GetCollectionException;
import exceptions.SelectException;
import exceptions.UpdateException;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import interfaces.EJBDocumentInterface;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Fran
 */
@Stateless
public class EJBDocument implements EJBDocumentInterface {

    @PersistenceContext(unitName = "grupo5_ServerPU")
    private EntityManager em;

    /**
     * Metodo de obtencion de una lista de documentos
     *
     * @return retorna la lista de todos los documentos
     * @throws GetCollectionException si no hay datos que sacar
     */
    public Set<Document> getDocumentList() throws GetCollectionException {
        List<Document> listDocument = null;
        try {
            listDocument = em.createNamedQuery("findAllDocuments").getResultList();
        } catch (Exception ex) {
            throw new GetCollectionException(ex.getMessage());
        }
        Set<Document> ret = new HashSet<Document>(listDocument);
        return ret;
    }

    /**
     * Metodo que encuentra un documento por su id
     *
     * @param id Id del documento
     * @return un documetno con toda su informacion y contenido
     * @throws SelectException Si no existe el documento
     */
    public Document findDocumentById(int id) throws SelectException {
        Document ret = null;
        try {
            ret = (Document) em.createNamedQuery("findDocumentById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            throw new SelectException();
        }
        return ret;
    }

    /**
     * Metodo de creacion de un documento
     *
     * @param document objeto del tipo Document
     * @throws CreateException Si no se pudo hacer persistente el documento
     */
    @Override
    public void createNewDocument(Document document) throws CreateException {
        try {
            LocalDateTime localDate = LocalDateTime.now();
            Date date = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
            document.setUploadDate(date);
            em.persist(document);
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    /**
     * Metodo de actualizacion de documento
     *
     * @param document Objeto del tipo document
     * @throws UpdateException si no se puede modificar
     * 
     */
    public void updateDocument(Document document) throws UpdateException {
        try {
            em.merge(document);
            em.flush();// actualiza al momento base de datos
        } catch (Exception ex) {
            throw new UpdateException(ex.getMessage());
        }
    }
    /**
     * Metodo de actualizacion de documento
     *
     * @param user_id Objeto del tipo int
     * @throws UpdateException si no se puede modificar
     */
    public void updateDocumentByUser(int user_id) throws UpdateException {
        try {
            Query q = em.createQuery("Update Document a set a.user.id=1 where a.user.id=:user_id");
            q.setParameter("user_id", user_id);
            q.executeUpdate();
        } catch (Exception e) {
            throw new UpdateException(e.getMessage());
        }
    }

    /**
     * Metodo rest que borra un documento
     *
     * @param document documento a borrar
     * @throws DeleteException si no se puede borrar
     */
    public void deleteDocument(Document document) throws DeleteException {
        try {
            Query q1 = em.createQuery("delete from Document a where a.id=:id");
            q1.setParameter("id", document.getId());
            q1.executeUpdate();
            em.flush();
        } catch (Exception ex) {
            throw new DeleteException(ex.getMessage());
        }
    }
}
