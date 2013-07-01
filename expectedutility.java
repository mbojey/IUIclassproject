package foo;

public class expectedutility {
	public static boolean action = false;
	public static int[][][] reward = { { { -5, 7 }, { 10, -10 } },
			{ { -5, 7 }, { -2, 5 } } };

	// {{{(Boss,Working,Open),(Boss,Working,Nothing)}
	// {(Boss,NotWOrking,Open),(Boss,NotWorking,Nothing)}}
	// {{(NotBoss,Working,Open),(NotBoss,Working,Nothing)}
	// {(NotBoss,NotWorking,Open),(NotBoss,NotWorking,Nothing)}}}
	public static void determineaction(double isBoss, double isntBoss,
			double working, double notworking) {
		double act = 0, nothing = 0;
		nothing = isBoss * working * reward[0][0][1] + isBoss * notworking
				* reward[0][1][1] + isntBoss * working * reward[1][0][1]
				+ isntBoss * notworking * reward[1][1][1];
		System.out.println("Nothing "+nothing);
		act = isBoss * working * reward[0][0][0] + isBoss * notworking
				* reward[0][1][0] + isntBoss * working * reward[1][0][0]
				+ isntBoss * notworking * reward[1][1][0];
		System.out.println("Open "+act);
		action = act > nothing ? true : false;

	}

}
