package com.michaldurkalec.fw.fastnfurious.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.CascadeType.DELETE;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Cinema {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String name;

    private String address;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @Cascade({SAVE_UPDATE, DELETE})
    @OneToMany(mappedBy = "cinema", fetch = EAGER)
    private Set<MovieShow> movieShows;
}
