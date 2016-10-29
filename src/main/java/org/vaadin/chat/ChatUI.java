package org.vaadin.chat;

import javax.servlet.annotation.WebServlet;

import org.vaadin.chat.data.Message;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Registration;
import com.vaadin.ui.UI;

@Theme("chat")
@Push
public class ChatUI extends UI {

	private static ChatService chatService = new ChatService();
	private ChatView chatView;
	private Registration registration;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		Page.getCurrent().setTitle("Chat");
		chatView = new ChatView();
		chatView.nameField.focus();

		chatView.nameField.addValueChangeListener(e -> {

			boolean nameValid = chatView.nameField.getValue().length() > 2;
			chatView.msgField.setEnabled(nameValid);
		});

		for (Message m : chatService.getMessageLog()) {
			chatView.renderMessage(m);
		}

		registration = chatService.addMessageListener(m -> {
			chatView.getUI().access(() -> {
				chatView.renderMessage(m);
			});
		});

		chatView.sendButton.addClickListener(e -> {
			chatView.nameField.setReadOnly(true);
			chatService.addMessage(new Message(chatView.nameField.getValue(), chatView.msgField.getValue()));
			chatView.msgField.setValue("");
			chatView.msgField.focus();

		});

		chatView.msgField.addValueChangeListener(e -> {
			boolean msgValid = chatView.msgField.getValue().length() > 2;
			chatView.sendButton.setEnabled(msgValid);
			chatView.msgField.setValueChangeTimeout(0);
		});

		addShortcutListener(new ShortcutListener("Send", KeyCode.ENTER, null) {

			@Override
			public void handleAction(Object sender, Object target) {
				chatView.sendButton.click();
			}
		});

		setContent(chatView);
	}

	@Override
	public void detach() {
		super.detach();

		registration.remove();
	}

	@WebServlet(urlPatterns = "/*", name = "ChatUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = ChatUI.class, productionMode = true)
	public static class ChatUIServlet extends VaadinServlet {

	}
}
