/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesJPA;

import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Past;

/**
 *
 * @author 2dam
 */
@Entity
@Table(name = "companyDepartment", schema = "grupo5_database")
public class CompanyDepartment {

    @EmbeddedId
    CompanyDepartmentKey id;

    @ManyToOne
    @MapsId("id_company")
    @JoinColumn(name = "id_company")
    Company company;
    
    @ManyToOne
    @MapsId("id_department")
    @JoinColumn(name = "id_department")
    Department department;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Past
    private Date date;
    

    public CompanyDepartmentKey getId() {
        return id;
    }

    public void setId(CompanyDepartmentKey id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    
}
