package tfip.csf.day39workshop.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import tfip.csf.day39workshop.models.Comment;
import tfip.csf.day39workshop.models.MarvelCharacter;
import tfip.csf.day39workshop.repositories.CharCommentRepository;

@Service
public class CharacterService {
    
    @Autowired
    private MarvelApiService marvelApiSvc;

    @Autowired
    private CharCommentRepository charCommentRepo;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public Optional<List<MarvelCharacter>> getCharacters (String charName, Integer limit, Integer offset) {
        return this.marvelApiSvc.getCharacters(charName, limit, offset);
    }

    public MarvelCharacter getCharacterDetails (String charId) throws IOException {
        MarvelCharacter mc = null;
        // check if charId already exists inside Redis
        String charDetailsJson = (String) redisTemplate.opsForValue().get(charId);
        if (charDetailsJson != null) {
            mc = MarvelCharacter.createFromCache(charDetailsJson);
        } else {
            Optional<MarvelCharacter> opt = this.marvelApiSvc.getCharacterDetails(charId);
            mc = opt.get();
            redisTemplate.opsForValue().set(charId, mc.toJSON().toString());
            long currentDateTime = Instant.now().getMillis();
            Date afterAdding60Mins = new Date(currentDateTime + (60*60*1000));
            redisTemplate.expireAt(charId, afterAdding60Mins);
        }
        return mc;
    }

    public Comment insertComment(Comment c) {
        return this.charCommentRepo.insertComment(c);
    }

    public List<Comment> getAllComments(String charId) {
        return this.charCommentRepo.getAllComment(charId);
    }
}
