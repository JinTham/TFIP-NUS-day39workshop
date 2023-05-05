package tfip.csf.day39workshop.models;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Comment {
    
    private String charId;
    private String comment;
    
    public Comment() {
    }

    public String getCharId() {
        return charId;
    }

    public void setCharId(String charId) {
        this.charId = charId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static Comment create(Document doc) {
        Comment c = new Comment();
        c.setCharId(doc.getObjectId("charId").toString());
        c.setComment(doc.getString("comment"));
        return c;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("charId",getCharId())
                .add("comment",getComment())
                .build();
    }
    
}
