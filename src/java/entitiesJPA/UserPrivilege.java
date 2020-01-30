/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesJPA;

/**
 * This enumeration establishes the user's role
 *
 * @author 2dam
 */
public enum UserPrivilege {
    /**
     * User role
     */
    USER,
    /**
     * Company administrator role
     */
    COMPANYADMIN,
    /**
     * Super administrator role
     */
    SUPERADMIN
}
