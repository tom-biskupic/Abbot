package com.runcible.abbot.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

/**
 * A saved export configuration for a race series.
 * Records the name, the CSS table class used when rendering HTML,
 * and an ordered list of items (competitions and/or fleets) to export.
 */
@Entity
@Table(name = "EXPORT_CONFIG")
public class ExportConfig
{
    public ExportConfig() {}

    public ExportConfig(
            String               name,
            Integer              raceSeriesId,
            String               tableClass,
            List<ExportConfigItem> items)
    {
        this.name = name;
        this.raceSeriesId = raceSeriesId;
        this.tableClass = tableClass;
        this.items = items;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXPORT_CONFIG_ID")
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @Size(min = 1, message = "Export configuration name must be provided.")
    @Column(name = "NAME", nullable = false)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(name = "RACE_SERIES_ID", nullable = false)
    public Integer getRaceSeriesId()
    {
        return raceSeriesId;
    }

    public void setRaceSeriesId(Integer raceSeriesId)
    {
        this.raceSeriesId = raceSeriesId;
    }

    /**
     * The CSS class applied to result tables in the exported HTML.
     * Defaults to "results-table".
     */
    @Column(name = "TABLE_CLASS", nullable = false)
    public String getTableClass()
    {
        return tableClass;
    }

    public void setTableClass(String tableClass)
    {
        this.tableClass = tableClass;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "EXPORT_CONFIG_ID")
    @OrderColumn(name = "ITEM_ORDER")
    public List<ExportConfigItem> getItems()
    {
        return items;
    }

    public void setItems(List<ExportConfigItem> items)
    {
        this.items = items;
    }

    private Integer              id;
    private String               name         = "";
    private Integer              raceSeriesId;
    private String               tableClass   = "results-table";
    private List<ExportConfigItem> items      = new ArrayList<>();
}
