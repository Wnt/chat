package org.vaadin.chat;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.vaadin.chat.data.Message;

import com.vaadin.shared.Registration;

public final class ChatService {
	public static final int MAX_LOG_SIZE = 50;

	interface MessageListener {
		void messageReceived(Message m);
	}

	List<Message> messageLog = new CopyOnWriteArrayList<>();
	List<MessageListener> listeners = new CopyOnWriteArrayList<>();

	public Registration addMessageListener(MessageListener e) {
		listeners.add(e);
		return () -> listeners.remove(e);
	}

	public void addMessage(Message m) {
		messageLog.add(m);
		for (MessageListener l : listeners) {
			l.messageReceived(m);
		}
		while (messageLog.size() > MAX_LOG_SIZE) {
			messageLog.remove(0);
		}
	}

	public List<Message> getMessageLog() {
		return messageLog;
	}
	
}