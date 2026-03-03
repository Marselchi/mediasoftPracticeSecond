package com.practice.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    //В тз никаких ограничений не стоит (длина например), и соответственно дальше их тоже не будет.
    // * Без необходимости
    String name;

    String shortName;
}
