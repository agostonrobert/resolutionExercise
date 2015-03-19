package szabivan.loginfalk.data;

public class And extends Associative {
	public static final String CONNECTIVE_STRING = " ∧ ";

	public And(Formula[] subformulaArray) {
		super(subformulaArray);
	}

	@Override
	protected boolean isSameConnective(Associative other) {
		return (other instanceof And);
	}

	@Override
	public Formula normalize() {
		super.normalize();
		return new And(getNormalizedSubFormulas());
	}

	@Override
	protected String getConnectiveString() {
		return CONNECTIVE_STRING;
	}
}
