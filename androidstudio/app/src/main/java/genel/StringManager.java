package genel;

public class StringManager {
	public static String Left(String str, int n) {
		if (n <= 0)
			return "";
		else if (n > str.length())
			return str;
		else
			return str.substring(0, n);
	}

	public static String Right(String str, int n) {
		if (n <= 0)
			return "";
		else if (n > str.length())
			return str;
		else {
			int iLen = str.length();
			return str.substring(iLen - n, n + 1);
		}
	}

}