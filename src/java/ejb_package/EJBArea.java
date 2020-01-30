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
import javax.persistence.Query;

/**
 * EJB class that make the CRUD of the Area entity
 *
 * @author Andoni
 */
@Stateless
public class EJBArea implements EJBAreaInterface {

    @PersistenceContext(unitName = "grupo5_ServerPU")
    private EntityManager em;

    /**
     * Methot that create a new Area
     *
     * @param area Area entity object type
     * @throws CreateException
     */
    public void createArea(Area area) throws CreateException {
        try {
            em.persist(area);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    /**
     * Method that make the update of the area entity
     *
     * @param area Area entity object type
     * @throws UpdateException
     */
    public void updateArea(Area area) throws UpdateException {
        try {
            Query q= em.createQuery("update Area a set a.name=:name WHERE a.id=:id");
            q.setParameter("id", area.getId());
            q.setParameter("name", area.getName());
            q.executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new UpdateException(e.getMessage());
        }
    }

    /**
     * Methot that delete a specific Area by the id
     *
     * @param id int that contain the area id to delete
     * @throws DeleteException
     */
    public void deleteArea(int id) throws DeleteException {
        try {
            Query q1 = em.createNamedQuery("DeleteArea").setParameter("id", id);
            q1.executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new DeleteException(e.getMessage());
        }

    }

    /**
     * Methot that return a area list collection
     *
     * @return a area list collection
     * @throws GetCollectionException
     */
    public Set<Area> getAreaList() throws GetCollectionException {
        List<Area> listAreas = null;
        try {
            listAreas = em.createNamedQuery("FindAllAreas").getResultList();
        } catch (Exception e) {
            // LOGGER.log(Level.WARNING, "IMPOSIBLE GET AREA LIST");
            throw new GetCollectionException(e.getMessage());
        }
        Set<Area> ret = new HashSet<Area>(listAreas);
        return ret;
    }

    @Override
    public Area getCompanyProfile(int id) throws SelectException {
        try {
            return em.find(Area.class, id);
        } catch (Exception ex) {
            throw new SelectException(ex.getMessage());
        }
    }
}
