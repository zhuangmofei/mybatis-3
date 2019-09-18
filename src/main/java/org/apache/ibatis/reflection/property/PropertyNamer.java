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
package org.apache.ibatis.reflection.property;

import java.util.Locale;

import org.apache.ibatis.reflection.ReflectionException;

/**
 * @author Clinton Begin
 */
public final class PropertyNamer {

  private PropertyNamer() {
    // Prevent Instantiation of Static Class
  }

  /**
   *
   * @param name
   * @return
   */
  public static String methodToProperty(String name) {
    if (name.startsWith("is")) {
      name = name.substring(2);
    } else if (name.startsWith("get") || name.startsWith("set")) {
      name = name.substring(3);
    } else {
      throw new ReflectionException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
    }

    /**
     * 属性名只有一个字符，或者是如果大于1个字节，且第一个字符是大写的话，
     * 将第一个字符转换成小写
     * 已经在github上提了issue
     * 20190918: 在github提的issue已经被关闭了，理由是issue上面只跟踪问题和提出新特性，如果发现有bug，需要按照邮件列表的格式去提出
     *           1.其实此时心里是很不开心的，为啥就不能正视一下我发现的问题，但是后来细想了一下，是不是有哪里我没想到，那根本就不是一个bug呢？
     *           2.回复里面还有一句话：If you get such question when reading OSS code, you should change the code and run the tests.
     *                                 Some tests may fail and you will have your answer if you investigate the cause of the failure
     *                                让我更加坚信，是不是真的有哪里我没想到呢
     *    Key:又仔细看了一下下面的代码，首先!Character.isUpperCase(name.charAt(1)) ，是判断第二个字符是否是大写的
     *        那么如果第二个字符是大写的呢？那是不是就表明这个属性就是这种：ABC  然后get方法就是getABC,所以这个时候就认为属性就是ABC而不需要调整
     *        如果第二个字符是小写的，那就证明get方法改写了第一个字母，所以才要走下面的逻辑，将第一个字符改写成小写
     *
     *    So : 也许是这么考虑的吧
     */
    if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
      name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
    }

    return name;
  }

  public static boolean isProperty(String name) {
    return isGetter(name) || isSetter(name);
  }

  public static boolean isGetter(String name) {
    return (name.startsWith("get") && name.length() > 3) || (name.startsWith("is") && name.length() > 2);
  }

  public static boolean isSetter(String name) {
    return name.startsWith("set") && name.length() > 3;
  }

  public static void main(String[] args) {
    String name = "getJames";
    String str = methodToProperty(name);
    System.out.println(str);
  }

}
