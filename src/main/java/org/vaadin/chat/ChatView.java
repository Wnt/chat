package org.vaadin.chat;

import org.vaadin.chat.data.Message;

public class ChatView extends ChatViewDesign {
	public ChatView() {
		nameField.setPlaceholder("Nickname");
		msgField.setPlaceholder("Message");
		nameField.setValueChangeTimeout(0);
	}


	public void renderMessage(Message m) {
		MessageRowDesign msgRow = new MessageRowDesign();
		msgRow.nameLabel.setValue(m.getName());
		msgRow.msgLabel.setValue(m.getMsg());

		messageRows.addComponent(msgRow);
		panel.setScrollTop(Integer.MAX_VALUE);
		
		if (messageRows.getComponentCount() > ChatService.MAX_LOG_SIZE) {
			messageRows.removeComponent(messageRows.getComponent(0));
		}
	}
}