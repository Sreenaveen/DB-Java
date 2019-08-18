import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sreenaveen
 *
 */
public class EmailIdValidator {
	public final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	/**
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> fileRead(String filePath) throws IOException {
		FileReader in = null;
		BufferedReader read = null;
		ArrayList<String> listOfWords = new ArrayList<String>();
		try {
			in = new FileReader(filePath);
			read = new BufferedReader(in);

			String line;
			while ((line = read.readLine()) != null) {
				String[] wordsInLine = line.split(" ");
				for (String string : wordsInLine) {
					listOfWords.add(string);
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}

		}
		return listOfWords;
	}

	/**
	 * @param emailStr
	 * @return
	 */
	public boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	/**
	 * @param strings
	 * @return
	 */
	public Map<String, Boolean> wordCount(ArrayList<String> strings) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		Iterator<String> itr = strings.iterator();
		while (itr.hasNext()) {
			String word = itr.next();
			Boolean result = validate(word);
			map.put(word, result);

		}

		return map;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		EmailIdValidator obj = new EmailIdValidator();
		ArrayList<String> listOfWords = obj.fileRead("emails.txt");
		System.out.println(obj.wordCount(listOfWords));

	}
}
