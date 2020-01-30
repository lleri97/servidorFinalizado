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

    public void createDepartment(Department department) throws CreateException;

    public void updateDepartment(Department department) throws UpdateException;

    public void deleteDepartment(int id) throws DeleteException;

    public Set<Department> getDepartmentList() throws GetCollectionException;

    public Department getDepartmentProfile(int id) throws SelectException;

}
