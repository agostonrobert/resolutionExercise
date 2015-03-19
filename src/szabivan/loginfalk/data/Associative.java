package szabivan.loginfalk.data;

import java.util.Vector;

/**
 * An AssociativeConnective is a Connective whose underlying boolean function is
 * associative, hence it can have an arbitrary (positive) number of subformulas.
 * 
 * @author szabivan
 * @version 1.0
 */
public abstract class Associative extends CompoundFormula {
	protected Associative(Formula[] subformulaArray) {
		super(subformulaArray);
	}

	protected abstract boolean isSameConnective(Associative other);

	protected Formula[] getNormalizedSubFormulas() {
		Vector<Formula> newSubformulas = new Vector<Formula>();

		for (Formula subFormula : this.subformulas) {
			if (subFormula instanceof Associative) {
				Associative subCompound = (Associative) subFormula;
				if (isSameConnective(subCompound)) {
					for (Formula form : subCompound.subformulas) {
						newSubformulas.add(form);
					}
				} else {
					newSubformulas.add(subFormula);
				}
			} else {
				newSubformulas.add(subFormula);
			}
		}

		return newSubformulas.toArray(this.subformulas);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < subformulas.length; i++) {
			if (i > 0)
				builder.append(getConnectiveString());
			if (!subformulas[i].isAtomic()) {
				builder.append('(');
			}
			builder.append(subformulas[i].toString());
			if (!subformulas[i].isAtomic()) {
				builder.append(')');
			}
		}
		return builder.toString();
	}
}
