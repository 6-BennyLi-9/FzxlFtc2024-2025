package org.betastudio.ftc;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public final class Annotations {
	@Documented
	@Target({ElementType.TYPE_USE, TYPE})
	public @interface TestSucceed {}

	@Documented
	@Target(TYPE)
	public @interface TestShelved {}

	@Documented
	@Target({TYPE, METHOD, FIELD, CONSTRUCTOR})
	public @interface Beta {
		String date();
	}

	@Documented
	@Target({METHOD, CONSTRUCTOR})
	public @interface MirrorMethod {}
}
