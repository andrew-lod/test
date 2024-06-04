package apple.backend.filter.domain.entity;

import jakarta.persistence.*;
;

@Entity
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "query", nullable = false)
    private String filter;

    // Constructors, getters, and setters

    public Filter() {
    }

    public Filter(String name, String filter) {
        this.name = name;
        this.filter = filter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
