/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitiesJPA;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Yeray
 */
@NamedQuery(
        name = "findAllDepartments",
        query = "SELECT a FROM Department a ORDER BY a.id")
@Entity
@Table(name = "department", schema = "grupo5_database")
@XmlRootElement
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    /**
     * Department name
     */
    @Size(min = 1, max = 40)
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Companies collection
     */
    @ManyToMany(mappedBy = "departments", fetch = FetchType.EAGER)
    private Set<Company> companies;

    /**
     * Areas collection
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "department", cascade = CascadeType.ALL)
    private Set<Area> areas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the companies
     */
    @XmlTransient
    public Set<Company> getCompanies() {
        return companies;
    }

    /**
     * @param companies the companies to set
     */
    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    /**
     * @return the areas
     */
    public Set<Area> getAreas() {
        return areas;
    }

    /**
     * @param areas the areas to set
     */
    public void setAreas(Set<Area> areas) {
        this.areas = areas;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    /**
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Department)) {
            return false;
        }
        Department other = (Department) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "entitiesJPA.NewEntity[ id=" + id + " ]";
    }

}
