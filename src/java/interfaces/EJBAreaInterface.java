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

    public void createArea(Area area) throws CreateException;

    public void updateArea(Area area) throws UpdateException;

    public void deleteArea(int id) throws DeleteException;

    public Set<Area> getAreaList() throws GetCollectionException;

    public Area getCompanyProfile(int id) throws SelectException;
}
