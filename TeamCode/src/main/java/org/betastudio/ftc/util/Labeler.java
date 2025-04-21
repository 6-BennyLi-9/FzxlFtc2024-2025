/**
 * 包含一个标签生成器的类，用于为对象或原始ID生成唯一的标识字符串。
 */
package org.betastudio.ftc.util;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Labeler类用于管理一个内部ID，并提供方法来生成唯一的标识符。
 */
public final class Labeler {
	/**
	 * 用于生成唯一标识符的内部ID。
	 */
	private static long ID;

	/**
	 * 返回一个递增的ID值。
	 *
	 * @return 生成的唯一long类型的ID。
	 */
	public static long summon() {
		++ ID;
		return ID;
	}

	/**
	 * 返回一个包含对象类名和递增ID值的标识符字符串。
	 *
	 * @param object 需要生成标识符的对象。
	 * @return 生成的唯一字符串形式的标识符。
	 */
	@NonNull
	public static String summon(@NonNull final Object object) {
		return getClassSign(object.getClass()) + "@" + summon();
	}

	@NonNull
	public static String getClassSign(@NonNull Class<?> clazz){
		if (clazz.isAnnotation()) {
			return "<@" + clazz.getSimpleName() + ">";
		} else if (clazz.isEnum()) {
			return "<E>" + clazz.getSimpleName();
		} else if (clazz.isInterface()) {
			return "<I>" + clazz.getSimpleName();
		} else if (clazz.isArray()) {
			return "<" + Objects.requireNonNull(clazz.getComponentType()).getSimpleName() + "[]>";
		} else {
			return "<C>" + clazz.getSimpleName();
		}
	}
}
