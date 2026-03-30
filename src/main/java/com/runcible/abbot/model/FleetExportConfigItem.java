package com.runcible.abbot.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * An {@link ExportConfigItem} that refers to a fleet's race results.
 */
@Entity
@DiscriminatorValue("FLEET")
public class FleetExportConfigItem extends ExportConfigItem
{
    public FleetExportConfigItem() {}

    public FleetExportConfigItem(Integer fleetId)
    {
        this.fleetId = fleetId;
    }

    @Column(name = "FLEET_ID")
    public Integer getFleetId()
    {
        return fleetId;
    }

    public void setFleetId(Integer fleetId)
    {
        this.fleetId = fleetId;
    }

    private Integer fleetId;
}
