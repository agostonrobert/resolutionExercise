package szabivan.loginfalk.data;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import szabivan.loginfalk.data.Resolver.ClauseReason;

public class Main {
	public static void parseArg( String arg, Map<String, Integer> argMap){
		String[] val = arg.split("=");
		if( val.length != 2 ) return;
		try{
			int intVal = Integer.parseInt( val[1] );
			argMap.put( val[0], intVal );
		}catch( NumberFormatException nfe ){
			// those idiots cannot even pass an integer as parameter. who cares.
		}		
	}
	public static int getThreshold( int def, int lower, int upper, Integer value ){
		if( value == null ) return def;
		if( value < lower ) return lower;
		if( value > upper ) return upper;
		return value;
	}
	
	public static void output( ResolutionExercise exercise, int maxVars, int minLength, int trials, PrintStream stream ){
		stream.println("Parameters:\n  # of variables <= " + maxVars + "\n  Length of refutation >= " + minLength + "\n");		
		if( exercise == null ){
			stream.println("Could not generate any exercise within "+trials+" trials, giving up. Sorry.");
			return;
		}
		
		SigmaFPair pair = exercise.input;
		stream.println("TASK.\n  Show by resolution that");
		boolean hadElement = false;
		if( !pair.sigma.isEmpty() ){
			stream.print("  {");
			for( Formula f : pair.sigma ){
				if( hadElement ) stream.print(",");
				stream.print(" ");
				stream.print( f );
				hadElement = true;
			}
			stream.print("} ");			
		}
		stream.print("|= ");
		stream.println( pair.f );
		stream.println();
		stream.println("SOLUTION.");
		stream.println("  I. Transform each member of Sigma and "+UnaryConnective.NOT.toString()+"F to CNF.");
		stream.println("     The resulting set of nontrivial clauses is");
		Set< Integer > sigmaPrime = exercise.sigmaPrime;
		stream.print("    Sigma' = {");
		hadElement = false;
		for( int clause : sigmaPrime ){
			if( hadElement ) stream.print(",");
			stream.print(" ");
			stream.print( ClausePrinter.toString( clause, maxVars ));
			hadElement = true;
		}
		stream.println("}");
		stream.println();
		Vector< ClauseReason > reasons = exercise.solution;
		stream.println("  II. Execute the resolution algorithm on Sigma'.");
		for( int i = 0; i < reasons.size(); i++ ){
			stream.print("    ");
			stream.print(i+1);
			stream.print(". ");
			ClauseReason reason = reasons.get( i );
			stream.print( ClausePrinter.toString( reason.clause, maxVars ));
			if( reason.fromClause1 == -1 ){
				stream.println(" is in Sigma'");
			}else{
				stream.print(" Res(");
				stream.print( reason.fromClause1 );
				stream.print(",");
				stream.print( reason.fromClause2 );
				stream.println(")");
			}
		}
		stream.println("\n  Since "+ClausePrinter.toString(0,1)+" is in Res*(Sigma'),\n  Sigma' is unsatisfiable,\n  hence Sigma |= F indeed holds.");
		
	}
	
	public static void main(String[] args) {
		Map< String, Integer > argMap = new HashMap<String, Integer>(); 
		for( String arg : args ){
			parseArg( arg, argMap );
		}
		int maxVars = getThreshold( 4, 3, 6, argMap.get("maxVars"));
		int minLength = getThreshold( 7, 5, 13, argMap.get("minLength"));
		int trials = getThreshold( 1000, 100, 10000, argMap.get("trials"));
		int minUnits = getThreshold( 1, 0, 2*maxVars, argMap.get("minUnits") );
		int maxUnits = getThreshold( minUnits, 0, 2*maxVars, argMap.get("maxUnits"));
		int maxDepth = 3;
		
		TruthVariable.registerDefaults();
		
		ResolutionExercise exercise = ExerciseGenerator.generateExercise( maxVars, maxDepth, minLength, trials, minUnits, maxUnits);
		
		output( exercise, maxVars, minLength, trials, argMap.containsKey("ascii") ? new PrintStreamWrapper( System.out ) :
			argMap.containsKey("latex") ? new PrintStreamLatexWrapper( System.out ) : System.out );
		
	}
}
