package szabivan.loginfalk.data;

/**
 * A BinaryConnective is a Connective which can have exactly two subformulas.
 * Associative connectives are handled by the {@link AssociativeConnective} enum.
 * 
 */

public enum BinaryConnective implements Connective {
	IMPLIES(" → "), IFF("<->");
	
	/**
	 * Instances of this enum are specified by giving a "name" which is given back by {@link #toString()}.
	 */
	private final String name;
	private BinaryConnective(String name){ this.name = name; }
	@Override
	public String toString(){ return name; }
}
