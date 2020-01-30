/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entitiesJPA.User;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.DisabledUserException;
import exceptions.GetCollectionException;
import exceptions.LoginException;
import exceptions.LoginPasswordException;
import exceptions.RecoverPasswordException;
import exceptions.SelectException;
import exceptions.UpdateException;
import interfaces.EJBDocumentInterface;
import interfaces.EJBUserInterface;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.resource.spi.UnavailableException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Fran
 */
@Path("user")
public class UserFacadeREST {

    @EJB
    private EJBUserInterface ejb;

    @EJB
    private EJBDocumentInterface ejbDoc;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public User find(@PathParam("id") int id) throws InternalServerErrorException {
        User ret = null;
        try {
            ret = ejb.findUserById(id);
        } catch (SelectException ex) {
            throw new InternalServerErrorException("No hay usuario con esa id en la base de datos.");
        }
        return ret;
    }

    /**
     *
     * @param login
     * @param password
     * @return
     */
    @GET
    @Path("{login}/{password}") //PARA LOGIN
    @Produces({MediaType.APPLICATION_XML})
    public User login(@PathParam("login") String login, @PathParam("password") String password) throws InternalServerErrorException {
        User ret = new User();
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        try {
            ret = ejb.login(user);
        } catch (LoginException ex) {
            throw new NotAuthorizedException("Login de usuario incorrecto.");
        } catch (LoginPasswordException ex) {
            throw new NotFoundException("Contraseña incorrecta.");
        } catch (DisabledUserException ex) {
            throw new InternalServerErrorException("Usuario no disponible, consulte con su empresa/entidad.");
        }
        return ret;
    }

    /**
     *
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public Set<User> findAll() throws InternalServerErrorException {
        Set<User> collection = null;
        try {
            collection = ejb.getUserList();
        } catch (GetCollectionException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException(ex.getMessage());
        }
        return collection;
    }

    /**
     *
     * @param email
     */
    @PUT
    @Path("{email}") //buscar contraseña por email apra enviar por correo
    @Consumes({MediaType.APPLICATION_XML})
    public void recoverPassword(User user) throws InternalServerErrorException {
        try {
            ejb.recoverPassword(user);
        } catch (RecoverPasswordException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("Fallo al enviar email, compruebe que el email existe.");
        } catch (SelectException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException("Fallo al enviar email, compruebe que el email existe.");
        }
    }

    /**
     *
     * @param user
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(User user) throws InternalServerErrorException {
        try {
            ejb.createUser(user);
        } catch (CreateException ex) {
            throw new InternalServerErrorException("Error al dar de alta al usuario.");
        } catch (UpdateException ex) {
            throw new NotAuthorizedException("Login y/o email ya existen en la base de datos.");
        } catch (MessagingException ex) {
            throw new NotFoundException("Error al enviar el email a su correo electrónico.");
        }
    }

    /**
     *
     * @param user
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(User user) throws InternalServerErrorException {
        try {
            ejb.updateUser(user);
        } catch (UpdateException ex) {
            throw new InternalServerErrorException();
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) throws InternalServerErrorException {
        User user = new User();
        user.setId(id);
        try {
            ejbDoc.updateDocumentByUser(id);
            ejb.deleteUser(user);
        } catch (DeleteException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException(ex.getMessage());
        } catch (UpdateException ex) {
            Logger.getLogger(UserFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("/getPublicKey")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPublicKey() {
        String publicKey;
        try {
            publicKey = ejb.getPublicKey();
        } catch (Exception ex) {
            // LOGGER.warning("RESTUser: " + ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        return publicKey;
    }

}
