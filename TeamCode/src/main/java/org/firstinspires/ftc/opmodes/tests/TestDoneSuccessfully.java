package org.firstinspires.ftc.opmodes.tests;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

@Documented
@Target({TYPE, METHOD, FIELD, CONSTRUCTOR, LOCAL_VARIABLE, PACKAGE, ANNOTATION_TYPE, TYPE_PARAMETER, TYPE_USE})
public @interface TestDoneSuccessfully {}
