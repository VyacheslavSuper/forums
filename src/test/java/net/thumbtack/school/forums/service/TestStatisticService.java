package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.request.forums.CreateForumRequest;
import net.thumbtack.school.forums.dto.request.messages.AddCommentRequest;
import net.thumbtack.school.forums.dto.request.messages.AddRatingOnMessageRequest;
import net.thumbtack.school.forums.dto.request.messages.CreateMessageRequest;
import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import net.thumbtack.school.forums.dto.response.forum.ForumResponse;
import net.thumbtack.school.forums.dto.response.message.MessageResponse;
import net.thumbtack.school.forums.dto.response.statistics.ListCountMessagesResponse;
import net.thumbtack.school.forums.dto.response.statistics.ListRatingMessagesResponse;
import net.thumbtack.school.forums.dto.response.statistics.ListRatingUserResponse;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.types.ForumType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class TestStatisticService extends TestServiceBase {
    @Test
    public void testGetCountsMessagesAndComments() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));
        for (int i = 0; i < 11; i++) {
            ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest" + i, ForumType.UNMODERATED));

            MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject" + i, "myBody"));

            if (i == 5) {
                for (int j = 0; j < 10; j++) {
                    messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody" + j));
                }
            }
        }

        ListCountMessagesResponse responseList = (ListCountMessagesResponse) statisticsService.getCountMessages(cookie, null, 0, 1);
        Assert.assertFalse(responseList.getList().isEmpty());
        Assert.assertEquals(1, responseList.getList().size());
        Assert.assertEquals("MyForumTest5", responseList.getList().get(0).getTopic());
        Assert.assertEquals(1, responseList.getList().get(0).getCountMessages());
        Assert.assertEquals(10, responseList.getList().get(0).getCountComments());
    }

    @Test
    public void testGetRatingMessagesOrComments() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));


        String cookie3 = "Test_Cookie3";
        userService.register(cookie3, new RegisterUserRequest("My_login3", "TestName3", "myemail32@mail.ru", "MypasswordTes3t2"));

        for (int i = 0; i < 10; i++) {
            ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest" + i, ForumType.UNMODERATED));

            MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject" + i, "myBody"));

            messageService.addRating(messageResponse.getId(), cookie2, new AddRatingOnMessageRequest(1));
            messageService.addRating(messageResponse.getId(), cookie3, new AddRatingOnMessageRequest(1));
            for (int j = 0; j < 10; j++) {
                MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie3, new AddCommentRequest("TestCommentBody" + j));
                if (j == 5 && i == 5) {
                    //   messageService.addRating(commentResponse.getId(), cookie3, new AddRatingOnMessageRequest((int) (Math.random() * 4 + 1)));
                    messageService.addRating(commentResponse.getId(), cookie2, new AddRatingOnMessageRequest(5));
                }
            }
        }

        ListRatingMessagesResponse responseList = (ListRatingMessagesResponse) statisticsService.getListMessagesByRating(cookie, null, true, 0, 1);
        Assert.assertEquals(1, responseList.getList().size());
        Assert.assertEquals("exampleSubject5", responseList.getList().get(0).getSubject());
        Assert.assertEquals("My_login3", responseList.getList().get(0).getCreator());
        Assert.assertEquals(1, responseList.getList().get(0).getRated());
        Assert.assertEquals(5.0, responseList.getList().get(0).getRating(), 0.0);
    }

    @Test
    public void testGetRatingUser() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));


        String cookie3 = "Test_Cookie3";
        userService.register(cookie3, new RegisterUserRequest("My_login3", "TestName3", "myemail32@mail.ru", "MypasswordTes3t2"));

        for (int i = 0; i < 10; i++) {
            ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest" + i, ForumType.UNMODERATED));

            MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject" + i, "myBody"));

            messageService.addRating(messageResponse.getId(), cookie2, new AddRatingOnMessageRequest(1));
            messageService.addRating(messageResponse.getId(), cookie3, new AddRatingOnMessageRequest(1));
            for (int j = 0; j < 10; j++) {
                MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie3, new AddCommentRequest("TestCommentBody" + j));
                if (j == 5 && i == 5) {
                    //   messageService.addRating(commentResponse.getId(), cookie3, new AddRatingOnMessageRequest((int) (Math.random() * 4 + 1)));
                    messageService.addRating(commentResponse.getId(), cookie2, new AddRatingOnMessageRequest(5));
                }
            }
        }

        ListRatingUserResponse responseList = (ListRatingUserResponse) statisticsService.getListUsersByRating(cookie, null, 0, 2);
        Assert.assertEquals(2, responseList.getList().size());
        Assert.assertEquals("My_login3", responseList.getList().get(0).getCreator());
        Assert.assertEquals("My_login", responseList.getList().get(1).getCreator());
    }
}
