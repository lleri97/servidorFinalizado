/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesJPA;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author fran
 */
@Embeddable
public class CompanyDepartmentKey implements Serializable {
    
    @Column(name = "id_company")
    int id_company;
    
    @Column(name = "id_department")
    int id_department;

    public int getId_company() {
        return id_company;
    }

    public void setId_company(int id_company) {
        this.id_company = id_company;
    }

    public int getId_department() {
        return id_department;
    }

    public void setId_department(int id_department) {
        this.id_department = id_department;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompanyDepartmentKey other = (CompanyDepartmentKey) obj;
        if (this.id_company != other.id_company) {
            return false;
        }
        if (this.id_department != other.id_department) {
            return false;
        }
        return true;
    }
    
    
}
