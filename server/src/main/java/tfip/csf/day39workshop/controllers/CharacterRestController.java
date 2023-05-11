package tfip.csf.day39workshop.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import tfip.csf.day39workshop.models.Comment;
import tfip.csf.day39workshop.models.MarvelCharacter;
import tfip.csf.day39workshop.services.CharacterService;

@RestController
@RequestMapping(path="/api/characters", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
public class CharacterRestController {
    
    @Autowired
    private CharacterService charSvc;

    @GetMapping
    public ResponseEntity<String> getCharacters(
        @RequestParam(required=true) String charName,
        @RequestParam(required=true) Integer limit,
        @RequestParam(required=true) Integer offset) {
        Optional<List<MarvelCharacter>> or = this.charSvc.getCharacters(charName, limit, offset);
        List<MarvelCharacter> results = or.get();
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (MarvelCharacter mc : results)
            arrBuilder.add(mc.toJSON());
        JsonArray result = arrBuilder.build();
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString());
    }

    @GetMapping(path="/{charId}")
    public ResponseEntity<String> getCharacterDetails(@PathVariable(required=true) String charId) throws IOException {
        MarvelCharacter mc = this.charSvc.getCharacterDetails(charId);
        JsonObject jo = Json.createObjectBuilder()
            .add("details",mc.toJSON()).build();
        return ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(jo.toString());
    }

    @PostMapping(path="/{charId}")
    public ResponseEntity<String> saveCharacterComment(@RequestBody Comment comment, @PathVariable(required=true) String charId) {
        comment.setCharId(charId);
        Comment c = this.charSvc.insertComment(comment);
        return ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(c.toString());
    }

    @GetMapping(path="/comments/{charId}")
    public ResponseEntity<String> getCharComments(@PathVariable(required=true) String charId) {
        List<Comment> comments = this.charSvc.getAllComments(charId);
        JsonArrayBuilder bld = Json.createArrayBuilder();
        for (Comment comment : comments) {
            bld.add(comment.toJSON());
        }
        JsonArray result = bld.build();
        return ResponseEntity.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(result.toString());
    }

}
