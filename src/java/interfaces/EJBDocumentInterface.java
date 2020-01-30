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

    public void createNewDocument(Document document) throws CreateException;

    public void updateDocument(Document document) throws UpdateException;

    public void deleteDocument(Document document) throws DeleteException;

    public Set<Document> getDocumentList() throws GetCollectionException;

    public Document findDocumentById(int id) throws SelectException;

    public void updateDocumentByUser(int user_id) throws UpdateException;
}
