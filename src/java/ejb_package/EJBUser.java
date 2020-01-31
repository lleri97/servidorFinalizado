/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb_package;

import entitiesJPA.User;
import entitiesJPA.UserStatus;
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
import utils.MailSender;
import interfaces.EJBUserInterface;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import utils.EncryptionServerClass;

/**
 *
 * @author Francisco Romero Alonso
 */
@Stateless
public class EJBUser implements EJBUserInterface {
    
    private static final Logger LOGGER = Logger.getLogger(EJBUser.class.getPackage() + "." + EJBUser.class.getName());

    @PersistenceContext(unitName = "grupo5_ServerPU")
    private EntityManager em;

/**
 * Este metodo modifica un usuario dado en la base de datos
 * @param user El usuario con los nuevos datos
 * @throws UpdateException Si la modificación falla
 */
    @Override
    public void updateUser(User user) throws UpdateException {
        try {
            LOGGER.info("Iniciando modificación de usuario.");
            if (user.getPassword().equalsIgnoreCase("")) {
                //sacar contraseña de la base de datos y cargar la que tenia
                Query q1 = em.createQuery("Select a from User a where a.id=:id");
                q1.setParameter("id", user.getId());
                User userPassw = (User) q1.getSingleResult();
                user.setPassword(userPassw.getPassword());

            } else {
                //cargamos la nueva
                EncryptionServerClass encryp = new EncryptionServerClass();
                String notEncodedPassword = encryp.decryptText(user.getPassword());

                String passwordHashDB = encryp.hashingText(notEncodedPassword);
                user.setPassword(passwordHashDB);
                LocalDateTime localDate = LocalDateTime.now();
                Date date = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
                user.setLastPassWordChange(date);
            }
            checkLoginAndEmail(user);
            em.merge(user);
            LOGGER.info("Usuario modificaco con exito.");
        } catch (Exception e) {
            throw new UpdateException("Error al modificar el usuario.");
        }
    }

