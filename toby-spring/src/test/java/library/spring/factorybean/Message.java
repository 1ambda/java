package library.spring.factorybean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Message {
	@Value("Example")
	private String text;

	public Message() {}

	public Message(String text) {
		this.text = text;
	}

	public String getMessage() {
		return this.text;
	}

	static public Message getMessage(String text) {
		return new Message(text);
	}
}
