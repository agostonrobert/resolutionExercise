package szabivan.loginfalk.data;

/**
 * Class with static methods calculating a DNF for a formula and with operations on DNFs.
 * 
 * @author szabivan
 * @version 1.0
 */

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TruthTableComputer {	
	public static boolean isClash( int valuation ){
		// Rate on 1..10: what's your opinion wrt those magic constants? :P
		return ( (valuation & (valuation >> 1) & 0x1555555) != 0 );
	}
	public static Set< Integer > veeValuations( Set< Integer >[] valuations ){
		Set< Integer > ret = new HashSet<Integer>();
		for( Set< Integer > orig : valuations ){
			ret.addAll( orig );
		}
		return ret;
	}
	public static Set< Integer > wedgeValuations( Set< Integer > valuation1, Set< Integer > valuation2 ){
		Set< Integer > ret = new HashSet< Integer >();
		for( int v1 : valuation1 ){
			for( int v2: valuation2 ){
				if( !isClash( v1 | v2 )) ret.add( v1 | v2 );
			}
		}
		return ret;
	}
	public static Set< Integer > wedgeValuations( Set< Integer >[] valuations ){
		if( valuations.length <= 1 ) return valuations[ 0 ];
		Set< Integer > ret = wedgeValuations( valuations[0], valuations[1]) ;
		for( int i = 2; i < valuations.length; i++ ){
			ret = wedgeValuations( ret, valuations[ i ] );
		}
		return ret;
	}
	@SuppressWarnings("unchecked")
	public static Set< Integer > getValuations( Formula formula, boolean value ){
		if( formula instanceof TruthVariable ){
			int varIndex = ((TruthVariable)formula).varIndex;
			return Collections.singleton( value ? 1 << (varIndex<<1) : 1 << ((varIndex<<1)|1) );
		}
		CompoundFormula compoundFormula = (CompoundFormula)formula;
		Connective conn = compoundFormula.getConnective();
		if( conn == UnaryConnective.NOT ){
			return getValuations( compoundFormula.getSubFormula(0), !value );
		}
		if( (conn == AssociativeConnective.OR && value )||(conn == AssociativeConnective.AND && !value )){
			Set< Integer >[] valuations = new Set[ compoundFormula.getSubFormulaCount() ];
			for( int i = 0; i < valuations.length; i++ ){
				valuations[ i ] = getValuations( compoundFormula.getSubFormula( i ), value );
			}
			return veeValuations( valuations );
		}
		if( (conn == AssociativeConnective.OR && !value )||(conn == AssociativeConnective.AND && value )){
			Set< Integer >[] valuations = new Set[ compoundFormula.getSubFormulaCount() ];
			for( int i = 0; i < valuations.length; i++ ){
				valuations[ i ] = getValuations( compoundFormula.getSubFormula( i ), value );
			}
			return wedgeValuations( valuations );
		}
		if( conn == BinaryConnective.IMPLIES && value ){
			Set<Integer> leftSet = getValuations(compoundFormula.getSubFormula(0), false);
			Set<Integer> rightSet = getValuations(compoundFormula.getSubFormula(1), true);
			return veeValuations( new Set[]{ leftSet, rightSet });
		}
		if( conn == BinaryConnective.IMPLIES && !value ){
			Set<Integer> leftSet = getValuations(compoundFormula.getSubFormula(0), true);
			Set<Integer> rightSet = getValuations(compoundFormula.getSubFormula(1), false);
			return wedgeValuations( new Set[]{ leftSet, rightSet });
		}
		throw new UnsupportedOperationException("Connective "+conn+" is not supported");
	}
}
