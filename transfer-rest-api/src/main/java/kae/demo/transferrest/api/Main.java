package kae.demo.transferrest.api;

import org.apache.catalina.startup.Tomcat;

import java.util.Optional;

/**
 *
 */
public class Main {

  public static void main(String[] args) throws Exception {
    String contextPath = "/";
    String appBase = ".";
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(getPort());
    tomcat.getHost().setAppBase(appBase);
    tomcat.addWebapp(contextPath, appBase);
    tomcat.start();
    tomcat.getServer().await();
  }

  private static Integer getPort() {
    return Integer.valueOf(Optional.ofNullable(System.getenv("PORT")).orElse("8080") );
  }

}
