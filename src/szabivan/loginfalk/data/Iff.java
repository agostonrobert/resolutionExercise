package szabivan.loginfalk.data;

public class Iff extends Binary {
	public static final String CONNECTIVE_STRING = "<->";

	public Iff(Formula[] subformulaArray) {
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
