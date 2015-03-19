package szabivan.loginfalk.data;

public class Implies extends Binary {
	public static final String CONNECTIVE_STRING = " → ";

	public Implies(Formula[] subformulaArray) {
		super(subformulaArray);
	}

	@Override
	public Formula normalize() {
		return super.normalize();
	}

	@Override
	protected String getConnectiveString() {
		return CONNECTIVE_STRING;
	}
}
