package szabivan.loginfalk.data;

/**
 * An AssociativeConnective is a Connective whose underlying boolean function is
 * associative, hence it can have an arbitrary (positive) number of subformulas.
 * 
 * @author szabivan
 * @version 1.0
 */

public enum AssociativeConnective implements Connective {

	AND(" ∧ "), OR(" ∨ "), XOR(" X ");

	/**
	 * Instances of this enum are specified by giving a "name" which is given
	 * back by {@link #toString()}.
	 */
	private final String name;

	private AssociativeConnective(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
