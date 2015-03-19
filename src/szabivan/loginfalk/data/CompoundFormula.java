package szabivan.loginfalk.data;

/**
 * A CompoundFormula is a {@link Connective} and
 * an array of one or more immediate subformulas.
 * 
 * TODO designed to be Immutable but the array can be modified.
 * 
 * @author szabivan
 * @version 1.0
 */

/**
 * Empty interface for the logical connectives. TODO a method like acceptsArity(
 * int ) would be handy for verifying construction attempts of compound
 * formulas. Maybe later.
 * 
 * @see {@link CompoundFormula}
 * 
 * @author szabivan
 * @version 1.0
 */
public abstract class CompoundFormula implements Formula {

	protected abstract String getConnectiveString();

	/**
	 * The array containing the immediate subformulas in order.
	 */
	protected final Formula[] subformulas;

	/**
	 * @param connective
	 *            The top-level connective of the compound formula.
	 * @param subformulaArray
	 *            The immediate subformulas of the formula, in order. ENH arity
	 *            validity is NOT checked for efficiency. ENH not-null values
	 *            are NOT checked for efficiency.
	 */
	protected CompoundFormula(Formula[] subformulaArray) {
		this.subformulas = subformulaArray;
	}

	/**
	 * @return the number of immediate subformulas that are indexed from 0 to
	 *         {@link #getSubFormulaCount()}-1.
	 */
	public int getSubFormulaCount() {
		return subformulas.length;
	}

	/**
	 * @param index
	 *            The index of the requested subformula, should be in 0..
	 *            {@link #getSubFormulaCount()}-1, inclusive.
	 * @return The index-th immediate subformula of the formula.
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the index is outside the bounds.
	 */
	public Formula getSubFormula(int index) {
		return subformulas[index];
	}

	@Override
	/**
	 * A compound formula is not atomic.
	 * @return false.
	 */
	public boolean isAtomic() {
		return false;
	}

	@Override
	/*
	 * "How about writing exception texts containing INFORMATION about the problem, eh?"
	 */
	public abstract String toString();

	@Override
	public Formula normalize() {
		for (int i = 0; i < subformulas.length; i++) {
			subformulas[i] = subformulas[i].normalize();
		}
		return this;
	}
}
