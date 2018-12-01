package kae.demo.transfer.api;

import java.util.Optional;
import org.apache.catalina.startup.Tomcat;

/** */
public class Main {

  public static void main(String[] args) throws Exception {
    String contextPath = "/transfer/v1";
    String appBase = ".";
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(getPort());
    tomcat.getHost().setAppBase(appBase);
    tomcat.addWebapp(contextPath, appBase);
    tomcat.start();
    tomcat.getServer().await();
  }

  private static Integer getPort() {
    return Integer.valueOf(Optional.ofNullable(System.getenv("PORT")).orElse("8080"));
  }
}
