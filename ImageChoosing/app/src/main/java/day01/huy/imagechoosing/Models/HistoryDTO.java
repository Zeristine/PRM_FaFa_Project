package day01.huy.imagechoosing.Models;

import java.io.Serializable;

public class HistoryDTO implements Serializable {
    private String name;
    private String search_date;

    public HistoryDTO(String name, String search_date) {
        this.name = name;
        this.search_date = search_date;
    }

    public HistoryDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSearch_date() {
        return search_date;
    }

    public void setSearch_date(String search_date) {
        this.search_date = search_date;
    }
}
