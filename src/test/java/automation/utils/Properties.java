package automation.utils;

public class Properties {
  private static final String DEFAULT_BASE_URL = "http://prestashop-automation.qatestlab.com.ua/";
  private static final String DEFAULT_BASE_ADMIN_URL = "http://prestashop-automation.qatestlab.com.ua/admin147ajyvk0/";

  public static String getBaseUrl() {
    return System.getProperty(EnvironmentVariables.BASE_URL.toString(), DEFAULT_BASE_URL);
  }

  public static  String  getBaseAdminUrl() {
    return  System.getProperty(EnvironmentVariables.BASE_ADMIN_URL.toString(),DEFAULT_BASE_ADMIN_URL);
  }

}

enum EnvironmentVariables {
  BASE_URL("env.url"),
  BASE_ADMIN_URL("env.admin.url");

  private String value;
  EnvironmentVariables(String value) {this.value = value;}

  @Override
  public String toString() {return value;}
}