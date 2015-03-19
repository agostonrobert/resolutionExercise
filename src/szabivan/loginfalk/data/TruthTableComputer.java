package szabivan.loginfalk.data;

/**
 * Class with static methods calculating a DNF for a formula and with operations on DNFs.
 * 
 * @author szabivan
 * @version 1.0
 */

import java.util.Collections;
import java.util.Set;

public class TruthTableComputer {
	@SuppressWarnings("unchecked")
	public static Set<Integer> getValuations(Formula formula, boolean value) {
		if (formula instanceof TruthVariable) {
			int varIndex = ((TruthVariable) formula).varIndex;
			Integer clause = value ? 1 << (varIndex << 1)
					: 1 << ((varIndex << 1) | 1);
			return Collections.singleton(clause);
		}
		
		CompoundFormula compoundFormula = (CompoundFormula) formula;

		if (compoundFormula instanceof Not) {
			return getValuations(compoundFormula.getSubFormula(0), !value);
		}

		if ((compoundFormula instanceof Or && value) || (compoundFormula instanceof And && !value)) {
			Set<Integer>[] valuations = new Set[compoundFormula
					.getSubFormulaCount()];
			for (int i = 0; i < valuations.length; i++) {
				valuations[i] = getValuations(compoundFormula.getSubFormula(i),
						value);
			}
			return Clause.veeValuations(valuations);
		}

		if ((compoundFormula instanceof Or && !value) || (compoundFormula instanceof And && value)) {
			Set<Integer>[] valuations = new Set[compoundFormula
					.getSubFormulaCount()];
			for (int i = 0; i < valuations.length; i++) {
				valuations[i] = getValuations(compoundFormula.getSubFormula(i),
						value);
			}
			return Clause.wedgeValuations(valuations);
		}

		if (compoundFormula instanceof Implies && value) {
			Set<Integer> leftSet = getValuations(
					compoundFormula.getSubFormula(0), false);
			Set<Integer> rightSet = getValuations(
					compoundFormula.getSubFormula(1), true);
			return Clause.veeValuations(new Set[] { leftSet, rightSet });
		}

		if (compoundFormula instanceof Implies && !value) {
			Set<Integer> leftSet = getValuations(
					compoundFormula.getSubFormula(0), true);
			Set<Integer> rightSet = getValuations(
					compoundFormula.getSubFormula(1), false);
			return Clause.wedgeValuations(new Set[] { leftSet, rightSet });
		}

		throw new UnsupportedOperationException("Connective " + compoundFormula.getClass().getName()
				+ " is not supported");
	}
}
