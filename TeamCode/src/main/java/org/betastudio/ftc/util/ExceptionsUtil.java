package org.betastudio.ftc.util;

import androidx.annotation.NonNull;

public final class ExceptionsUtil {
	/**
	 * 用于跟踪异常的真正的原因
	 *
	 * @return 异常的真正的抛出原因，不会有 {@link RuntimeException} 的干扰
	 */
	@NonNull
	public static Throwable getOriginException(@NonNull Throwable e) {
		if (e.getCause() == null) {
			return e;
		} else {
			return getOriginException(e.getCause());
		}
	}
}
