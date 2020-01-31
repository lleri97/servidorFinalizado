/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entitiesJPA.Company;
import entitiesJPA.Department;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.GetCollectionException;
import exceptions.SelectException;
import exceptions.UpdateException;
import java.util.Set;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yeray
 */
@Local
public interface EJBDepartmentInterface {

      /**
     * Metodo que crea un Departmento
     * @param department objeto de tipo Department
     * @throws CreateException si no se puede crear
     */
    public void createDepartment(Department department) throws CreateException;

       /**
     * Metodo para actualizar un departamneto
     * @param department objeto del tipo Department
     * @throws UpdateException si no se puede actualizar
     */
    public void updateDepartment(Department department) throws UpdateException;
    /**
     * Metodo de borrado de departamento por id
     * @param id Id del Departamento
     * @throws DeleteException si no se encuentra el departamento
     */
    public void deleteDepartment(int id) throws DeleteException;
    
     /**
     * Metodo que devuelve una lista se departamentos
     * @return lista de Department
      * @throws GetCollectionException Si no hay departamentos en la base de datos
     */
    public Set<Department> getDepartmentList() throws GetCollectionException;
    /**
     * Metodo de obtencion de un departamento
     * @param id Id de dicho departamento
     * @return un objeto del tipo Department
     * @throws SelectException Si no existe el departamento
     */
    public Department getDepartmentProfile(int id) throws SelectException;

}
