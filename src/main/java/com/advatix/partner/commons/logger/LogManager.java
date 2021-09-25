/*
 * @author Advatix
 * 
 * @since 2019
 * 
 * @version 1.0
 */
package com.advatix.partner.commons.logger;

import java.util.TimeZone;

/**
 * The Class LogManager.
 */
public class LogManager {

  /**
   * Instantiates a new log manager.
   */
  private LogManager() {
    super();
  }

  /**
   * Info.
   *
   * @param obj the obj
   * @param timezone the timezone
   * @param message the message
   * @param messageType the message type
   */
  public static void info(Class<?> obj, TimeZone timezone, String message,
      MessageType messageType) {
    Logger.getLogger(obj).info("{} - {} - {}", messageType.getLogName(Logger.getLogger(obj)),
        timezone.getID(), message);
  }

  /**
   * Info.
   *
   * @param obj the obj
   * @param message the message
   * @param messageType the message type
   */
  public static void info(Class<?> obj, String message, MessageType messageType) {
    Logger.getLogger(obj).info("{} - {}", messageType.getLogName(Logger.getLogger(obj)), message);
  }

  /**
   * Debug.
   *
   * @param obj the obj
   * @param timezone the timezone
   * @param message the message
   * @param messageType the message type
   */
  public static void debug(Class<?> obj, TimeZone timezone, String message,
      MessageType messageType) {
    Logger.getLogger(obj).debug("{} - {} - {}", messageType.getLogName(Logger.getLogger(obj)),
        timezone.getID(), message);
  }

  /**
   * Debug.
   *
   * @param obj the obj
   * @param message the message
   * @param messageType the message type
   */
  public static void debug(Class<?> obj, String message, MessageType messageType) {
    Logger.getLogger(obj).debug("{} - {}", messageType.getLogName(Logger.getLogger(obj)), message);
  }

  /**
   * Error.
   *
   * @param obj the obj
   * @param timezone the timezone
   * @param message the message
   * @param messageType the message type
   */
  public static void error(Class<?> obj, TimeZone timezone, String message,
      MessageType messageType) {
    Logger.getLogger(obj).error("{} - {} - {}", messageType.getLogName(Logger.getLogger(obj)),
        timezone.getID(), message);
  }

  /**
   * Error.
   *
   * @param obj the obj
   * @param message the message
   * @param messageType the message type
   */
  public static void error(Class<?> obj, String message, MessageType messageType) {
    Logger.getLogger(obj).error("{} - {}", messageType.getLogName(Logger.getLogger(obj)), message);
  }

  /**
   * Error.
   *
   * @param obj the obj
   * @param message the message
   * @param messageType the message type
   */
  public static void error(Class<?> obj, Object message, MessageType messageType) {
    Logger.getLogger(obj).error("{} - {}", messageType.getLogName(Logger.getLogger(obj)), message);
  }

}
