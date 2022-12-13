package com.oracle.edge.pushData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author MPFEIFER
 */
@Getter
@Setter
@Entity
@Table(name="SnomedElements")
public class SnomedElements {
    @Id
    private String conceptId = null;
    private String term = null;
    private String definition = null;
}

