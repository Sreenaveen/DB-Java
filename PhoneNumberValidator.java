import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator {
	public final Pattern VALID_PHONE_NUMBER_INDIA_REGEX = Pattern.compile("(0/91)?[7-9][0-9]{9}");
	public final Pattern VALID_PHONE_NUMBER_AUSTRALIA_REGEX = Pattern.compile(
			"^(?:\\+?(61))? ?(?:\\((?=.*\\)))?(0?[2-57-8])\\)? ?(\\d\\d(?:[- ](?=\\d{3})|(?!\\d\\d[- ]?\\d[- ]))\\d\\d[- ]?\\d[- ]?\\d{3})$");

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


	public boolean validate(String emailStr, String country) {
		Matcher matcher = null;
		if (country.equalsIgnoreCase("India"))
			matcher = VALID_PHONE_NUMBER_INDIA_REGEX.matcher(emailStr);
		else
			matcher = VALID_PHONE_NUMBER_AUSTRALIA_REGEX.matcher(emailStr);

		return matcher.find();
	}
	public Map<String, Boolean> wordCount(ArrayList<String> strings) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		Iterator<String> itr = strings.iterator();
		while (itr.hasNext()) {
			String word = itr.next();
			Boolean result = validate(word, "India");
			map.put(word, result);

		}

		return map;
	}

	public static void main(String args[]) throws IOException {
		PhoneNumberValidator obj = new PhoneNumberValidator();
		ArrayList<String> listOfWords = obj.fileRead("Phonenumbers.txt");
		System.out.println(obj.wordCount(listOfWords));

	}
}
