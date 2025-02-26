package org.betastudio.ftc;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

public final class Annotations {
	@Documented
	@Target(TYPE)
	public @interface TestDoneSuccessfully {}

	@Documented
	@Target(TYPE)
	public @interface TestShelved {}
}
