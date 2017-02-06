package chat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.chat.ChatService;
import org.vaadin.chat.data.Message;

public class ChatServiceTest {


    private ChatService chatService;

	@Before
    public void setUp() {
    	chatService = new ChatService();
    }
    
    @Test
    public void testMessagesAreLogged() {
    	Message m = new Message("asd", "foo");
		chatService.addMessage(m);
		Assert.assertTrue("Message log should contain the message just added to there", chatService.getMessageLog().contains(m));
    }
}
