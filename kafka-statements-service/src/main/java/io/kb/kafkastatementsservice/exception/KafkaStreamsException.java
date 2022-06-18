package io.kb.kafkastatementsservice.exception;

public class KafkaStreamsException extends RuntimeException {
	
  public KafkaStreamsException(String message) {
    super(message);
  }

  public KafkaStreamsException(String message, Throwable cause) {
    super(message, cause);
  }
}
