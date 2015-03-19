package szabivan.loginfalk.data;

public class Or extends Associative {
	public static final String CONNECTIVE_STRING = " ∨ ";

	public Or(Formula[] subformulaArray) {
		super(subformulaArray);
	}

	@Override
	protected boolean isSameConnective(Associative other) {
		return (other instanceof Or);
	}

	@Override
	public Formula normalize() {
		super.normalize();
		Formula[] normalizedSubFormulas = getNormalizedSubFormulas();
		return new Or(normalizedSubFormulas);
	}

	@Override
	protected String getConnectiveString() {
		return CONNECTIVE_STRING;
	}
}
