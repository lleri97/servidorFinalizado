/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb_package;

import entitiesJPA.Area;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.GetCollectionException;
import exceptions.SelectException;
import exceptions.UpdateException;
import interfaces.EJBAreaInterface;
import java.util.HashSet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Set;
import java.util.logging.Logger;
import javax.persistence.Query;
import service.UserFacadeREST;

/**
 * Clase de gestion de los metodos Rest de Area
 *
 * @author Andoni
 */
@Stateless
public class EJBArea implements EJBAreaInterface {
    
    private static final Logger LOGGER = Logger.getLogger(EJBArea.class.getPackage() + "." + EJBArea.class.getName());

    @PersistenceContext(unitName = "grupo5_ServerPU")
    private EntityManager em;

    /**
     * Metodo que crea un nuevo area
     *
     * @param area Objeto del tipo Area
     * @throws CreateException .Si no se pudo crear un Area
     */
    public void createArea(Area area) throws CreateException {
        LOGGER.info("êticion de creacion de Area");
        try {
            em.persist(area);
            LOGGER.info("Area creada con exito.");
        } catch (Exception e) {
            LOGGER.warning("ERROR a la hora de crear un Area");
            throw new CreateException(e.getMessage());
        }
    }

    /**
     * Metodo paara actualizar un Area
     *
     * @param area Objeto del tipo area
     * @throws UpdateException . Si no se pudo actualizar el area
     */
    public void updateArea(Area area) throws UpdateException {
        try {
            LOGGER.info("Modificanco área.");
            Query q= em.createQuery("update Area a set a.name=:name WHERE a.id=:id");
            q.setParameter("id", area.getId());
            q.setParameter("name", area.getName());
            q.executeUpdate();
            em.flush();
            LOGGER.info("Area modificada con éxito.");
        } catch (Exception e) {
            LOGGER.warning("ERROR a la hora de actualizar un Area");
            throw new UpdateException("ERROR en la actualizacion de un Area ");
        }
    }

    /**
     * Metodo que borra un Area por su id
     *
     * @param id Id del ARea
     * @throws DeleteException .Si un Area no pudo ser borrada con exito
     */
    public void deleteArea(int id) throws DeleteException {
        LOGGER.info("Petiicion de borrado de un Area");
        try {
            LOGGER.info("Borrando área.");
            Query q1 = em.createNamedQuery("DeleteArea").setParameter("id", id);
            q1.executeUpdate();
            em.flush();
            LOGGER.info("Area borrada con exito.");
        } catch (Exception e) {
            LOGGER.warning("ERROR en el borrado de area");
            throw new DeleteException("ERROR en el borrado de Area");
        }

    }

    /**
     * Metodo que retorna una lista de Areas
     *
     * @return una lista de Areas
     * @throws GetCollectionException .Si no puede devolver una lista de Areas
     */
    public Set<Area> getAreaList() throws GetCollectionException {
        LOGGER.info("Peticion de Lista de Areas");
        List<Area> listAreas = null;
        try {
            LOGGER.info("Obteniendo lista de areas.");
            listAreas = em.createNamedQuery("FindAllAreas").getResultList();
            LOGGER.info("Lista de areas obtenidas con exito.");
        } catch (Exception e) {
            LOGGER.warning("ERROR en la obtencion de la lista de Areas");
            throw new GetCollectionException(e.getMessage());
        }
        Set<Area> ret = new HashSet<Area>(listAreas);
        LOGGER.info("Respuesta de lista de Areas");
        return ret;
    }

    /**
     * Metodo de obtencion de un Area concreta
     * @param id Id del Area
     * @return un objeto de Area
     * @throws SelectException  .Si no puede devolver el Area 
     */
    @Override
    public Area getCompanyProfile(int id) throws SelectException {
        LOGGER.info("Peticion de Area");
        try {
            LOGGER.info("Obteniendo area por id.");
            return em.find(Area.class, id);
        } catch (Exception ex) {
            LOGGER.warning("ERROR en la obtencion de un Area");
            throw new SelectException("ERROR en la obtencion de Area");
        }
    }
}
