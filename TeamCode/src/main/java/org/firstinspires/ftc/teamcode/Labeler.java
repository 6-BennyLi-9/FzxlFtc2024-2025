/**
 * 包含一个标签生成器的类，用于为对象或原始ID生成唯一的标识字符串。
 */
package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

/**
 * Labeler类用于管理一个内部ID，并提供方法来生成唯一的标识符。
 */
public class Labeler {

	/**
	 * 用于生成唯一标识符的内部ID。
	 */
	private long ID;

	/**
	 * 返回一个递增的ID值。
	 *
	 * @return 生成的唯一long类型的ID。
	 */
	public long summonID() {
		return ++ ID;
	}

	/**
	 * 返回一个包含对象类名和递增ID值的标识符字符串。
	 *
	 * @param object 需要生成标识符的对象。
	 * @return 生成的唯一字符串形式的标识符，格式为"类名:ID"。
	 */
	public String summonID(@NonNull Object object) {
		return "<I>"+object.getClass().getSimpleName() + "@" + summonID();
	}
}
