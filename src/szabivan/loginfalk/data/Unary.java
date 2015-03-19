package szabivan.loginfalk.data;

/**
 * A UnaryConnective is a Connective which can have exactly one subformula.
 * 
 * Currently only the negation (NOT) is unary.
 * 
 * TODO extending to first-order, should be quantifiers be here as some
 * derivated sub-enum?
 * 
 * @author szabivan
 * @see Connective
 */
public abstract class Unary extends CompoundFormula {

	protected Unary(Formula[] subformulaArray) {
		super(subformulaArray);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getConnectiveString());
		if (!subformulas[0].isAtomic())
			builder.append('(');
		builder.append(subformulas[0].toString());
		if (!subformulas[0].isAtomic())
			builder.append(')');
		return builder.toString();
	}
}
