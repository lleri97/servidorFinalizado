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
 * @author Fran
 */
@Stateless
public class EJBUser implements EJBUserInterface {

    @PersistenceContext(unitName = "grupo5_ServerPU")
    private EntityManager em;

    /**
     *
     * @param user
     * @throws UpdateException
     */
    @Override
    public void updateUser(User user) throws UpdateException {
        try {
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
        } catch (Exception e) {
            throw new UpdateException();
        }
    }

    /**
     *
     * @param id
     * @return
     * @throws SelectException
     */
    public User findUserById(int id) throws SelectException {
        User ret = null;
        try {
            ret = (User) em.createNamedQuery("findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            throw new SelectException();
        }
        return ret;
    }

    /**
     *
     * @param user
     * @return
     * @throws LoginException
     * @throws LoginPasswordException
     * @throws DisabledUserException
     */
    @Override
    public User login(User user) throws LoginException, LoginPasswordException, DisabledUserException {
        User ret = new User();
        try {

            EncryptionServerClass encryp = new EncryptionServerClass();
            String notEncodedPassword = encryp.decryptText(user.getPassword());
            ret = checkUserbyLogin(user);

            UserStatus x = ret.getStatus();
            if (x == UserStatus.ENABLED) {
                try {
                    ret = (User) em.createNamedQuery("findByLoginAndPassword").setParameter("login", user.getLogin()).setParameter("password", encryp.hashingText(notEncodedPassword)).getSingleResult();
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
            q1.executeUpdate();
            String passwordHashDB = encryp.hashingText(user.getPassword());
            user.setPassword(passwordHashDB);

        } catch (Exception ex) {
            Logger.getLogger(EJBUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    /**
     *
     * @return @throws GetCollectionException
     */
    @Override
    public Set<User> getUserList() throws GetCollectionException {
        List<User> listUser = null;
        try {
            listUser = em.createNamedQuery("findAllUsers").getResultList();
        } catch (Exception ex) {
            throw new GetCollectionException(ex.getMessage());
        }
        Set<User> ret = new HashSet<User>(listUser);
        return ret;
    }

    /**
     *
     * @param user
     * @throws RecoverPasswordException
     */
    @Override
    public void recoverPassword(User user) throws RecoverPasswordException {
        try {
            EncryptionServerClass hash = new EncryptionServerClass();
            String passwordHashDB = (String) em.createNamedQuery("recoverPassword")
                    .setParameter("email", user.getEmail()).getSingleResult();
            //generamos nueva contraseña
            String notEncodedNew = generatePassword();

            passwordHashDB = hash.hashingText(notEncodedNew);
            user.setPassword(passwordHashDB);

            //ENVIAR CORREO
            MailSender emailService = new MailSender(ResourceBundle.getBundle("files.MailSenderConfig").getString("SenderName"),
                    ResourceBundle.getBundle("files.MailSenderConfig").getString("SenderPassword"), null, null);
            try {
                emailService.sendMail(ResourceBundle.getBundle("files.MailSenderConfig").getString("SenderEmail"),
                        user.getEmail(),
                        ResourceBundle.getBundle("files.MailSenderConfig").getString("MessageSubject"),
                        ResourceBundle.getBundle("files.MailSenderConfig").getString("MessageEmail1")
                        + notEncodedNew
                        + ResourceBundle.getBundle("files.MailSenderConfig").getString("MessageEmail2"));
                System.out.println("Ok, mail sent!");
            } catch (MessagingException ex) {
                throw new RecoverPasswordException(ex.getMessage());
            }
            //actualizamos en la base de datos
            Query q1 = em.createQuery("update User a set a.password=:password where a.email=:email");
            q1.setParameter("password", passwordHashDB);
            q1.setParameter("email", user.getEmail());
            q1.executeUpdate();
        } catch (Exception ex) {
            throw new RecoverPasswordException(ex.getMessage());
        }
    }

    /**
     *
     * @param user
     * @throws DeleteException
     */
    @Override
    public void deleteUser(User user) throws DeleteException {
        try {
            Query q1 = em.createNamedQuery("DeleteUser").setParameter("id", user.getId());
            q1.executeUpdate();
            em.flush();
        } catch (Exception ex) {
            throw new DeleteException(ex.getMessage());
        }
    }

    /**
     *
     * @param user
     * @throws CreateException
     */
    @Override
    public void createUser(User user) throws CreateException, UpdateException, MessagingException {
        try {
            checkLoginAndEmail(user);
        } catch (UpdateException ex) {
            Logger.getLogger(EJBUser.class.getName()).log(Level.SEVERE, null, ex);
            throw new UpdateException();
        }
        EncryptionServerClass hash = new EncryptionServerClass();
        String notHashPassword = generatePassword();
        String hashPassword = hash.hashingText(notHashPassword);
        user.setPassword(hashPassword);
        try {
            byte[] photo = user.getPhoto();
            user.setPhoto(null);
            em.persist(user);
            Query q1 = em.createQuery("update User a set a.photo=:photo where a.id=:id");
            q1.setParameter("photo", photo);
            q1.setParameter("id", user.getId());
            q1.executeUpdate();
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
        try {
            MailSender emailService = new MailSender(ResourceBundle.getBundle("files.MailSenderConfig").getString("SenderName"),
                    ResourceBundle.getBundle("files.MailSenderConfig").getString("SenderPassword"), null, null);
            emailService.sendMail(ResourceBundle.getBundle("files.MailSenderConfig").getString("SenderEmail"),
                    user.getEmail(),
                    ResourceBundle.getBundle("files.MailSenderConfig").getString("NewUserMessageSubject"),
                    ResourceBundle.getBundle("files.MailSenderConfig").getString("NewUserMessageEmail1")
                    + "" + notHashPassword
                    + ResourceBundle.getBundle("files.MailSenderConfig").getString("NewUserMessageEmail2"));
            System.out.println("Ok, mail sent!");
        } catch (MessagingException e) {
            System.out.println("Doh!");
            throw new MessagingException();
        }
    }

    /**
     *
     * @param company_id
     * @throws UpdateException
     */
    public void disabledUserByCompany(int company_id) throws UpdateException {
        try {
            
            Query q1 = em.createQuery("update User a set a.status='DISABLED',a.company.id=NULL where a.company.id=:company_id");
            q1.setParameter("company_id", company_id);
            q1.executeUpdate();
        } catch (Exception ex) {
            throw new UpdateException(ex.getMessage());
        }
    }

    /**
     *
     * @return
     */
    private String generatePassword() {
        Random random = new Random();
        String alphabet = "0123456789abcdfghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWYXZ";
        StringBuilder notEncodedNew = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            notEncodedNew.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
//String notEncodedNew = new Random().ints(10, 33, 122).collect(StringBuilder::new,
        //StringBuilder::appendCodePoint, StringBuilder::append)
        // .toString();
        return new String(notEncodedNew);
    }

    /**
     *
     * @param user
     * @return
     * @throws LoginException
     */
    private User checkUserbyLogin(User user) throws LoginException {
        User ret = new User();
        try {
            ret = (User) em.createNamedQuery("findByLogin").setParameter("login", user.getLogin()).getSingleResult();
        } catch (NoResultException e) {
            throw new LoginException("El login no existe.");
        }
        return ret;
    }

    /**
     *
     * @param user
     * @throws UpdateException
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
