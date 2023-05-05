package tfip.csf.day39workshop.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import tfip.csf.day39workshop.models.Comment;

@Repository
public class CharCommentRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COMMENTS_COL = "comments";

    public Comment insertComment(Comment c) {
        return this.mongoTemplate.insert(c, COMMENTS_COL);
    }

    public List<Comment> getAllComment(String charId) {
        Pageable pageable = PageRequest.of(0,10);
        Query commentsDynamicQry = new Query()
                .addCriteria(Criteria.where("charId").is(charId))
                .with(pageable);
        List<Comment> filterComments = mongoTemplate.find(commentsDynamicQry, Comment.class, COMMENTS_COL);
        Page<Comment> commentPage = PageableExecutionUtils.getPage(
                            filterComments,
                            pageable,
                            ()->mongoTemplate.count(commentsDynamicQry, Comment.class)
        );
        return commentPage.toList();
    }

}