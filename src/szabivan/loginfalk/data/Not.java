package szabivan.loginfalk.data;

public class Not extends Unary {
	public static final String CONNECTIVE_STRING = "¬";

	public Not(Formula[] subformulaArray) {
		super(subformulaArray);
	}

	@Override
	public Formula normalize() {
		super.normalize();

		Formula firstSubFormula = subformulas[0];
		if (firstSubFormula instanceof Not) {
			Not subFormula = (Not) firstSubFormula;
			return subFormula.getSubFormula(0);
		}

		return this;
	}

	@Override
	protected String getConnectiveString() {
		return CONNECTIVE_STRING;
	}
}
