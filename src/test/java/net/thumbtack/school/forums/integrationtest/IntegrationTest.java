package net.thumbtack.school.forums.integrationtest;

import net.thumbtack.school.forums.ForumsServer;
import net.thumbtack.school.forums.integrationtest.base.TestBase;
import net.thumbtack.school.forums.model.types.Decision;
import net.thumbtack.school.forums.model.types.ForumType;
import net.thumbtack.school.forums.model.types.MessageState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ForumsServer.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest extends TestBase {

    @Test
    public void testExample() {
        clear();
        String cookieAuthor = registerUser(200, "AuthorForForum");
        Integer idForum = createForum(200, cookieAuthor, "TopicForForum", ForumType.UNMODERATED);
        Integer idMessage = createMessage(200, cookieAuthor, idForum, "Example", "TestBody", MessageState.PUBLISHED);
        Integer idComment = createComment(200, cookieAuthor, idMessage, "BodyComment", MessageState.PUBLISHED);
    }

    @Test
    public void testRegisterWithSameLogin() {
        clear();
        String cookieTest = registerUser(200, "SameLogin");
        registerUser(400, "SameLogin");
        deleteUser(200, cookieTest);
        deleteUser(400, cookieTest);
        registerUser(200, "SameLogin");
        registerUser(400, "SameLogin");
    }

    @Test
    public void testCreateForumWithSameTopic() {
        clear();
        String cookieTest = registerUser(200, "TestUser");

        int idForum = createForum(200, cookieTest, "TopicForum", ForumType.UNMODERATED);

        createForum(400, cookieTest, "TopicForum", ForumType.UNMODERATED);
        createForum(400, cookieTest, "TopicForum", ForumType.MODERATED);

        deleteForum(200, cookieTest, idForum);
        deleteForum(400, cookieTest, idForum);

        createForum(200, cookieTest, "TopicForum", ForumType.MODERATED);


    }


    @Test
    public void testForumUnModerated() {
        clear();
        String cookieAuthor = registerUser(200, "AuthorForForum");
        int idForum = createForum(200, cookieAuthor, "TopicForForum", ForumType.UNMODERATED);

        int idMessage = createMessage(200, cookieAuthor, idForum, "ExampleAuthor", "TestBody", MessageState.PUBLISHED);
        int forChange = createMessage(200, cookieAuthor, idForum, "ExampleAuthor2", "TestBody", MessageState.PUBLISHED);

        createComment(200, cookieAuthor, idMessage, "BodyComment1", MessageState.PUBLISHED);
        createComment(200, cookieAuthor, idMessage, "BodyComment1", MessageState.PUBLISHED);
        int idComment = createComment(200, cookieAuthor, idMessage, "BodyComment1", MessageState.PUBLISHED);

        String justUser = registerUser(200, "JustUser");

        createComment(200, justUser, idComment, "BodyComment2", MessageState.PUBLISHED);
        int idCommentComment = createComment(200, justUser, idComment, "BodyComment2", MessageState.PUBLISHED);
        int forDelete = createComment(200, justUser, idComment, "BodyComment2", MessageState.PUBLISHED);

        int forChange2 = createMessage(200, justUser, idForum, "ExampleUser", "TestBody", MessageState.PUBLISHED);
        int forDelete2 = createMessage(200, justUser, idForum, "ExampleUser2", "TestBody", MessageState.PUBLISHED);
        createMessage(200, justUser, idForum, "ExampleUser3", "TestBody", MessageState.PUBLISHED);

        String justUserTwo = registerUser(200, "JustUserTwo");

        getForum(200, justUserTwo, idForum, 5, 6);

        deleteMessage(400, cookieAuthor, idMessage);
        deleteMessage(400, cookieAuthor, idComment);

        deleteMessage(200, justUser, forDelete);
        deleteMessage(200, justUser, forDelete2);

        getForum(200, justUserTwo, idForum, 4, 5);

        changeMessage(200, cookieAuthor, forChange, "I like this Message", MessageState.PUBLISHED);

        changeMessage(200, justUser, forChange2, "I like this Comment", MessageState.PUBLISHED);

        getForum(200, justUserTwo, idForum, 4, 5);

        createComment(200, justUserTwo, idCommentComment, "BodyComment4", MessageState.PUBLISHED);

        createComment(200, justUserTwo, idCommentComment, "BodyComment4", MessageState.PUBLISHED);
        int forConvert2 = createComment(200, justUserTwo, idCommentComment, "BodyComment4", MessageState.PUBLISHED);

        convertComment(200, cookieAuthor, idCommentComment, "TopicForMessage", MessageState.PUBLISHED);

        convertComment(200, cookieAuthor, forConvert2, "MessageConverted", MessageState.PUBLISHED);

        getForum(200, justUserTwo, idForum, 6, 6);

        deleteForum(200, cookieAuthor, idForum);

        getForum(400, justUserTwo, idForum, 0, 0);
    }

    @Test
    public void testForumModerated() {
        clear();
        String cookieAuthor = registerUser(200, "AuthorForForum");
        int idForum = createForum(200, cookieAuthor, "TopicForForum", ForumType.MODERATED);

        int idMessage = createMessage(200, cookieAuthor, idForum, "ExampleAuthor", "TestBody", MessageState.PUBLISHED);
        createMessage(200, cookieAuthor, idForum, "ExampleAuthor2", "TestBody", MessageState.PUBLISHED);

        createComment(200, cookieAuthor, idMessage, "BodyComment1", MessageState.PUBLISHED);
        createComment(200, cookieAuthor, idMessage, "BodyComment1", MessageState.PUBLISHED);
        int idComment = createComment(200, cookieAuthor, idMessage, "BodyComment1", MessageState.PUBLISHED);

        String justUser = registerUser(200, "JustUser");

        createComment(200, justUser, idComment, "BodyComment2", MessageState.UNPUBLISHED);

        int idPublished = createComment(200, justUser, idComment, "BodyComment2", MessageState.UNPUBLISHED);
        int idUnpublished = createComment(200, justUser, idComment, "BodyComment2", MessageState.UNPUBLISHED);

        getForum(200, justUser, idForum, 2, 6);

        publishMessage(200, cookieAuthor, idPublished, Decision.YES);

        publishMessage(200, cookieAuthor, idUnpublished, Decision.NO);

        getForum(200, justUser, idForum, 2, 5);

        deleteMessage(400, cookieAuthor, idComment);

        deleteMessage(400, cookieAuthor, idPublished);

        deleteMessage(200, justUser, idPublished);

        getForum(200, justUser, idForum, 2, 4);

        createMessage(200, justUser, idForum, "JustTopic", "JustBodyMessage", MessageState.UNPUBLISHED);

        int idMessageByJustUser = createMessage(200, justUser, idForum, "JustTopic2", "JustBodyMessage2", MessageState.UNPUBLISHED);

        publishMessage(200, cookieAuthor, idMessageByJustUser, Decision.YES);

        publishMessage(400, cookieAuthor, idMessageByJustUser, Decision.YES);

        getForum(200, justUser, idForum, 4, 4);

        changeMessage(200, justUser, idMessageByJustUser, "ChangedJustBodyMessage", MessageState.UNPUBLISHED);

        publishMessage(200, cookieAuthor, idMessageByJustUser, Decision.YES);

        getForum(200, justUser, idForum, 4, 4);

        int idCommentByJustUser = createComment(200, justUser, idMessageByJustUser, "CommentBodyByJustUser", MessageState.UNPUBLISHED);

        getForum(200, justUser, idForum, 4, 5);

        convertComment(200, cookieAuthor, idCommentByJustUser, "NewTopicForJustUser", MessageState.UNPUBLISHED);

        convertComment(400, cookieAuthor, idCommentByJustUser, "NewTopicForJustUser", MessageState.UNPUBLISHED);

        getForum(200, justUser, idForum, 5, 4);

        publishMessage(200, cookieAuthor, idCommentByJustUser, Decision.YES);

        publishMessage(400, cookieAuthor, idCommentByJustUser, Decision.YES);

        getForum(200, justUser, idForum, 5, 4);

        deleteForum(200, cookieAuthor, idForum);

        getForum(200, justUser, idForum, 5, 4);

        createMessage(400, cookieAuthor, idForum, "ExampleAuthor", "TestBody", MessageState.PUBLISHED);

        createMessage(400, justUser, idForum, "JustTopic", "JustBodyMessage", MessageState.UNPUBLISHED);

        createComment(400, justUser, idMessageByJustUser, "CommentBodyByJustUser", MessageState.UNPUBLISHED);
    }


    @Test
    public void testRatings() {
        clear();
        String cookieAuthor = registerUser(200, "AuthorForForum");
        int idForum = createForum(200, cookieAuthor, "TopicForForum", ForumType.MODERATED);

        List<Integer> messages = new ArrayList<>();

        int idMessage = createMessage(200, cookieAuthor, idForum, "ExampleAuthor", "TestBody", MessageState.PUBLISHED);
        messages.add(idMessage);
        messages.add(createMessage(200, cookieAuthor, idForum, "ExampleAuthor2", "TestBody", MessageState.PUBLISHED));

        int idComment = createComment(200, cookieAuthor, idMessage, "BodyComment1", MessageState.PUBLISHED);
        messages.add(idComment);

        messages.add(createComment(200, cookieAuthor, idMessage, "BodyComment1", MessageState.PUBLISHED));

        String liker1 = registerUser(200, "Liker1");
        String liker2 = registerUser(200, "Liker2");
        String liker3 = registerUser(200, "Liker3");


        for (Integer messageId : messages) {
            addRatingMessage(200, liker1, messageId, 1);

            addRatingMessage(200, liker2, messageId, 2);

            addRatingMessage(200, liker3, messageId, 3);
        }

        for (Integer messageId : messages) {
            addRatingMessage(400, liker1, messageId, 1);

            addRatingMessage(400, liker2, messageId, 2);

            addRatingMessage(400, liker3, messageId, 3);
        }

        for (Integer messageId : messages) {
            addRatingMessage(200, liker1, messageId, 2);

            addRatingMessage(200, liker2, messageId, 3);

            addRatingMessage(200, liker3, messageId, 4);
        }

        for (Integer messageId : messages) {
            addRatingMessage(200, liker1, messageId, 3);

            addRatingMessage(200, liker2, messageId, 4);

            addRatingMessage(200, liker3, messageId, 5);
        }

        for (Integer messageId : messages) {
            addRatingMessage(200, liker1, messageId, 4);

            addRatingMessage(200, liker2, messageId, 5);

            addRatingMessage(400, liker3, messageId, 6);
        }

        for (Integer messageId : messages) {
            addRatingMessage(200, liker1, messageId, null);

            addRatingMessage(200, liker2, messageId, null);

            addRatingMessage(200, liker3, messageId, null);
        }

        for (Integer messageId : messages) {
            addRatingMessage(400, cookieAuthor, messageId, 1);

            addRatingMessage(400, cookieAuthor, messageId, 5);

            addRatingMessage(400, cookieAuthor, messageId, null);
        }

    }

}
