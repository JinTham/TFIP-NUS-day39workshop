package tfip.csf.day39workshop.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import tfip.csf.day39workshop.models.MarvelCharacter;


@Service
public class MarvelApiService {
    
    @Value("${workshop39.marvel.api.url}")
    private String marvelApiUrl;

    @Value("${workshop39.marvel.api.priv_key}")
    private String marvelApiPrivKey;

    @Value("${workshop39.marvel.api.pub_key}")
    private String marvelApiPubKey;

    private String[] getMarvelApiHash() {
        String[] result = new String[2];
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long tsVal = timestamp.getTime();
        String hashVal = tsVal + marvelApiPrivKey + marvelApiPubKey;
        result[0] = tsVal+"";
        result[1] = DigestUtils.md5Hex(hashVal);
        return result;
    }

    public Optional<List<MarvelCharacter>> getCharacters(String characterName, Integer limit, Integer offset) {
        ResponseEntity<String> resp = null;
        List<MarvelCharacter> mcList = null;
        String[] hash = getMarvelApiHash();

        String marvelApiCharsUrl = UriComponentsBuilder
                                    .fromUriString(marvelApiUrl+"characters")
                                    .queryParam("ts",hash[0])
                                    .queryParam("apikey",marvelApiPubKey.trim())
                                    .queryParam("hash",hash[1])
                                    .queryParam("nameStartsWith",characterName.replaceAll(" ","+"))
                                    .queryParam("offset", offset)
                                    .queryParam("limit",limit)
                                    .toUriString();
        RestTemplate restTemplate = new RestTemplate();
        resp = restTemplate.getForEntity(marvelApiCharsUrl,String.class);
        try {
            mcList = MarvelCharacter.create(resp.getBody());
        } catch (IOException e){
            e.printStackTrace();
        }

        if (mcList != null) {
            return Optional.of(mcList);
        }
        return Optional.empty();
    }

    public Optional<MarvelCharacter> getCharacterDetails(String charId) {
        ResponseEntity<String> resp = null;
        MarvelCharacter mc = null;
        String[] hash = getMarvelApiHash();

        String marvelApiCharsUrl = UriComponentsBuilder
                                    .fromUriString(marvelApiUrl+"characters/"+charId)
                                    .queryParam("ts",hash[0])
                                    .queryParam("apikey",marvelApiPubKey.trim())
                                    .queryParam("hash",hash[1])
                                    .toUriString();
        RestTemplate restTemplate = new RestTemplate();
        resp = restTemplate.getForEntity(marvelApiCharsUrl,String.class);
        try {
            List<MarvelCharacter> mcList = MarvelCharacter.create(resp.getBody());
            mc = mcList.get(0);
        } catch (IOException e){
            e.printStackTrace();
        }
        if (mc != null) {
            return Optional.of(mc);
        }
        return Optional.empty();
    }

}
