/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.reflection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class DefaultReflectorFactory implements ReflectorFactory {
  private boolean classCacheEnabled = true;
  private final ConcurrentMap<Class<?>, Reflector> reflectorMap = new ConcurrentHashMap<>();

  public DefaultReflectorFactory() {
  }

  @Override
  public boolean isClassCacheEnabled() {
    return classCacheEnabled;
  }

  @Override
  public void setClassCacheEnabled(boolean classCacheEnabled) {
    this.classCacheEnabled = classCacheEnabled;
  }

  /**
   * 在调用这个方法的时候，其实就是初始化对应的type
   * 然后解析type映射到一个对应的Reflector实例
   * 最终存储到这个reflectorMap中
   * @param type
   * @return
   */
  @Override
  public Reflector findForClass(Class<?> type) {
    if (classCacheEnabled) {
      // synchronized (type) removed see issue #461
      /**
       * 下面的用法是Java8中的新特性，就是ConcurrentMap的方法，不是Map中的方法
       * 方法中定义的是Function<? super K, ? extends V> mappingFunction
       * 按照Java8中的新定义，K为参数，V为返回值，而要想使用lmbada表达式，那么lambda的
       * 构造器方法与函数式接口中的抽象方法的参数和返回值必须保持一致
       */
      return reflectorMap.computeIfAbsent(type, Reflector::new);

    } else {
      return new Reflector(type);
    }
  }

}
