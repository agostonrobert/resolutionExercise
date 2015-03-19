package szabivan.loginfalk.data;

/**
 * A BinaryConnective is a Connective which can have exactly two subformulas.
 * Associative connectives are handled by the {@link AssociativeConnective}
 * enum.
 * 
 */
public abstract class Binary extends CompoundFormula {

	protected Binary(Formula[] subformulaArray) {
		super(subformulaArray);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (!subformulas[0].isAtomic())
			builder.append('(');
		builder.append(subformulas[0].toString());
		if (!subformulas[0].isAtomic())
			builder.append(')');
		builder.append(getConnectiveString());
		if (!subformulas[1].isAtomic())
			builder.append('(');
		builder.append(subformulas[1].toString());
		if (!subformulas[1].isAtomic())
			builder.append(')');
		return builder.toString();
	}
}
