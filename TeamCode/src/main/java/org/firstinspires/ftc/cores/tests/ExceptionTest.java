package org.firstinspires.ftc.cores.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.cores.eventloop.IntegralOpMode;
import org.firstinspires.ftc.cores.eventloop.TerminateReason;
import org.firstinspires.ftc.teamcode.Local;
import org.firstinspires.ftc.teamcode.events.IntegralThreadExceptionHandler;

@Autonomous(group = "9_Tests")
public class ExceptionTest extends LinearOpMode implements IntegralOpMode {
	private Exception e;

	@Override
	public void runOpMode() throws InterruptedException {
		sleep(5000);

		startResolvedThread();

		Local.waitForVal(()->e==null&&opModeIsActive(),false);
		throw new RuntimeException(e);
	}

	public void startNonResolverThread(){
		new Thread(()->{
			sleep(5000);
			throw new RuntimeException();
		}).start();
	}
	public void startResolvedThread(){
		Thread t=new Thread(()->{
			sleep(5000);
			throw new RuntimeException();
		});

		t.setUncaughtExceptionHandler(new IntegralThreadExceptionHandler());
		t.start();
	}

	@Override
	public void sendTerminateSignal(TerminateReason reason) {
		sendTerminateSignal(reason,new NullPointerException());
	}

	@Override
	public void sendTerminateSignal(TerminateReason reason, Exception e) {
		this.e=e;
	}
}
