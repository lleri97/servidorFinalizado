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

    /**
     * Metodo que crea una compañia
     *
     * @param company objeto del tipo Company
     *
     * @throws CreateException si no se puede crear
     */
    public void createCompany(Company company) throws CreateException;

    /**
     * Metodo de actualizacio de compañia
     *
     * @param company objeto del tipo Company
     * @throws UpdateException si no se encuentra la compañia
     */
    public void updateCompany(Company company) throws UpdateException;

    /**
     * Metodo que borra una compañia
     *
     * @param id Id de la comapañia
     * @throws DeleteException si no se puede borrar la compañia
     */
    public void deleteCompany(int id) throws DeleteException;

    /**
     * Metodo para obtener una compañia
     *
     * @param id Id de la compañia
     * @return objeto del tipo Company
     * @throws SelectException si no se encuentra la compañia
     */
    public Company getCompanyProfile(int id) throws SelectException;

    /**
     * metodo para obtener una lista de compañias
     *
     * @return lista de compañias
     * @throws GetCollectionException si no hay compañia en la base de datos
     */
    public Set<Company> getCompanyList() throws GetCollectionException;

}
