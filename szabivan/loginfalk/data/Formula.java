package szabivan.loginfalk.data;
/**
 * Formula interface.
 * A Formula is either atomic or is a {@link CompoundFormula}.
 * @author szabivan
 */

public interface Formula {
	/**
	 * @return true iff the formula is an atomic formula.
	 */
	public boolean isAtomic();
	/**
	 * The normalization process eliminates double negations
	 * and groups together associative connectives, i.e. performs the rewritings
	 * ¬¬F -> F
	 * for negation and
	 * O(G1,...,Gk,O(F1,..,Fn),Gk+2,...,Gm) -> O(G1,...,Gk,F1,...,Fn,Gk+2,...,Gm)
	 * for each associative connective O, as many times as possible.
	 * @return The normalized equivalent formula. 
	 */
	public Formula normalize();
}
