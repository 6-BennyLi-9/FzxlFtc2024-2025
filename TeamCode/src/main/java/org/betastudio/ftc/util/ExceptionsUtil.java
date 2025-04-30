package org.betastudio.ftc.util;

import androidx.annotation.NonNull;

public final class ExceptionsUtil {
	@NonNull
	public static Throwable getOriginException(@NonNull Throwable e) {
		if (e.getCause() == null) {
			return e;
		} else {
			return getOriginException(e.getCause());
		}
	}
}