    /**
     * Este metodo busca un usuario por id
     * @param id La id del usuario a buscar
     * @return Retorna el usuario con todos los datos
     * @throws SelectException Si falla la busqueda del usuario
     */
    public User findUserById(int id) throws SelectException {
        User ret = null;
        try {
            LOGGER.info("Buscando perfil de usuario por id.");
            ret = (User) em.createNamedQuery("findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            throw new SelectException("Error al buscar el usuario por id.");
        }
        LOGGER.info("Perfil cargado con éxito.");
        return ret;
    }

   /**
    * Metodo para comprobar credenciales al iniciar sesión
    * @param user Usuario que contiene la contraseña y el login a comprobar
    * @return Retorna el usuario con todos los datos cargados en caso de exito
    * @throws LoginException Si el login introducido no existe
    * @throws LoginPasswordException Si la contraseña introducida no es correta
    * @throws DisabledUserException Si el usuario está deshabilitado
    */
    @Override
    public User login(User user) throws LoginException, LoginPasswordException, DisabledUserException {
        User ret = new User();
        try {
            LOGGER.info("Iniciando comprobación de credenciales.");
            EncryptionServerClass encryp = new EncryptionServerClass();
            LOGGER.info("Desencriptando contraseña");
            String notEncodedPassword = encryp.decryptText(user.getPassword());
            LOGGER.info("Contraseña desencriptada con exito");
            ret = checkUserbyLogin(user);

            UserStatus x = ret.getStatus();
            if (x == UserStatus.ENABLED) {
                LOGGER.info("Usuario habilitado, continuando con la comprobación de credenciales.");
                try {
                    ret = (User) em.createNamedQuery("findByLoginAndPassword").setParameter("login", user.getLogin()).setParameter("password", encryp.hashingText(notEncodedPassword)).getSingleResult();
                    LOGGER.info("La contraseña introducida es correcta.");
                } catch (Exception e) {
                    throw new LoginPasswordException("La contraseña es incorrecta.");
                }
            } else {
                throw new DisabledUserException("Usuario no habilitado, pongase en contacto con su empresa/entiedad para más información.");
            }
            Query q1 = em.createQuery("update User a set a.lastAccess=:dateNow where a.id=:user_id");
            LocalDateTime localDate = LocalDateTime.now();
            Date date = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
            q1.setParameter("dateNow", date);
            q1.setParameter("user_id", ret.getId());
            LOGGER.info("Actualizando fecha de ultimo acceso en la base de datos.");
            q1.executeUpdate();
            String passwordHashDB = encryp.hashingText(user.getPassword());
            user.setPassword(passwordHashDB);

        } catch (Exception ex) {
            LOGGER.info("ERROR al hacer login : " + ex.getMessage());
        }
        return ret;
    }

   /**
    * Metodo que devuelve una lista con los usuarios existentes
    * @return La lista que contiene los usuarios de la base de datos
    * @throws GetCollectionException Si falla al hacer la busqueda y cargar los usuarios
    */
    @Override
    public Set<User> getUserList() throws GetCollectionException {
        List<User> listUser = null;
        try {
            LOGGER.info("Buscando usuarios en la base de datos.");
            listUser = em.createNamedQuery("findAllUsers").getResultList();
        } catch (Exception ex) {
            LOGGER.warning("Error al buscar usuarios en la base de datos");
            throw new GetCollectionException(ex.getMessage());
        }
        Set<User> ret = new HashSet<User>(listUser);
        return ret;
    }

    /**
     * Metodo para recuperar contraseña de usuario mediante correo electronico
     * @param user Usuario que contiene el correo electronico
     * @throws RecoverPasswordException Si falla la recuperación de contraseña
     */
    @Override
    public void recoverPassword(User user) throws RecoverPasswordException {
        try {
            EncryptionServerClass hash = new EncryptionServerClass();
            LOGGER.info("Recuperando contraseña encriptada desde la base de datos.");
            String passwordHashDB = (String) em.createNamedQuery("recoverPassword")
                    .setParameter("email", user.getEmail()).getSingleResult();
            //generamos nueva contraseña
            LOGGER.info("Generando nueva contraseña.");
            String notEncodedNew = generatePassword();
            LOGGER.info("Codificando contraseña generada.");
            passwordHashDB = hash.hashingText(notEncodedNew);
            user.setPassword(passwordHashDB);

            //ENVIAR CORREO
            MailSender emailService = new MailSender(ResourceBundle.getBundle("files.MailSenderConfig").getString("SenderName"),
                    ResourceBundle.getBundle("files.MailSenderConfig").getString("SenderPassword"), null, null);
            try {
                LOGGER.info("Enviando email con la nueva contraseña al usuario.");
                emailService.sendMail(ResourceBundle.getBundle("files.MailSenderConfig").getString("SenderEmail"),
                        user.getEmail(),
                        ResourceBundle.getBundle("files.MailSenderConfig").getString("MessageSubject"),
                        ResourceBundle.getBundle("files.MailSenderConfig").getString("MessageEmail1")
                        + notEncodedNew
                        + ResourceBundle.getBundle("files.MailSenderConfig").getString("MessageEmail2"));
                LOGGER.info("Contraseña enviada con exito.");
            } catch (MessagingException ex) {
                LOGGER.warning("Fallo al enviar contraseña.");
                throw new RecoverPasswordException(ex.getMessage());
            }
            //actualizamos en la base de datos
            LOGGER.info("Actualizando la base de datos con la nueva contraseña.");
            Query q1 = em.createQuery("update User a set a.password=:password where a.email=:email");
            q1.setParameter("password", passwordHashDB);
            q1.setParameter("email", user.getEmail());
            q1.executeUpdate();
            LOGGER.info("Recuperación de contraseña realizada con exito.");
        } catch (Exception ex) {
            throw new RecoverPasswordException("Error al recuperar contraseña: " + ex.getMessage());
        }
    }

    /**
     * Metodo que elimina un usuario de la base de datos
     * @param user Usuario que será eliminado de la bse de datos
     * @throws DeleteException Si falla el borrado del usuario
     */
    @Override
    public void deleteUser(User user) throws DeleteException {
        try {
            LOGGER.info("Borrando usuario");
            Query q1 = em.createNamedQuery("DeleteUser").setParameter("id", user.getId());
            q1.executeUpdate();
            em.flush();
            LOGGER.info("Usuario borrado con exito.");
        } catch (Exception ex) {
            throw new DeleteException("Error al borrar usuario: " + ex.getMessage());
        }
    }

    /**
     * Metodo que crea un nuevo registro de usuario en la base de datos
     * @param user Usuario que será creado
     * @throws CreateException Si falla al introducir los datos en l abase de datos
     * @throws UpdateException Si el email o login ya existen en la base de datos
     * @throws MessagingException Si falla el envío de las credenciales al usuario por email
     */
    @Override
    public void createUser(User user) throws CreateException, UpdateException, MessagingException {
        try {
            LOGGER.info("Comprobando login y email con la base de datos.");
            checkLoginAndEmail(user);
        } catch (UpdateException ex) {
            LOGGER.warning("El login/Email ya existen.");
            throw new UpdateException(ex.getMessage());
        }
        LOGGER.info("Encriptando contraseña.");
        EncryptionServerClass hash = new EncryptionServerClass();
        String notHashPassword = generatePassword();
        String hashPassword = hash.hashingText(notHashPassword);
        user.setPassword(hashPassword);
        LOGGER.info("Guardando usuario en la base de datos.");
        try {
            byte[] photo = user.getPhoto();
            user.setPhoto(null);
            em.persist(user);
            Query q1 = em.createQuery("update User a set a.photo=:photo where a.id=:id");
            q1.setParameter("photo", photo);
            q1.setParameter("id", user.getId());
            q1.executeUpdate();
            LOGGER.info("Usuario guardado en la base de datos con exito");
        } catch (Exception e) {
            LOGGER.warning("Error al introducir nuevo usuario en la base de datos.");
            throw new CreateException(e.getMessage());
        }
        MailSender emailService = new MailSender(ResourceBundle.getBundle("files.MailSenderConfig").getString("SenderName"),
                ResourceBundle.getBundle("files.MailSenderConfig").getString("SenderPassword"), null, null);
        try {
            LOGGER.info("Enviando email con contraseña al nuevo usuario.");
            emailService.sendMail(ResourceBundle.getBundle("files.MailSenderConfig").getString("SenderEmail"),
                    user.getEmail(),
                    ResourceBundle.getBundle("files.MailSenderConfig").getString("NewUserMessageSubject"),
                    ResourceBundle.getBundle("files.MailSenderConfig").getString("NewUserMessageEmail1")
                    + "" + notHashPassword
                    + ResourceBundle.getBundle("files.MailSenderConfig").getString("NewUserMessageEmail2"));
        } catch (Exception ex) {
            LOGGER.severe("Fallo al enviar el email");
            Logger.getLogger(EJBUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOGGER.info("Email enviado con exito");
    }

    /**
     * Metodo usado para deshabilitar los usuarios de una compañia borrada
     * @param company_id compañia que ha sido borrada
     * @throws UpdateException Si falla el proceso de update
     */
    public void disabledUserByCompany(int company_id) throws UpdateException {
        try {
            LOGGER.info("Actualizando status de usuario.");
            Query q1 = em.createQuery("update User a set a.status='DISABLED',a.company.id=NULL where a.company.id=:company_id");
            q1.setParameter("company_id", company_id);
            q1.executeUpdate();
        } catch (Exception ex) {
            throw new UpdateException(ex.getMessage());
        }
        LOGGER.info("Usuario actualizado con exito.");
    }

    /**
     * Metodo que genera una nueva contraseña aleatoria
     * @return Una nueva contraseña
     */
    private String generatePassword() {
        Random random = new Random();
        String alphabet = "0123456789abcdfghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWYXZ";
        StringBuilder notEncodedNew = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            notEncodedNew.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return new String(notEncodedNew);
    }

    /**
     * Metodo que comprueba si un login existe en la base de datos
     * @param user usuario con el login a comprobar
     * @return Retorna el usuario si el login existe
     * @throws LoginException Si no existe
     */
    private User checkUserbyLogin(User user) throws LoginException {
        LOGGER.info("Comprobando que el login existe en la base de datos.");
        User ret = new User();
        try {
            ret = (User) em.createNamedQuery("findByLogin").setParameter("login", user.getLogin()).getSingleResult();
        } catch (NoResultException e) {
            throw new LoginException("El login no existe.");
        }
        LOGGER.info("El login existe.");
        return ret;
    }

    /**
     * Metodo que comprueba credenciales estan repetidos
     * @param user usuario con login y contrasña que van a comprobarse
     * @throws UpdateException Si el login o contraseña estan repetidos
     */
    private void checkLoginAndEmail(User user) throws UpdateException {
        Long comparador = new Long(0);
        //comprobamos si hay usuarios con el login y email que nos pasan
        Query q1 = em.createQuery("Select count(a) from User a where a.login=:login and a.id!=:id");
        q1.setParameter("login", user.getLogin());
        q1.setParameter("id", user.getId());
        Object loginRepe = q1.getSingleResult();
        if (!loginRepe.equals(comparador)) {
            throw new UpdateException("Login repetido. Elija otro.");
        }
        //comprobar email
        Query q2 = em.createQuery("Select count(a) from User a where a.email=:email and a.id!=:id");
        q2.setParameter("email", user.getEmail());
        q2.setParameter("id", user.getId());
        Object emailRepe = q2.getSingleResult();
        if (!emailRepe.equals(comparador)) {
            throw new UpdateException("Email repetido. Elija otro.");
        }
    }

    /**
     * Metodo para enviar la clave publica al cliente que lo solicite
     * @return Retorna un string con la clave publica
     * @throws GenericServerException Si hay un fallo al recuperar la clave publica
     */
    @Override
    public String getPublicKey() throws GenericServerException {
        String publicKey = "";
        try {
            publicKey = EncryptionServerClass.getPublic();
        } catch (IOException ex) {
        }
        return publicKey;
    }

}
