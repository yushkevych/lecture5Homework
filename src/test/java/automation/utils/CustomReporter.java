package automation.utils;

import org.testng.Reporter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CustomReporter {
  private CustomReporter() {
  }

  /**
   *
   * Logs ation step
   */
  public static void logAction(String message) {
    Reporter.log(String
            .format("[%-12s] ACTION: %s <br>", LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME), message));
  }

  /**
   * Logs simple step
   */
  public static void log(String message) {
    Reporter.log(String
            .format("[%-12s] %s <br>", LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME), message));
  }
}
