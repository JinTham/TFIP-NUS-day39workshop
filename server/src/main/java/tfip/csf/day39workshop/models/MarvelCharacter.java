package tfip.csf.day39workshop.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class MarvelCharacter implements Serializable{
    
    private Integer id;
    private String name;
    private String description;
    private String photo;
    private List<Comment> comments;
    
    public MarvelCharacter() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public static MarvelCharacter createJson(JsonObject jo) {
        MarvelCharacter mc = new MarvelCharacter();
        JsonObject t = jo.getJsonObject("thumbnail");
        String path = t.getString("path");
        String fileExt = t.getString("extension");
        mc.setId(jo.getJsonNumber("id").intValue());
        mc.setName(jo.getString(fileExt));
        mc.setDescription(jo.getString("description"));
        mc.setPhoto(path+"."+fileExt);
        return mc;
    }

    public static List<MarvelCharacter> create(String json) throws IOException{
        List<MarvelCharacter> mcList = new LinkedList<>();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader jrd = Json.createReader(is);
            JsonObject jo = jrd.readObject();
            JsonObject joo = jo.getJsonObject("data");
            if (joo.getJsonArray("results") != null) {
                mcList = joo.getJsonArray("results").stream()
                            .map(v-> (JsonObject)v)
                            .map(v-> MarvelCharacter.createJson(v))
                            .toList();
            }
        }
        return mcList;
    }

    public static MarvelCharacter createFormCache(String json) throws IOException {
        MarvelCharacter mc = new MarvelCharacter();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader jrd = Json.createReader(is);
            JsonObject jo = jrd.readObject();
            mc.setId(jo.getJsonNumber("id").intValue());
            mc.setName(jo.getString("name"));
            mc.setDescription(jo.getString("description"));
            mc.setPhoto(jo.getString("photo"));
        }
        return mc;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id",getId())
                .add("name",getName())
                .add("description",getDescription())
                .add("photo",getPhoto())
                .build();
    }

}
