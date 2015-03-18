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

import java.util.Vector;

public class CompoundFormula implements Formula {
	/**
	 * The top-level {@link Connective} of the formula.
	 */
	private final Connective connective;
	/**
	 * The array containing the immediate subformulas in order.
	 */
	private final Formula[] subformulas;

	/**
	 * @param connective
	 *            The top-level connective of the compound formula.
	 * @param subformulaArray
	 *            The immediate subformulas of the formula, in order. ENH arity
	 *            validity is NOT checked for efficiency. ENH not-null values
	 *            are NOT checked for efficiency.
	 */
	public CompoundFormula(Connective connective, Formula[] subformulaArray) {
		this.connective = connective;
		this.subformulas = subformulaArray;
	}

	/**
	 * @return the top-level connective of the formula.
	 */
	public Connective getConnective() {
		return this.connective;
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
	/**
	 * Returns a human-readable string representation of the formula.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (connective instanceof UnaryConnective) {
			builder.append(connective.toString());
			if (!subformulas[0].isAtomic())
				builder.append('(');
			builder.append(subformulas[0].toString());
			if (!subformulas[0].isAtomic())
				builder.append(')');
		} else if (connective instanceof BinaryConnective) {
			if (!subformulas[0].isAtomic())
				builder.append('(');
			builder.append(subformulas[0].toString());
			if (!subformulas[0].isAtomic())
				builder.append(')');
			builder.append(connective.toString());
			if (!subformulas[1].isAtomic())
				builder.append('(');
			builder.append(subformulas[1].toString());
			if (!subformulas[1].isAtomic())
				builder.append(')');
		} else if (connective instanceof AssociativeConnective) {
			for (int i = 0; i < subformulas.length; i++) {
				if (i > 0)
					builder.append(connective.toString());
				if (!subformulas[i].isAtomic()) {
					builder.append('(');
				}
				builder.append(subformulas[i].toString());
				if (!subformulas[i].isAtomic()) {
					builder.append(')');
				}
			}
		} else
			throw new UnsupportedOperationException(
					"How about writing exception texts containing INFORMATION about the problem, eh?");
		return builder.toString();
	}

	@Override
	public Formula normalize() {
		for (int i = 0; i < subformulas.length; i++) {
			subformulas[i] = subformulas[i].normalize();
		}
		if (this.connective == UnaryConnective.NOT) {
			if (subformulas[0] instanceof CompoundFormula) {
				CompoundFormula subFormula = (CompoundFormula) subformulas[0];
				if (subFormula.getConnective() == UnaryConnective.NOT) {
					return subFormula.getSubFormula(0);
				}
			}
			return this;
		}
		if (this.connective instanceof AssociativeConnective) {
			Vector<Formula> newSubformulas = new Vector<Formula>();
			for (Formula subFormula : this.subformulas) {
				if (subFormula instanceof CompoundFormula) {
					CompoundFormula subCompound = (CompoundFormula) subFormula;
					if (subCompound.getConnective() == this.connective) {
						for (Formula form : subCompound.subformulas) {
							newSubformulas.add(form);
						}
					} else
						newSubformulas.add(subFormula);
				} else
					newSubformulas.add(subFormula);
			}
			return new CompoundFormula(this.connective,
					newSubformulas.toArray(this.subformulas));
		}
		return this;
	}
}
