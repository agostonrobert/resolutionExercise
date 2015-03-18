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
public enum UnaryConnective implements Connective {
	NOT("¬");
	/**
	 * Instances of this enum are specified by giving a "name" which is given
	 * back by {@link #toString()}.
	 */
	private final String name;

	private UnaryConnective(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}