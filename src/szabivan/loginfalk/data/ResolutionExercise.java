package szabivan.loginfalk.data;

import java.util.Set;
import java.util.Vector;

import szabivan.loginfalk.data.Resolver.ClauseReason;

public class ResolutionExercise {

	public final SigmaFPair input;
	public final Set<Integer> sigmaPrime;
	public final Vector<ClauseReason> solution;

	public ResolutionExercise(SigmaFPair input, Set<Integer> sigmaPrime,
			Vector<ClauseReason> solution) {
		this.input = input;
		this.sigmaPrime = sigmaPrime;
		this.solution = solution;
	}

}
