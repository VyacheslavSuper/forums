package net.thumbtack.school.forums.integrationtest.base;

import net.thumbtack.school.forums.dto.request.RequestBase;
import net.thumbtack.school.forums.dto.request.forums.CreateForumRequest;
import net.thumbtack.school.forums.dto.request.messages.*;
import net.thumbtack.school.forums.dto.request.users.LoginUserRequest;
import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.dto.response.forum.ForumResponse;
import net.thumbtack.school.forums.dto.response.forum.FullInfoForumResponse;
import net.thumbtack.school.forums.dto.response.message.MessageResponse;
import net.thumbtack.school.forums.model.types.Decision;
import net.thumbtack.school.forums.model.types.ForumType;
import net.thumbtack.school.forums.model.types.MessageState;
import org.junit.Assert;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestBase {
    protected RestTemplate restTemplate = new RestTemplate();

    @LocalServerPort
    private int port;

    protected String getRootUrl() {
        return "http://localhost:" + port;
    }

    //-------------------------------User----------------------------
    protected String registerUser(int status, String login) {
        try {
            RegisterUserRequest registerUserRequest = new RegisterUserRequest(login, "TestName", "mytestemail@mail.ru", "MyGoodPasswordTest");
            HttpEntity<RegisterUserRequest> request = new HttpEntity<>(registerUserRequest);
            HttpEntity<ResponseBase> response = restTemplate.exchange(getRootUrl() + "/api/users", HttpMethod.POST, request, ResponseBase.class);
            HttpHeaders headers = response.getHeaders();
            String headerCookie = headers.getFirst(HttpHeaders.SET_COOKIE);
            Assert.assertNotNull(headerCookie);
            return headerCookie.substring(headerCookie.indexOf("=") + 1, headerCookie.indexOf(" ") - 1);
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
            return null;
        }
    }

    protected void deleteUser(int status, String cookie) {
        try {
            HttpEntity request = new HttpEntity<>(new RequestBase(), getHeaders(cookie));
            HttpEntity<ResponseBase> response = restTemplate.exchange(getRootUrl() + "/api/users", HttpMethod.DELETE, request, ResponseBase.class);
            ResponseBase responseBase = response.getBody();
            Assert.assertNotNull(responseBase);
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
        }
    }

    //---------------------------session------------------------------------

    protected String login(int status, String login) {
        try {
            LoginUserRequest loginUserRequest = new LoginUserRequest(login, "MyGoodPasswordTest");
            HttpEntity<LoginUserRequest> request = new HttpEntity<>(loginUserRequest);
            HttpEntity<ResponseBase> response = restTemplate.exchange(getRootUrl() + "/api/sessions", HttpMethod.POST, request, ResponseBase.class);
            HttpHeaders headers = response.getHeaders();
            String headerCookie = headers.getFirst(HttpHeaders.SET_COOKIE);
            Assert.assertNotNull(headerCookie);
            return headerCookie.substring(headerCookie.indexOf("=") + 1, headerCookie.indexOf(" ") - 1);
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
            return null;
        }
    }

    protected void logout(int status, String cookie) {
        try {
            HttpEntity request = new HttpEntity<>(new RequestBase(), getHeaders(cookie));
            HttpEntity<ResponseBase> response = restTemplate.exchange(getRootUrl() + "/api/sessions", HttpMethod.DELETE, request, ResponseBase.class);
            ResponseBase responseBase = response.getBody();
            Assert.assertNotNull(responseBase);
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
        }
    }

    //---------------------------forum--------------------------------------
    protected Integer createForum(int status, String cookie, String topic, ForumType type) {
        try {
            CreateForumRequest createForumRequest = new CreateForumRequest(topic, type);
            HttpEntity<CreateForumRequest> request = new HttpEntity<>(createForumRequest, getHeaders(cookie));
            HttpEntity<ForumResponse> response = restTemplate.exchange(getRootUrl() + "/api/forums", HttpMethod.POST, request, ForumResponse.class);
            ForumResponse forumResponse = response.getBody();
            Assert.assertNotNull(forumResponse);
            Assert.assertNotEquals(0, forumResponse.getId());
            Assert.assertEquals(topic, forumResponse.getTopic());
            Assert.assertEquals(type, forumResponse.getType());
            return forumResponse.getId();
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
            return null;
        }
    }

    protected Integer createMessage(int status, String cookie, int idForum, String topic, String body, MessageState state) {
        try {
            CreateMessageRequest createMessageRequest = new CreateMessageRequest(topic, body);
            HttpEntity<CreateMessageRequest> request = new HttpEntity<>(createMessageRequest, getHeaders(cookie));
            HttpEntity<MessageResponse> response = restTemplate.exchange(getRootUrl() + String.format("/api/forums/%d/messages", idForum), HttpMethod.POST, request, MessageResponse.class);
            MessageResponse messageResponse = response.getBody();
            Assert.assertNotNull(messageResponse);
            Assert.assertNotEquals(0, messageResponse.getId());
            Assert.assertEquals(state, messageResponse.getState());
            return messageResponse.getId();
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
            return null;
        }
    }

    protected void getForum(int status, String cookie, int idForum, int countMessages, int countComments) {
        try {
            HttpEntity request = new HttpEntity<>(new ResponseBase(), getHeaders(cookie));
            HttpEntity<FullInfoForumResponse> response = restTemplate.exchange(getRootUrl() + String.format("/api/forums/%d", idForum), HttpMethod.GET, request, FullInfoForumResponse.class);
            FullInfoForumResponse fullForumResponse = response.getBody();
            Assert.assertNotNull(fullForumResponse);
            Assert.assertNotEquals(0, fullForumResponse.getId());
            Assert.assertEquals(idForum, fullForumResponse.getId());
            Assert.assertEquals(countComments, fullForumResponse.getCountComments());
            Assert.assertEquals(countMessages, fullForumResponse.getCountMessages());
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());

        }
    }

    protected void deleteForum(int status, String cookie, int idForum) {
        try {
            HttpEntity request = new HttpEntity<>(new RequestBase(), getHeaders(cookie));
            HttpEntity<ResponseBase> response = restTemplate.exchange(getRootUrl() + String.format("/api/forums/%d", idForum), HttpMethod.DELETE, request, ResponseBase.class);
            ResponseBase responseBase = response.getBody();
            Assert.assertNotNull(responseBase);
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
        }
    }

    //-----------------------messages------------------------------------------------
    protected Integer createComment(int status, String cookie, int idMessage, String body, MessageState state) {
        try {
            AddCommentRequest addCommentRequest = new AddCommentRequest(body);
            HttpEntity<AddCommentRequest> request = new HttpEntity<>(addCommentRequest, getHeaders(cookie));
            HttpEntity<MessageResponse> response = restTemplate.exchange(getRootUrl() + String.format("/api/messages/%d", idMessage), HttpMethod.POST, request, MessageResponse.class);
            MessageResponse messageResponse = response.getBody();
            Assert.assertNotNull(messageResponse);
            Assert.assertNotEquals(0, messageResponse.getId());
            Assert.assertEquals(state, messageResponse.getState());
            return messageResponse.getId();
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
            return null;
        }
    }

    protected Integer changeMessage(int status, String cookie, int idMessage, String body, MessageState state) {
        try {
            ChangeMessageRequest changeMessageRequest = new ChangeMessageRequest(body);
            HttpEntity<ChangeMessageRequest> request = new HttpEntity<>(changeMessageRequest, getHeaders(cookie));
            HttpEntity<MessageResponse> response = restTemplate.exchange(getRootUrl() + String.format("/api/messages/%d", idMessage), HttpMethod.PUT, request, MessageResponse.class);
            MessageResponse messageResponse = response.getBody();
            Assert.assertNotNull(messageResponse);
            Assert.assertNotEquals(0, messageResponse.getId());
            Assert.assertEquals(idMessage, messageResponse.getId());
            Assert.assertEquals(state, messageResponse.getState());
            return messageResponse.getId();
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
            return null;
        }
    }

    protected Integer convertComment(int status, String cookie, int idMessage, String topic, MessageState state) {
        try {
            CommentIntoMessageRequest commentIntoMessageRequest = new CommentIntoMessageRequest(topic, null, null);
            HttpEntity<CommentIntoMessageRequest> request = new HttpEntity<>(commentIntoMessageRequest, getHeaders(cookie));
            HttpEntity<MessageResponse> response = restTemplate.exchange(getRootUrl() + String.format("/api/messages/%d/up", idMessage), HttpMethod.PUT, request, MessageResponse.class);
            MessageResponse messageResponse = response.getBody();
            Assert.assertNotNull(messageResponse);
            Assert.assertNotEquals(0, messageResponse.getId());
            Assert.assertEquals(idMessage, messageResponse.getId());
            Assert.assertEquals(state, messageResponse.getState());
            return messageResponse.getId();
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
            return null;
        }
    }

    protected void publishMessage(int status, String cookie, int idMessage, Decision decision) {
        try {
            PublishMessageRequest publishMessageRequest = new PublishMessageRequest(decision);
            HttpEntity request = new HttpEntity<>(publishMessageRequest, getHeaders(cookie));
            HttpEntity<ResponseBase> response = restTemplate.exchange(getRootUrl() + String.format("/api/messages/%d/publish", idMessage), HttpMethod.PUT, request, ResponseBase.class);
            ResponseBase responseBase = response.getBody();
            Assert.assertNotNull(responseBase);
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
        }
    }


    protected void addRatingMessage(int status, String cookie, int idMessage, Integer value) {
        try {
            AddRatingOnMessageRequest addRatingOnMessageRequest = new AddRatingOnMessageRequest(value);
            HttpEntity request = new HttpEntity<>(addRatingOnMessageRequest, getHeaders(cookie));
            HttpEntity<ResponseBase> response = restTemplate.exchange(getRootUrl() + String.format("/api/messages/%d/rating", idMessage), HttpMethod.POST, request, ResponseBase.class);
            ResponseBase responseBase = response.getBody();
            Assert.assertNotNull(responseBase);
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
        }
    }

    protected void deleteMessage(int status, String cookie, int idMessage) {
        try {
            HttpEntity request = new HttpEntity<>(new RequestBase(), getHeaders(cookie));
            HttpEntity<ResponseBase> response = restTemplate.exchange(getRootUrl() + String.format("/api/messages/%d", idMessage), HttpMethod.DELETE, request, ResponseBase.class);
            ResponseBase responseBase = response.getBody();
            Assert.assertNotNull(responseBase);
        } catch (HttpServerErrorException | HttpClientErrorException exc) {
            assertEquals(status, exc.getStatusCode().value());
        }
    }


    //------------------------------debug--------------------------------------
    protected void clear() {
        try {
            HttpEntity<ResponseBase> response = restTemplate.exchange(getRootUrl() + "/api/debug/clear", HttpMethod.POST, new HttpEntity<>(new RequestBase()), ResponseBase.class);
            Assert.assertTrue(response.hasBody());
        } catch (HttpServerErrorException exc) {
            assertEquals(500, exc.getStatusCode().value());
        }
    }

    //---------------------------other------------------------------

    private HttpHeaders getHeaders(String cookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "JAVASESSIONID=" + cookie);
        return headers;
    }
}
