package szabivan.loginfalk.data;
/**
 * Class with several static methods for computing an equivalent CNF for an input
 * - formula,
 * - set of formulas
 * - or a consequence of the form Sigma |= F.
 *
 * TODO consider using {@link BitSet} for representing clauses instead of ints.
 * 
 * @author szabivan
 * @version 1.0
 */

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CnfComputer {
	public static Set< Integer > wedgeCnfs( Set< Integer >[] cnfs ){
		return TruthTableComputer.veeValuations( cnfs );
	}
	public static Set< Integer > veeCnfs( Set< Integer >[] cnfs ){
		return TruthTableComputer.wedgeValuations( cnfs );
	}
	
	/**
	 * Returns a set of ints that is a CNF equivalent to either the given formula
	 * or its complement based on retainValue.
	 * 
	 * Each int describes a clause, details and limitations are given in {@link ClausePrinter}.
	 * 
	 * @param formula		The input formula F.
	 * @param retainValue	Should the CNF be equivalent to F (<code>true</code>) or ¬F (<code>false</code>).
	 * @return	A CNF equivalent to F if retainValue is <code>true</code> and to ¬F otherwise.
	 * 
	 * Trivial clauses are NOT given back.
	 * 
	 */
	@SuppressWarnings("unchecked") //f'ck that "cannot create a generic array" stuff, NO WARNING in my code :P
	public static Set< Integer > createCnf( Formula formula, boolean retainValue ){
		if( formula instanceof TruthVariable ){
			// a TruthVariable is 
			return retainValue ?
					Collections.singleton( 1 << (2*((TruthVariable)formula).varIndex ))
					:
					Collections.singleton( 1 << (1+2*((TruthVariable)formula).varIndex)) ;
		}
		CompoundFormula compoundFormula = (CompoundFormula)formula;
		Connective conn = compoundFormula.getConnective();
		if( conn == UnaryConnective.NOT ){
			return createCnf( compoundFormula.getSubFormula(0), !retainValue );
		}
		if( conn == BinaryConnective.IMPLIES ){
			if( retainValue ){
				return veeCnfs( new Set[]{
					createCnf( compoundFormula.getSubFormula(0),false ),
					createCnf( compoundFormula.getSubFormula(1), true )
				} );
			}else{
				return wedgeCnfs( new Set[]{
						createCnf( compoundFormula.getSubFormula(0), true ),
						createCnf( compoundFormula.getSubFormula(1), false )
					} );
			}
		}
		if( conn instanceof AssociativeConnective ){
			Set<Integer>[] subcnfs = new Set[ compoundFormula.getSubFormulaCount() ];
			for( int i = 0; i < compoundFormula.getSubFormulaCount(); i++ ){
				subcnfs[ i ] = createCnf( compoundFormula.getSubFormula(i), retainValue ); 
			}
			if( conn == AssociativeConnective.OR ){
				return retainValue ? veeCnfs( subcnfs ) : wedgeCnfs( subcnfs );
			}
			if( conn == AssociativeConnective.AND ){
				return retainValue ? wedgeCnfs( subcnfs ) : veeCnfs( subcnfs );				
			}
		}
		throw new IllegalArgumentException("CnfComputer: connective " + conn + " is not supported");
	}
	
	public static Set< Integer > createCnf( Set< Formula > sigma ){
		Set< Integer > ret = new HashSet<Integer>();
		for( Formula form : sigma ){ ret.addAll( createCnf( form, true) );}
		return ret;
	}
	
	public static Set< Integer > createCnf( SigmaFPair pair ){
		Set< Integer > ret = new HashSet<Integer>();
		for( Formula form : pair.sigma ){ ret.addAll( createCnf( form, true) );}
		ret.addAll( createCnf( pair.f, false));
		return ret;
	}
}
