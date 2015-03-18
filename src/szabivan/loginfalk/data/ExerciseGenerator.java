package szabivan.loginfalk.data;

/**
 * Class with a static method to generate a complete exercise with solution of the form
 * "show by resolution that \Sigma \models F".
 * 
 * @author szabivan
 * @version 1.0
 */

import java.util.Set;
import java.util.Vector;

import szabivan.loginfalk.data.Resolver.ClauseReason;

public class ExerciseGenerator {
	private static final FormulaGenerator generator = new FormulaGenerator();
	static {
		// here come the magic constants :P
		generator.setWeight(UnaryConnective.NOT, 1);
		generator.setWeight(AssociativeConnective.OR, 1);
		generator.setWeight(AssociativeConnective.AND, 1);
		generator.setWeight(BinaryConnective.IMPLIES, 1);
		generator.setAtomicWeight(3);
	}
	private static final Resolver resolver = new Resolver();

	/**
	 * Tries to generate a resolution exercise with solution.
	 * 
	 * @param varCount
	 *            maximum number of different variables
	 * @param maxDepth
	 *            maximum depth of the generated formulas
	 * @param minLength
	 *            minimum length of a (shortest? sort of) refutation
	 * @param trials
	 *            number of attempts to generate
	 * @return a complete ResolutionExercise if it succeeds to generate one in
	 *         the specified number of attempts; <code>null</code> otherwise.
	 */
	public static ResolutionExercise generateExercise(int varCount,
			int maxDepth, int minLength, int trials, int minUnits, int maxUnits) {
		generator.setMaxDepth(maxDepth);
		generator.setVarCount(varCount);

		while (trials-- > 0) {
			SigmaFPair input = generator.generateConsequence();
			Set<Integer> sigmaPrime = CnfComputer.createCnf(input);
			int units = 0;
			for (Integer clause : sigmaPrime) {
				if (Resolver.isUnit(clause))
					units++;
			}
			if (units < minUnits)
				continue;
			if (units > maxUnits)
				continue;
			Vector<ClauseReason> solution = resolver
					.constructRefutationVector(resolver.refute(sigmaPrime,
							varCount));
			if (solution.size() >= minLength) {
				return new ResolutionExercise(input, sigmaPrime, solution);
			}
		}
		return null;
	}
}
