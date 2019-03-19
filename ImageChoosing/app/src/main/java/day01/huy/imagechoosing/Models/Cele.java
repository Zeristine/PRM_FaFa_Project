package day01.huy.imagechoosing.Models;

public class Cele {
    int id;

    String name;
    String description;
    String url;

    public Cele(String name, String description,String url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public Cele() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Cele{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
