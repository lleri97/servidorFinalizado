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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@NamedQueries({
    @NamedQuery(name = "FindAllAreas",
            query = "SELECT a FROM Area a ")
    ,
    @NamedQuery(
            name = "DeleteArea",
            query = "delete from Area a where a.id=:id")
})

/**
 *
 * @author Andoni
 */
@Entity
@Table(name = "area", schema = "grupo5_database")
@XmlRootElement
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "areas_documents", schema = "grupo5_database",
            joinColumns = @JoinColumn(name = "id_Area"),
            inverseJoinColumns = @JoinColumn(name = "id_Document"))
    private Set<Document> documents;

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
     * @return the departments
     */
    @XmlTransient
    public Department getDepartments() {
        return department;
    }

    /**
     * @param departments the departments to set
     */
    public void setDepartments(Department departments) {
        this.department = departments;
    }

    /**
     * @return the documents
     */

    public Set<Document> getDocuments() {
        return documents;
    }

    /**
     * @param documents the documents to set
     */
    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Area)) {
            return false;
        }
        Area other = (Area) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entitiesJPA.Area[ id=" + id + " ]";
    }
}
