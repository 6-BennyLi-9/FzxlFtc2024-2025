package org.firstinspires.ftc.opmodes;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AirCombatGameEx extends AirCombatGame {
	// 颜色定义
	private static final String PLAYER_COLOR = "#00FF00";  // 亮绿色
	private static final String ENEMY_COLOR  = "#FF0000";   // 红色
	private static final String BULLET_COLOR = "#FFFF00";  // 黄色
	private static final String BG_COLOR     = "#000000";      // 黑色背景

	// 游戏元素符号（使用HTML实体）
	private static final String PLAYER_SYMBOL = "&#9650;"; // ▲
	private static final String ENEMY_SYMBOL  = "&#9660;";  // ▼
	private static final String BULLET_SYMBOL = "&#9679;"; // ●

	protected void render() {
		telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);
		final StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<style>");
		html.append(".game-grid { border-collapse: collapse; }");
		html.append(".cell { width: 24px; height: 24px; text-align: center; background: ");
		html.append(BG_COLOR);
		html.append("; color: white; }");
		html.append("</style>");
		html.append("<div style='font-family: monospace;'>");//HEAD

		// 构建游戏网格表格
		html.append("<table class='game-grid'>");
		for (int y = 0 ; SCREEN_HEIGHT > y ; y++) {
			html.append("<tr>");
			for (int x = 0 ; SCREEN_WIDTH > x ; x++) {
				String content = "&nbsp;";
				String color   = "white";

				// 检查游戏元素
				if (x == playerX && y == playerY) {
					content = PLAYER_SYMBOL;
					color = PLAYER_COLOR;
				} else {
					for (final Enemy enemy : enemies) {
						if (enemy.x == x && enemy.y == y) {
							content = ENEMY_SYMBOL;
							color = ENEMY_COLOR;
							break;
						}
					}
					for (final Bullet bullet : bullets) {
						if (bullet.x == x && bullet.y == y) {
							content = BULLET_SYMBOL;
							color = BULLET_COLOR;
							break;
						}
					}
				}

				html.append("<td class='cell' style='color:");
				html.append(color);
				html.append("'>");
				html.append(content);
				html.append("</td>");
			}
			html.append("</tr>");
		}
		html.append("</table>");

		// 游戏状态信息
		html.append("<p style='color:");
		html.append(0 < lives ? "#00FF00" : "#FF0000");
		html.append("'>Lives: ");
		html.append(lives);
		html.append("</p>");
		html.append("<p style='color:#FFFF00'>Score: ");
		html.append(score);
		html.append("</p>");
		html.append("<p>Time: ");
		html.append((int) gameTimer.seconds());
		html.append("s</p>");

		if (0 >= lives) {
			html.append("<h1 style='color:#FF0000; text-align:center;'>GAME OVER!</h1>");
		}

		html.append("</div></html>");

		telemetry.addLine(html.toString());
		telemetry.update();
	}
}
