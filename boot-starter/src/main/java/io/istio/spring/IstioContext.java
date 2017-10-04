package io.istio.spring;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

final class IstioContext {
  private static final ThreadLocal<IstioContext> currentCommand =
      new ThreadLocal<IstioContext>() {
        @Override protected IstioContext initialValue() {
          return new IstioContext();
        }
      };

  static IstioContext getInstance() {
    return currentCommand.get();
  }


  private HystrixCommand commandConfig;

  void init(HystrixCommand commandConfig) {
    this.commandConfig = commandConfig;
  }

  HystrixCommand getCommandConfig() {
    return commandConfig;
  }

  void clear() {
    this.commandConfig = null;
  }
}
