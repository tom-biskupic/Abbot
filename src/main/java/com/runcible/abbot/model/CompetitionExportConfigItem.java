package com.runcible.abbot.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * An {@link ExportConfigItem} that refers to a competition points table.
 */
@Entity
@DiscriminatorValue("COMPETITION")
public class CompetitionExportConfigItem extends ExportConfigItem
{
    public CompetitionExportConfigItem() {}

    public CompetitionExportConfigItem(Integer competitionId)
    {
        this.competitionId = competitionId;
    }

    @Column(name = "COMPETITION_ID")
    public Integer getCompetitionId()
    {
        return competitionId;
    }

    public void setCompetitionId(Integer competitionId)
    {
        this.competitionId = competitionId;
    }

    private Integer competitionId;
}
