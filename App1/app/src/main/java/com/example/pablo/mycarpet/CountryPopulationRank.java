
package com.example.pablo.mycarpet;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryPopulationRank {

    @SerializedName("worldpopulation")
    @Expose
    private List<Worldpopulation> worldpopulation = null;

    public List<Worldpopulation> getWorldpopulation() {
        return worldpopulation;
    }

    public void setWorldpopulation(List<Worldpopulation> worldpopulation) {
        this.worldpopulation = worldpopulation;
    }

}
