package szabivan.loginfalk.data;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class PrintStreamWrapper extends PrintStream {

	final PrintStream stream;

	final static Map<String, String> translations = new HashMap<String, String>();
	static {
		translations.put(BinaryConnective.IMPLIES.toString(), " -> ");
		translations.put(UnaryConnective.NOT.toString(), "~");
		translations.put(AssociativeConnective.AND.toString(), " & ");
		translations.put(AssociativeConnective.OR.toString(), " V ");
		translations.put(ClausePrinter.EMPTY_CLAUSE_STRING, "[]");
	}

	public PrintStreamWrapper(PrintStream stream) {
		super(stream);
		this.stream = stream;
	}

	private String translate(String s) {
		for (Map.Entry<String, String> entry : translations.entrySet()) {
			s = s.replaceAll(entry.getKey(), entry.getValue());
		}
		return s;
	}

	public void print(String s) {
		stream.print(translate(s));
	}

	public void println(String s) {
		stream.println(translate(s));
	}

	public void print(Object obj) {
		this.print(obj.toString());
	}

	public void println(Object obj) {
		this.println(obj.toString());
	}
}
