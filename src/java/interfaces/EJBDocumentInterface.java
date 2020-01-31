/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entitiesJPA.Document;
import entitiesJPA.User;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.GetCollectionException;
import exceptions.SelectException;
import exceptions.UpdateException;
import java.util.Set;
import javax.ejb.Local;

/**
 *
 * @author Fran
 */
@Local
public interface EJBDocumentInterface {
    /**
     * Metodo  de creacion de un documento
     *
     * @param document objeto del tipo Document
     * @throws CreateException Si no se pudo hacer persistente el
     * documento
     */
    public void createNewDocument(Document document) throws CreateException;
   /**
     * Metodo Rest de actualizacion de documento
     *
     * @param document Objeto del tipo document
     * @throws UpdateException si no se puede actualizar
     */
    public void updateDocument(Document document) throws UpdateException;
    /**
     * Metodo rest que borra un documento
     *
     * @param document documento a borrar
     * @throws DeleteException si no se puede borrar
     */
    public void deleteDocument(Document document) throws DeleteException;
    /**
     * Metodo de obtencion de una lista de documentos
     *
     * @return retorna la lista de todos los documentos
     * @throws GetCollectionException si no hay datos que sacar
     */
    public Set<Document> getDocumentList() throws GetCollectionException;
/**
 * Metodo que encuentra un documento por su id
 *
 * @param id Id del documento
 * @return un documetno con toda su informacion y contenido
 * @throws SelectException Si no existe el documento
 */
    public Document findDocumentById(int id) throws SelectException;
    /**
     * Metodo de actualizacion de documento
     *
     * @param user_id Objeto del tipo document
     * @throws UpdateException si no se puede modificar
     */
    public void updateDocumentByUser(int user_id) throws UpdateException;
}
