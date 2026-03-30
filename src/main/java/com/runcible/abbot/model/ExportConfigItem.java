package com.runcible.abbot.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

/**
 * Base class for a single item within a saved {@link ExportConfig}.
 * Concrete subtypes are {@link CompetitionExportConfigItem} and {@link FleetExportConfigItem}.
 *
 * Uses SINGLE_TABLE inheritance: all subtypes share the EXPORT_CONFIG_ITEM table,
 * distinguished by the ITEM_TYPE discriminator column.
 */
@Entity
@Table(name = "EXPORT_CONFIG_ITEM")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ITEM_TYPE", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "itemType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CompetitionExportConfigItem.class, name = "COMPETITION"),
    @JsonSubTypes.Type(value = FleetExportConfigItem.class,       name = "FLEET")
})
public abstract class ExportConfigItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXPORT_CONFIG_ITEM_ID")
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    private Integer id;
}
