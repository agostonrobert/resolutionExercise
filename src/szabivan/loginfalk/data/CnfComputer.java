package szabivan.loginfalk.data;

/**
 * Class with several static methods for computing an equivalent CNF for an input
 * - formula,
 * - set of formulas
 * - or a consequence of the form Sigma |= F.
 *
 * @author szabivan
 * @version 1.0
 */

import java.util.HashSet;
import java.util.Set;

public class CnfComputer {
	public static Set<Integer> wedgeCnfs(Set<Integer>[] cnfs) {
		return Clause.veeValuations(cnfs);
	}

	public static Set<Integer> veeCnfs(Set<Integer>[] cnfs) {
		return Clause.wedgeValuations(cnfs);
	}

	/**
	 * Returns a set of ints that is a CNF equivalent to either the given
	 * formula or its complement based on retainValue.
	 * 
	 * Each int describes a clause, details and limitations are given in
	 * {@link ClausePrinter}.
	 * 
	 * @param formula
	 *            The input formula F.
	 * @param retainValue
	 *            Should the CNF be equivalent to F (<code>true</code>) or ¬F (
	 *            <code>false</code>).
	 * @return A CNF equivalent to F if retainValue is <code>true</code> and to
	 *         ¬F otherwise.
	 * 
	 *         Trivial clauses are NOT given back.
	 * 
	 */
	@SuppressWarnings("unchecked")
	// f'ck that "cannot create a generic array" stuff, NO WARNING in my code :P
	public static Set<Integer> createCnf(Formula formula, boolean retainValue) {
		if (formula instanceof TruthVariable) {
			// a TruthVariable is
			TruthVariable truthVariable = (TruthVariable) formula;
			Integer clause = retainValue ? truthVariable.toClause() : truthVariable.toNegatedClause();
			Set<Integer> cnf = new HashSet<Integer>();
			cnf.add(clause);
			return cnf;
		}

		CompoundFormula compoundFormula = (CompoundFormula) formula;

		if (compoundFormula instanceof Not) {
			return createCnf(compoundFormula.getSubFormula(0), !retainValue);
		}
		if (compoundFormula instanceof Implies) {
			if (retainValue) {
				return veeCnfs(new Set[] {
						createCnf(compoundFormula.getSubFormula(0), false),
						createCnf(compoundFormula.getSubFormula(1), true) });
			} else {
				return wedgeCnfs(new Set[] {
						createCnf(compoundFormula.getSubFormula(0), true),
						createCnf(compoundFormula.getSubFormula(1), false) });
			}
		}
		if (compoundFormula instanceof Associative) {
			Set<Integer>[] subcnfs = new Set[compoundFormula
					.getSubFormulaCount()];
			for (int i = 0; i < compoundFormula.getSubFormulaCount(); i++) {
				subcnfs[i] = createCnf(compoundFormula.getSubFormula(i),
						retainValue);
			}
			if (compoundFormula instanceof Or) {
				return retainValue ? veeCnfs(subcnfs) : wedgeCnfs(subcnfs);
			}
			if (compoundFormula instanceof And) {
				return retainValue ? wedgeCnfs(subcnfs) : veeCnfs(subcnfs);
			}
		}
		throw new IllegalArgumentException("CnfComputer: connective " + compoundFormula.getClass().getName()
				+ " is not supported");
	}

//	public static Set<Integer> createCnf(Set<Formula> sigma) {
//		Set<Integer> ret = new HashSet<Integer>();
//		for (Formula form : sigma) {
//			ret.addAll(createCnf(form, true));
//		}
//		return ret;
//	}
}
