package com.ebay.tdq.planner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author juntzhang
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpressionDescription {

  String usage() default "";

  String extended() default "";

  String arguments() default "";

  String examples() default "";

  String note() default "";

  String since() default "";
}