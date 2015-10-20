package org.talend.ps.benchmark.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(name = "ALL_PERSONS", query = "SELECT P FROM Person P")
})

public class Person {

    @Id
    private String id;
    private String name;
    private String twitterName;

    public Person() {
    }

    public Person(String id, String name, String twitterName) {
        super();
        this.id = id;
        this.name = name;
        this.twitterName = twitterName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTwitterName() {
        return twitterName;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }

}
