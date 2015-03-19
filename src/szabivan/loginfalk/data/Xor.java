package szabivan.loginfalk.data;

public class Xor extends Associative {
	public static final String CONNECTIVE_STRING = " X ";

	public Xor(Formula[] subformulaArray) {
		super(subformulaArray);
	}

	@Override
	protected boolean isSameConnective(Associative other) {
		return (other instanceof Xor);
	}

	@Override
	public Formula normalize() {
		super.normalize();
		return new Xor(getNormalizedSubFormulas());
	}

	@Override
	protected String getConnectiveString() {
		return CONNECTIVE_STRING;
	}
}
