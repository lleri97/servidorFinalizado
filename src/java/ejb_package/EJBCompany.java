/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb_package;

import entitiesJPA.Company;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.GetCollectionException;
import exceptions.SelectException;
import exceptions.UpdateException;
import interfaces.EJBCompanyInterface;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ruben
 */
@Stateless
public class EJBCompany implements EJBCompanyInterface {

    @PersistenceContext(unitName = "grupo5_ServerPU")
    private EntityManager em;

    /**
     * Metodo que crea una compañia
     *
     * @param company objeto del tipo Company
     *
     * @throws CreateException si no se puede crear
     */
    @Override
    public void createCompany(Company company) throws CreateException {
        try {
            em.persist(company);
        } catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    /**
     * Metodo de actualizacio de compañia
     *
     * @param company objeto del tipo Company
     * @throws UpdateException si no se encuentra la compañia
     */
    @Override
    public void updateCompany(Company company) throws UpdateException {

        try {
            Query q = em.createQuery("update Company a set a.name=:name, a.cif=:cif WHERE a.id=:id");
            q.setParameter("id", company.getId());
            q.setParameter("cif", company.getCif());
            q.setParameter("name", company.getName());
            q.executeUpdate();
            em.flush();
        } catch (Exception ex) {
            throw new UpdateException(ex.getMessage());
        }
    }

    /**
     * Metodo que borra una compañia
     *
     * @param id Id de la comapañia
     * @throws DeleteException si no se puede borrar la compañia
     */

    @Override
    public void deleteCompany(int id) throws DeleteException {
        try {
            Query q1 = em.createQuery("delete from Company a where a.id=:id");
            q1.setParameter("id", id);
            q1.executeUpdate();
            em.flush();
        } catch (Exception ex) {
            throw new DeleteException(ex.getMessage());
        }
    }

    /**
     * metodo para obtener una lista de compañias
     *
     * @return lista de compañias
     * @throws GetCollectionException si no hay compañia en la base de datos
     */
    @Override
    public Set<Company> getCompanyList() throws GetCollectionException {
        List<Company> listCompany = null;
        try {
            listCompany = em.createNamedQuery("findAllCompanies").getResultList();
        } catch (Exception ex) {
            throw new GetCollectionException(ex.getMessage());
        }
        Set<Company> ret = new HashSet<Company>(listCompany);
        return ret;
    }

    /**
     * Metodo para obtener una compañia
     *
     * @param id Id de la compañia
     * @return objeto del tipo Company
     * @throws SelectException si no se encuentra la compañia
     */
    @Override
    public Company getCompanyProfile(int id) throws SelectException {
        Company ret = null;
        try {
            ret = em.find(Company.class, id);
        } catch (Exception ex) {
            throw new SelectException(ex.getMessage());
        }
        return ret;
    }
}
