package tfip.csf.day39workshop.repositories;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import tfip.csf.day39workshop.models.Comment;

@Repository
public class CharCommentRepository {
    
    //MongoDB
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

    // MYSQL
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT = "insert into comments (char_id,comments) values (?,?)";
    private static final String SQL_SELECT = "select * from comments where char_id = ?";

    public void SQLinsertComment(Comment c) {
        jdbcTemplate.update(SQL_INSERT, c.getCharId(), c.getComment());
    }

    public List<Comment> SQLgetAllComments(String charId) {
        List<Comment> comments = new LinkedList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT, charId);
        while (rs.next()) {
            Comment comment = new Comment();
            comment.setCharId(rs.getString("char_id"));
            comment.setComment(rs.getString("comments"));
            comments.add(comment);
        }
        return comments;
    }

}