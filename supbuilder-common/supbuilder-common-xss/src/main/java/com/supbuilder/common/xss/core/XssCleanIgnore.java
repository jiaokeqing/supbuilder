
package com.supbuilder.common.xss.core;

import java.lang.annotation.*;

/**
 * 忽略 xss
 *
 * @author L.cm
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XssCleanIgnore {

	/**
	 * @return 需要跳过的字段列表
	 */
	String[] value() default {};

}
