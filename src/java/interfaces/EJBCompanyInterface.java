/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entitiesJPA.Company;
import entitiesJPA.User;
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
 * @author stone
 */
@Local
public interface EJBCompanyInterface {

    public void createCompany(Company company) throws CreateException;

    public void updateCompany(Company company) throws UpdateException;

    public void deleteCompany(int id) throws DeleteException;

    public Company getCompanyProfile(int id) throws SelectException;

    public Set<Company> getCompanyList() throws GetCollectionException;

}
