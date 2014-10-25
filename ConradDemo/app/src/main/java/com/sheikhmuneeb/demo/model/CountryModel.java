package com.sheikhmuneeb.demo.model;

import com.google.gson.Gson;
import java.util.List;

public class CountryModel {
    public List<Worldpopulation> worldpopulation;

    public class Worldpopulation {
        public int rank;
        public String country;
        public String population;
        public String flag;
    }

    public static CountryModel fromJson(String json) {
        return new Gson().fromJson(json, CountryModel.class);
    }
}