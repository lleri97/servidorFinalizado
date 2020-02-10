/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb_package;

import entitiesJPA.Department;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.GetCollectionException;
import exceptions.SelectException;
import exceptions.UpdateException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import interfaces.EJBDepartmentInterface;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Yeray
 */
@Stateless
public class EJBDepartment implements EJBDepartmentInterface {

    @PersistenceContext(unitName = "grupo5_ServerPU")
    private EntityManager em;

    /**
     * Metodo que crea un Departmento
     *
     * @param depart objeto de tipo Department
     * @throws CreateException si no se puede crear
     */
    public void createDepartment(Department depart) throws CreateException {
        em.persist(depart);
    }

      /**
     * Metodo para actualizar un departamneto
     * @param depart objeto del tipo Department
     * @throws UpdateException si no se puede actualizar
     */
    public void updateDepartment(Department depart) throws UpdateException {
        Department department = new Department();
        department = em.find(Department.class, depart.getId());
        department.setAreas(depart.getAreas());
        department.setCompanies(depart.getCompanies());
        department.setName(depart.getName());
        em.merge(department);
        em.flush();
    }
    /**
     * Metodo de borrado de departamento por id
     * @param id Id del Departamento
     * @throws DeleteException si no se encuentra el departamento
     */
    public void deleteDepartment(int id) throws DeleteException {
        try {
            Query q1 = em.createNamedQuery("deleteDepartment").setParameter("id", id);
            q1.executeUpdate();
            em.flush();
        } catch (Exception ex) {
            throw new DeleteException(ex.getMessage());
        }
    }

       /**
     * Metodo que devuelve una lista se departamentos
     * @return lista de Department
      * @throws GetCollectionException Si no hay departamentos en la base de datos
     */
    public Set<Department> getDepartmentList() throws GetCollectionException {
        List<Department> listDepartment = null;
        try {
            listDepartment = em.createNamedQuery("findAllDepartments").getResultList();
        } catch (Exception ex) {
            throw new GetCollectionException(ex.getMessage());
        }
        Set<Department> ret = new HashSet<Department>(listDepartment);
        return ret;
    }
      /**
     * Metodo de obtencion de un departamento
     * @param id Id de dicho departamento
     * @return un objeto del tipo Department
     * @throws SelectException Si no existe el departamento
     */
    @Override
    public Department getDepartmentProfile(int id) throws SelectException {
        try {
            return em.find(Department.class, id);
        } catch (NoResultException ex) {
            throw new SelectException(ex.getMessage());
        }
    }

}
