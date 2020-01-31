/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import entitiesJPA.Area;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.GetCollectionException;
import exceptions.SelectException;
import exceptions.UpdateException;
import java.util.Set;
import javax.ejb.Local;

/**
 *
 * @author Andoni
 */
@Local
public interface EJBAreaInterface {

    /**
     * Metodo que crea un nuevo area
     *
     * @param area Objeto del tipo Area
     * @throws CreateException .Si no se pudo crear un Area
     */
    public void createArea(Area area) throws CreateException;

     /**
     * Metodo paara actualizar un Area
     *
     * @param area Objeto del tipo area
     * @throws UpdateException . Si no se pudo actualizar el area
     */
    public void updateArea(Area area) throws UpdateException;

    /**
     * Metodo que borra un Area por su id
     *
     * @param id Id del ARea
     * @throws DeleteException .Si un Area no pudo ser borrada con exito
     */
    public void deleteArea(int id) throws DeleteException;

    /**
     * Metodo que retorna una lista de Areas
     *
     * @return una lista de Areas
     * @throws GetCollectionException .Si no puede devolver una lista de Areas
     */
    public Set<Area> getAreaList() throws GetCollectionException;


    /**
     * Metodo de obtencion de un Area concreta
     * @param id Id del Area
     * @return un objeto de Area
     * @throws SelectException  .Si no puede devolver el Area 
     */
    public Area getCompanyProfile(int id) throws SelectException;
}
