package szabivan.loginfalk.data;

/**
 * A generator class that generates random formula instances.
 * Each connective has an associated weight function, and also the atomic formulas have one.
 * Moreover, a maximum depth can also be set.
 * 
 * 
 * 
 * @author szabivan
 * @version 1.0
 */
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

public class FormulaGenerator {
	// ye olde random
	private static Random random = new Random();
	
	// each connective has a positive weight stored in this map
	private Map< Connective, Integer > weightMap = new HashMap<Connective, Integer>();
	// the weight of generating an atomic formula is also a positive integer
	private int atomicWeight;
	// totalWeight is simply the sum of all the weights above
	private int totalWeight;
	// the formula can have variables with ID falling into the interval [0..varCount)
	private int varCount;
	// the formula can have depth at most maxDepth
	private int maxDepth;
	
	/**
	 * After construction, only atomic formulas can be generated.
	 * Fiddle with {@link #setWeight(Connective, int)} and {@link #setVarCount(int)}
	 * before attempting to generate an "interesting" instance.
	 */
	public FormulaGenerator(){
		totalWeight = atomicWeight = 1;
		varCount = 1;
		maxDepth = 3;
	}
	
	/**
	 * Sets the weight of conn to the given value.
	 * @param conn		the specified Connective
	 * @param weight	the new weight value
	 * @return true if the weight of conn can be set to this value.
	 * If weight == 0, then the generated formula cannot contain conn anymore and the method returns <code>true</code>.
	 * If weight >  0, then the generated formula can contain conn, and the method returns <code>true</code>.
	 * Passing a negative weight is invalid, in that case nothing happens and the method returns <code>false</code>.
	 */
	public boolean setWeight( Connective conn, int weight ){
		if( weight < 0  ) return false;
		if( weightMap.containsKey( conn )){
			totalWeight -= weightMap.get(conn);
		}
		if( weight == 0 ){
			weightMap.remove( conn );			
		}else{
			weightMap.put(conn, weight);
			totalWeight += weight;
		}
		return true;		
	}
	
	/**
	 * Sets the number of variables the generated formula can have.
	 * @param count	The new number of variables.
	 * @return	<code>true</code> if count >= 1; <code>false</code> otherwise.
	 */
	public boolean setVarCount( int count ){
		if( count <= 0 ) return false;
		this.varCount = count;
		return true;
	}
	
	/**
	 * Sets the weight of generating atomic formulas.
	 * This weight has to be positive in all cases.
	 * @param weight	the new weight of generating atomic formulas.
	 * @return	<code>true</code> if weight > 0, <code>false</code> otherwise.
	 */
	public boolean setAtomicWeight( int weight ){
		if( weight <= 0 ) return false;
		this.totalWeight -= this.atomicWeight;
		this.totalWeight += (this.atomicWeight = weight);
		return true;
	}
	
	/**
	 * Generates a formula with depth at most depth.
	 * So if depth == 0, it will be an atomic formula.
	 * Otherwise, either an atomic formula or a {@link CompoundFormula} with a top-level connective whose
	 * weight has been set to positive. The probability of these outcomes is proportional to the
	 * given weights, so if the weight of NOT is 2, the weight of AND is 1 and the atomic weight is 1,
	 * then in 50% of the time a NOT formula will be generated and in 25-25% of the time an AND and
	 * an atomic formula, respectively.
	 * If a compound formula gets generated, it will be either binary or unary, depending on the
	 * connective (associative connectives also get two immediate subformulas) and the method is
	 * called for the subformulas with depth-1.
	 *    
	 * @param depth	The maximal depth of the generated formula.
	 * @return A random {@link Formula} instance.
	 */
	public Formula generateFormula( int depth ){
		int rand = random.nextInt( totalWeight );
		rand -= this.atomicWeight;
		if( rand < 0 || depth == 0 ){
			return TruthVariable.getVariable( random.nextInt( this.varCount ));
		}
		Iterator< Map.Entry<Connective, Integer> > iterator = this.weightMap.entrySet().iterator();
		Map.Entry<Connective, Integer> entry = null;
		do{
			entry = iterator.next();
			rand -= entry.getValue();
		}while( rand >= 0 );
		Connective conn = entry.getKey();
		if( conn instanceof UnaryConnective ){
			return new CompoundFormula( conn, new Formula[]{ generateFormula( depth - 1 ) });
		}
		if(( conn instanceof BinaryConnective )||(conn instanceof AssociativeConnective)){
			return new CompoundFormula( conn, new Formula[]{ generateFormula( depth - 1 ), generateFormula( depth - 1 ) } );
		}
		throw new IllegalStateException("Houston, we have a serious problem with the random generator!");
	}
	
	/**
	 * Generates an unsatisfiable set of formulas.
	 * @return	A Set of formulas which is guaranteed to be a minimal unsatisfiable one.
	 * @see #generateFormula(int).
	 */
	public Set< Formula > generateUnsatSigma(){
		Vector< Formula > formulas = new Vector<Formula>();
		Vector< Set< Integer > > valuations = new Vector< Set< Integer > >();
		Set< Integer > wedgeAll = Collections.singleton( (Integer) 0 );
		Set< Formula > ret = new HashSet<Formula>();
		do{
			Formula formula = generateFormula( this.maxDepth ).normalize();
			formulas.add( formula );
			Set<Integer> current = TruthTableComputer.getValuations( formula, true );
			valuations.add( current );
			wedgeAll = TruthTableComputer.wedgeValuations(
					wedgeAll,
					 current );			
		}while( !wedgeAll.isEmpty() );
		Set< Integer > prefixValuation = Collections.singleton( (Integer) 0);
		for( int i = 0; i < formulas.size(); i++ ){
			wedgeAll = prefixValuation;
			for( int j = i + 1; j < formulas.size(); j++ ){
				wedgeAll = TruthTableComputer.wedgeValuations( wedgeAll, valuations.elementAt( j ));
			}
			if( wedgeAll.isEmpty() ){
				
			}else{
				ret.add( formulas.elementAt( i ));
				prefixValuation = TruthTableComputer.wedgeValuations( prefixValuation, valuations.elementAt( i ));
			}
		}
		return ret;
	}
	
	/**
	 * Generates a \Sigma and an F with \Sigma |= F.
	 * \Sigma may be empty.
	 * \Sigma is minimal in the sense that no proper subset of \Sigma entails F. 
	 * @return A {@link SigmaFPair} whose {@link SigmaFPair#sigma} entails {@link SigmaFPair#f}.
	 */
	public SigmaFPair generateConsequence(){
		Set< Formula > sigma = generateUnsatSigma();
		Formula negated = null;
		for( Formula f : sigma ){
			if( negated == null) { negated = f; continue; }
			if( f instanceof CompoundFormula ){
				if( ((CompoundFormula)f).getConnective() == UnaryConnective.NOT ){ negated = f; continue; }
			}
		}
		sigma.remove( negated );
		return new SigmaFPair(
				sigma,
				new CompoundFormula( UnaryConnective.NOT, new Formula[]{ negated } ).normalize() );
	}
	
	/**
	 * Setter method for the maximum depth of the generated formula.
	 * @param depth The new depth. With value 0, always atomic formulas are generated.
	 * With a negative value, no restriction happens on the depth.
	 * Use this latter with caution, setting too large weights to the branching connectives
	 * can result in a nonterminating generating sequence.
	 */
	public void setMaxDepth( int depth ) {
		this.maxDepth = depth;
	}
}
