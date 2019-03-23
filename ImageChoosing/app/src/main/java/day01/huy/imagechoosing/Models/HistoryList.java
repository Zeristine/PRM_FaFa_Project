package day01.huy.imagechoosing.Models;

import java.util.List;

public class HistoryList {
    private static List<HistoryDTO> list;

    public HistoryList() {

    }

    public static List<HistoryDTO> getList() {
        return list;
    }

    public static void setList(List<HistoryDTO> list) {
        HistoryList.list = list;
    }
}
