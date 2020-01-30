/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entitiesJPA.User;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.DisabledUserException;
import exceptions.GenericServerException;
import exceptions.GetCollectionException;
import exceptions.LoginException;
import exceptions.LoginPasswordException;
import exceptions.RecoverPasswordException;
import exceptions.SelectException;
import exceptions.UpdateException;
import java.util.Set;
import javax.ejb.Local;
import javax.mail.MessagingException;

/**
 *
 * @author 2dam
 */
@Local
public interface EJBUserInterface {

    public void createUser(User user) throws CreateException, UpdateException, MessagingException;

    public void updateUser(User user) throws UpdateException;

    public User login(User user) throws LoginException, LoginPasswordException, DisabledUserException;

    public Set<User> getUserList() throws GetCollectionException;

    public void recoverPassword(User user) throws RecoverPasswordException, SelectException;

    public void deleteUser(User user) throws DeleteException;

    public User findUserById(int id) throws SelectException;

    public void disabledUserByCompany(int company_id) throws UpdateException;
    
    public String getPublicKey() throws GenericServerException;
}
