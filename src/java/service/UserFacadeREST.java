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
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.mail.MessagingException;
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
 * @author Francisco Romero Alonso
 */
@Path("user")
public class UserFacadeREST {

    private static final Logger LOGGER = Logger.getLogger(UserFacadeREST.class.getPackage() + "." + UserFacadeREST.class.getName());

    @EJB
    private EJBUserInterface ejb;

    @EJB
    private EJBDocumentInterface ejbDoc;

    /**
     * Metodo Rest de busqueda de un usuario por id
     * @param id Id del usuario a buscar
     * @return Retorna un usuario con los datos
     * @throws InternalServerErrorException  Si hay error al realizar la busqueda
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public User find(@PathParam("id") int id) throws InternalServerErrorException {
        LOGGER.info("Peticion de busqueda por id recibida con exito");
        User ret = null;
        try {
            ret = ejb.findUserById(id);
        } catch (SelectException ex) {
            LOGGER.warning("ERROR a la hora de encontrar un usuario");
            throw new InternalServerErrorException("No hay usuario con esa id en la base de datos.");
        }
        LOGGER.info("Enviando usuario.");

        return ret;
    }

    /**
     * 
     * @param login tipo string
     * @param password tipo password
     * @return usuario
     * @throws InternalServerErrorException  si no se encuyentran datos
     */
    @GET
    @Path("{login}/{password}") //PARA LOGIN
    @Produces({MediaType.APPLICATION_XML})
    public User login(@PathParam("login") String login, @PathParam("password") String password) throws InternalServerErrorException {
        LOGGER.info("Peticion de login recibida con exito");

        User ret = new User();
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);

        try {
            ret = ejb.login(user);
        } catch (LoginException ex) {
            LOGGER.warning("Login incorrecto");
            throw new NotAuthorizedException("Login de usuario incorrecto.");
        } catch (LoginPasswordException ex) {
            LOGGER.warning("Coontraseña incorrecta");
            throw new NotFoundException("Contraseña incorrecta.");
        } catch (DisabledUserException ex) {
            LOGGER.warning("Usuario no disponible, consulte con su empresa/entidad.");
            throw new InternalServerErrorException("Usuario no disponible, consulte con su empresa/entidad.");
        }
        LOGGER.info("Enviando perfil de usuario");

        return ret;
    }

    /**
     *
     * @return coleccion
     */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public Set<User> findAll() throws InternalServerErrorException {
        LOGGER.info("Peticion de Lista de usuarios recibida con exito");

        Set<User> collection = null;
        try {
            collection = ejb.getUserList();
        } catch (GetCollectionException ex) {
            LOGGER.warning("ERROR a la hora de acceder a la Lista de usuarios");
            throw new InternalServerErrorException("<lista de usuarios no disponible, consulte con su empresa/entidad.d");
        }

        LOGGER.info("Enviando lista de usuarios.");

        return collection;
    }

    /**
     *
     * @param user  tipo user
     */
    @PUT
    @Path("{email}") //buscar contraseña por email apra enviar por correo
    @Consumes({MediaType.APPLICATION_XML})
    public void recoverPassword(User user) throws InternalServerErrorException {
        LOGGER.info("Peticion de recuperacion de contraseña recibida con exito");

        try {
            ejb.recoverPassword(user);
        } catch (RecoverPasswordException ex) {
            LOGGER.warning("ERROR a la hora de recuperar la contraseña");
            throw new InternalServerErrorException("Fallo al enviar email, compruebe que el email existe.");
        } catch (SelectException ex) {
            LOGGER.warning("ERROR a la hora de recuperar la contraseña");
            throw new InternalServerErrorException("Fallo al enviar email, compruebe que el email existe.");
        }
    }

    /**
     *
     * @param user tipo user
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    public void create(User user) throws InternalServerErrorException {
        LOGGER.info("Peticion de creacion de usuario recibida con exito");

        try {
            ejb.createUser(user);
        } catch (CreateException ex) {
            LOGGER.warning("Error al dar de alta al usuario.");
            throw new InternalServerErrorException("Error al dar de alta al usuario.");
        } catch (UpdateException ex) {
            LOGGER.warning("Login y/o email ya existen en la base de datos.");
            throw new NotAuthorizedException("Login y/o email ya existen en la base de datos.");
        } catch (MessagingException ex) {
            LOGGER.warning("Error al enviar el email a su correo electrónico.");
            throw new NotFoundException("Error al enviar el email a su correo electrónico.");
        }
    }

    /**
     *
     * @param user tipo user
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(User user) throws InternalServerErrorException {
        LOGGER.info("Peticion de edicion de usuario recibida con exito");

        try {
            ejb.updateUser(user);
        } catch (UpdateException ex) {
            LOGGER.warning("Error a la hora de actualizar el usuario");
            throw new InternalServerErrorException("Error al actualizar el usuario");
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) throws InternalServerErrorException {
        LOGGER.info("Peticion de borrado de usuario recibida con exito");

        User user = new User();
        user.setId(id);
        try {
            ejbDoc.updateDocumentByUser(id);
            ejb.deleteUser(user);

        } catch (DeleteException ex) {
            LOGGER.warning("Error a la hora de borrar el usuario");
            throw new InternalServerErrorException(ex.getMessage());
        } catch (UpdateException ex) {
            LOGGER.warning("Error a la hora de borrar el usuario");
            throw new InternalServerErrorException(ex.getMessage());
        }
    }

    @GET
    @Path("/getPublicKey")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPublicKey() {
        LOGGER.info("Peticion de clave publica recibida con exito");

        String publicKey;
        try {
            publicKey = ejb.getPublicKey();
        } catch (Exception ex) {
            LOGGER.warning("Error a la hora de obtener la clave publica");
            throw new InternalServerErrorException(ex.getMessage());
        }

        LOGGER.info("Respuesta de clave publica recibida con exito");

        return publicKey;
    }

}
