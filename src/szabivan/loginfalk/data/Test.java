package szabivan.loginfalk.data;

public class Test {
	public static void main(String[] args) {
		TruthVariable.registerDefaults();
		int varCount = 6;

		Formula test = new CompoundFormula(
				BinaryConnective.IMPLIES,
				new Formula[] {
						new CompoundFormula(
								AssociativeConnective.AND,
								new Formula[] {
										TruthVariable.getVariable("q"),
										new CompoundFormula(
												BinaryConnective.IMPLIES,
												new Formula[] {
														TruthVariable
																.getVariable("s"),
														TruthVariable
																.getVariable("r") }) }),
						new CompoundFormula(
								UnaryConnective.NOT,
								new Formula[] { new CompoundFormula(
										AssociativeConnective.AND,
										new Formula[] {
												TruthVariable.getVariable("p"),
												TruthVariable.getVariable("q") }) }) });

		System.out.println(test);
		for (int clause : CnfComputer.createCnf(test, true)) {
			System.out.println(ClausePrinter.toString(clause, varCount));
		}

	}
}
