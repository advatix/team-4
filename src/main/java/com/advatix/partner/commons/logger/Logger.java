package com.advatix.partner.commons.logger;

import java.io.Serializable;
import java.util.HashMap;

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import com.advatix.partner.commons.utils.Utils;

/**
 * The org.slf4j.Logger interface is the main user entry point of SLF4J API. It is expected that
 * logging takes place through concrete implementations of this interface.
 * <p/>
 * <h3>Typical usage pattern:</h3>
 * 
 * <pre>
 * import org.slf4j.Logger;
 * import org.slf4j.LoggerFactory;
 *
 * public class Wombat {
 *
 *   <span style=
"color:green">final static Logger logger = LoggerFactory.getLogger(Wombat.class);</span>
 *   Integer t;
 *   Integer oldT;
 *
 *   public void setTemperature(Integer temperature) {
 *     oldT = t;
 *     t = temperature;
 *     <span style=
"color:green">logger.debug("Temperature set to {}. Old temperature was {}.", t, oldT);</span>
 *     if(temperature.intValue() > 50) {
 *       <span style="color:green">logger.info("Temperature has risen above 50 degrees.");</span>
 *     }
 *   }
 * }
 * </pre>
 *
 * Be sure to read the FAQ entry relating to
 * <a href="../../../faq.html#logging_performance">parameterized logging</a>. Note that logging
 * statements can be parameterized in <a href="../../../faq.html#paramException">presence of an
 * exception/throwable</a>.
 *
 * <p>
 * Once you are comfortable using loggers, i.e. instances of this interface, consider using
 * <a href="MDC.html">MDC</a> as well as <a href="Marker.html">Markers</a>.
 * </p>
 *
 */
public class Logger implements Serializable {

  private static final long serialVersionUID = 4790314879777420382L;

  private transient org.slf4j.Logger log;

  private static final ThreadLocal<HashMap<String, String>> CONTEXT = new ThreadLocal<>();

  private Logger() {
    super();
  }

  private Logger(org.slf4j.Logger log) {
    super();
    this.log = log;
  }

  /**
   * Return a logger named corresponding to the class passed as parameter, using the statically
   * bound {@link ILoggerFactory} instance.
   * 
   * <p>
   * In case the the <code>clazz</code> parameter differs from the name of the caller as computed
   * internally by SLF4J, a logger name mismatch warning will be printed but only if the
   * <code>slf4j.detectLoggerNameMismatch</code> system property is set to true. By default, this
   * property is not set and no warnings will be printed even in case of a logger name mismatch.
   * 
   * @param clazz the returned logger will be named after clazz
   * @return logger
   * 
   * 
   * @see <a href="http://www.slf4j.org/codes.html#loggerNameMismatch">Detected logger name
   *      mismatch</a>
   */
  public static Logger getLogger(Class<?> className) {
    if (Boolean.TRUE.equals(Utils.checkCollectionIsEmpty(CONTEXT.get()))) {
      CONTEXT.set(new HashMap<>());
    }
    return new Logger(LoggerFactory.getLogger(className));
  }

  /**
   * Log a message at the INFO level.
   *
   * @param msg the message string to be logged
   */
  public void info(String msg) {
    log.info(msg);
  }

  /**
   * Log a message at the INFO level according to the specified format and argument.
   * <p/>
   * <p>
   * This form avoids superfluous object creation when the logger is disabled for the INFO level.
   * </p>
   *
   * @param format the format string
   * @param arg the argument
   */
  public void info(String format, Object arg) {
    log.info(format, arg);
  }

  /**
   * Log a message at the INFO level according to the specified format and arguments.
   * <p/>
   * <p>
   * This form avoids superfluous string concatenation when the logger is disabled for the INFO
   * level. However, this variant incurs the hidden (and relatively small) cost of creating an
   * <code>Object[]</code> before invoking the method, even if this logger is disabled for INFO. The
   * variants taking {@link #info(String, Object) one} and {@link #info(String, Object, Object) two}
   * arguments exist solely in order to avoid this hidden cost.
   * </p>
   *
   * @param format the format string
   * @param arguments a list of 3 or more arguments
   */
  public void info(String format, Object... arguments) {
    log.info(format, arguments);
  }

  public void info(String msg, Throwable t) {
    log.info(msg, t);
  }

  public void error(String msg) {
    log.error(msg);
  }

  public void error(String format, Object arg) {
    log.error(format, arg);
  }

  public void error(String format, Object... arguments) {
    log.error(format, arguments);
  }

  public void error(String msg, Throwable t) {
    log.error(msg, t);
  }

  public void debug(String msg) {
    log.debug(msg);
  }

  public void debug(String format, Object arg) {
    log.debug(format, arg);
  }

  public void debug(String format, Object... arguments) {
    log.debug(format, arguments);
  }

  public void debug(String msg, Throwable t) {
    log.debug(msg, t);
  }

  public void setValue(String key, String value) {
    CONTEXT.get().put(key, value);
  }

  public String getValue(String key) {
    return CONTEXT.get().getOrDefault(key, "");
  }

  public void clearContext() {
    CONTEXT.remove();
  }

}
