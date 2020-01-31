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
    /**
     * Interfaz que contiene los metodos del EJBUser
     * @param user objeto User
     * @throws CreateException  si el usuario no puede ser creado
     * @throws UpdateException si el usuario no puede ser modificado
     * @throws MessagingException  si no se puede mandar el mail
     */
    public void createUser(User user) throws CreateException, UpdateException, MessagingException;

      /**
       * Interfaz que contiene los metodos de modificacion de user
       * @param user el usuaeio a modificar
       * @throws UpdateException  si el usuario no puede ser modificado
       */
    public void updateUser(User user) throws UpdateException;
    /**
     * Interfaz que contiene el metodo de login de usuario
     * @param user el usuario a loguearse
     * @return el usuario logueado
     * @throws LoginException si el login es incorrecto
     * @throws LoginPasswordException si la contraseña es incorrecta
     * @throws DisabledUserException  si el usuario esta deshabilitado
     */

    public User login(User user) throws LoginException, LoginPasswordException, DisabledUserException;
    /**
     * interfaz que devuelve una lista de usuarios
     * @return lista de usuarios
     * @throws GetCollectionException  si no hay datos en la base de datos
     */
    public Set<User> getUserList() throws GetCollectionException;
    /**
     * Interfaz que contiene el metodo de recuperacion de contraseña
     * @param user usuario a recuperar la contraseña
     * @throws RecoverPasswordException si la contraseña no se puede recuperar
     * @throws SelectException  si el usuario no existe
     */
    public void recoverPassword(User user) throws RecoverPasswordException, SelectException;
    /**
     * Interfaz que contiene el metodo de eliminacion de un usuario
     * @param user el usuario a borrar
     * @throws DeleteException  si el usuario no se puede borrar
     */
    public void deleteUser(User user) throws DeleteException;
    /**
     * Interfaz que busca un usuario por id
     * @param id el id del usuario a buscar
     * @return el usuario cuyo id se ha introducido 
     * @throws SelectException  si el usuario no se encuentra
     */
    public User findUserById(int id) throws SelectException;
    /**
     * Interfaz que desactiva un usuario si su compañia ha sido borrada
     * @param company_id el id de la compañia borrada
     * @throws UpdateException si no se puede actualizar el dato
     */
    public void disabledUserByCompany(int company_id) throws UpdateException;
    /**
     * Interfza para recoger la clave publica 
     * @return la clave publica
     * @throws GenericServerException si no se encuentra la clave
     */
    public String getPublicKey() throws GenericServerException;
}
