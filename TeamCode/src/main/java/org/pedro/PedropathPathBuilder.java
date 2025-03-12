package org.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

public class PedropathPathBuilder {
	private final Follower follower;
	private       Pose     current;

	public PedropathPathBuilder(Follower follower) {
		this(follower, new Pose(0, 0));
	}

	public PedropathPathBuilder(Follower follower, Pose initialPose) {
		this.follower = follower;
		this.current = initialPose;
	}

	public PathChain lineTo(Pose target) {
		PathBuilder builder = follower.pathBuilder();
		builder.addPath(new BezierLine(new Point(current), new Point(target)));
		builder.setLinearHeadingInterpolation(current.getHeading(), target.getHeading());
		PathChain build = builder.build();
		current = target;
		return build;
	}

	public void setCurrent(Pose current) {
		this.current = current;
	}
}
