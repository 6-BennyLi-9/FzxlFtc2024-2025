package org.firstinspires.ftc.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@TeleOp(name = "Air Combat Game", group = Character.MAX_VALUE+"DEMO")
public class AirCombatGame extends LinearOpMode {
	// 游戏区域尺寸
	public static final int SCREEN_WIDTH  = 20;
	public static final int SCREEN_HEIGHT = 10;

	// 游戏元素符号
	private static final String PLAYER_SYMBOL = "A";
	private static final String ENEMY_SYMBOL  = "E";
	private static final String BULLET_SYMBOL = "|";

	// 玩家属性
	protected int playerX = SCREEN_WIDTH / 2;
	protected int playerY = SCREEN_HEIGHT - 1;
	protected int lives   = 3;
	protected int score;

	// 游戏对象列表
	protected final ArrayList <Enemy>  enemies = new ArrayList <>();
	protected final ArrayList <Bullet> bullets = new ArrayList <>();

	// 计时器
	protected final ElapsedTime enemySpawnTimer = new ElapsedTime();
	protected final ElapsedTime gameTimer       = new ElapsedTime();

	@Override
	public void runOpMode() throws InterruptedException {
		telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
		telemetry.addLine("Air Combat Game");
		telemetry.addLine("Press Start to begin.");
		telemetry.update();
		waitForStart();
		gameTimer.reset();
		enemySpawnTimer.reset();

		while (opModeIsActive()) {
			// 1. 处理输入
			processInput();

			// 2. 更新游戏状态
			updateGame();

			// 3. 渲染画面
			render();

			// 4. 控制游戏速度
			sleep(100);
		}
	}

	protected void processInput() {
		// 使用左摇杆控制移动
		final float stickX = gamepad1.left_stick_x;
		final float stickY = gamepad1.left_stick_y;

		// 移动玩家（限制在屏幕范围内）
		playerX = Math.min(Math.max(playerX + (int) (stickX * 2), 0), SCREEN_WIDTH - 1);
		playerY = Math.min(Math.max(playerY - (int) (stickY * 2), 0), SCREEN_HEIGHT - 1);

		// 按A键发射子弹
		if (gamepad1.a) {
			bullets.add(new Bullet(playerX, playerY - 1));
		}
	}

	protected void updateGame() {
		// 生成敌人（每2秒生成一个）
		if (2 < enemySpawnTimer.seconds()) {
			enemies.add(new Enemy(SCREEN_WIDTH - 1, (int) (Math.random() * SCREEN_HEIGHT)));
			enemySpawnTimer.reset();
		}

		// 更新敌人位置
		Iterator <Enemy> enemyIterator = enemies.iterator();
		while (enemyIterator.hasNext()) {
			final Enemy enemy = enemyIterator.next();
			enemy.x--;

			// 碰撞检测（玩家 vs 敌人）
			if (enemy.x == playerX && enemy.y == playerY) {
				lives--;
				enemyIterator.remove();
				continue;
			}

			// 移出屏幕的敌人
			if (0 > enemy.x) {
				enemyIterator.remove();
			}
		}

		// 更新子弹位置
		final Iterator <Bullet> bulletIterator = bullets.iterator();
		while (bulletIterator.hasNext()) {
			final Bullet bullet = bulletIterator.next();
			bullet.y--;

			// 子弹碰撞检测
			enemyIterator = enemies.iterator();
			while (enemyIterator.hasNext()) {
				final Enemy enemy = enemyIterator.next();
				if (bullet.x == enemy.x && bullet.y == enemy.y) {
					score += 100;
					enemyIterator.remove();
					bulletIterator.remove();
					break;
				}
			}

			// 移出屏幕的子弹
			if (0 > bullet.y) {
				bulletIterator.remove();
			}
		}
	}

	protected void render() {
		// 初始化屏幕缓冲区
		final char[][] screen = new char[SCREEN_HEIGHT][SCREEN_WIDTH];
		for (final char[] row : screen) {
			Arrays.fill(row, ' ');
		}

		// 绘制玩家
		screen[playerY][playerX] = PLAYER_SYMBOL.charAt(0);

		// 绘制敌人
		for (final Enemy enemy : enemies) {
			if (0 <= enemy.x && SCREEN_WIDTH > enemy.x && 0 <= enemy.y && SCREEN_HEIGHT > enemy.y) {
				screen[enemy.y][enemy.x] = ENEMY_SYMBOL.charAt(0);
			}
		}

		// 绘制子弹
		for (final Bullet bullet : bullets) {
			if (0 <= bullet.y && SCREEN_HEIGHT > bullet.y) {
				screen[bullet.y][bullet.x] = BULLET_SYMBOL.charAt(0);
			}
		}

		// 渲染到Telemetry
		telemetry.clear();
		for (int y = 0 ; SCREEN_HEIGHT > y ; y++) {
			telemetry.addLine(new String(screen[y]));
		}

		// 显示游戏信息
		telemetry.addLine("Lives: " + lives + "  Score: " + score + "  Time: " + (int) gameTimer.seconds());
		if (0 >= lives) {
			telemetry.addLine("GAME OVER!");
		}
		telemetry.update();
	}

	// 敌人类
	public static class Enemy {
		public       int x;
		public final int y;

		Enemy(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
	}

	// 子弹类
	public static class Bullet {
		public final int x;
		public       int y;

		Bullet(final int x, final int y) {
			this.x = x;
			this.y = y;
		}
	}
}