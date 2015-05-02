import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

public class Lexer {
	public static int TOKEN_LEN = 100;
	public static File file = null;
	public static int LineNo = 0;
	static String TokenBuffer = null;
	static Token TokenTab[] = new Token[18];
	static String str = null;
	static int now_point = 0;
	static PushbackReader pr = null;
	static BufferedReader reader = null;
	static StringReader sr = null;
	static boolean flag = false;

	Lexer() {
		TokenTab[0] = new Token(Token.Token_Type.CONST_ID, "PI", 3.1415926);
		TokenTab[1] = new Token(Token.Token_Type.CONST_ID, "E", 2.71828);
		TokenTab[2] = new Token(Token.Token_Type.T, "T", 0.0);
		TokenTab[3] = new Token(Token.Token_Type.FUNC, "SIN", 0.0);
		TokenTab[4] = new Token(Token.Token_Type.FUNC, "COS", 0.0);
		TokenTab[5] = new Token(Token.Token_Type.FUNC, "TAN", 0.0);
		TokenTab[6] = new Token(Token.Token_Type.FUNC, "LN", 0.0);
		TokenTab[7] = new Token(Token.Token_Type.FUNC, "EXP", 0.0);
		TokenTab[8] = new Token(Token.Token_Type.FUNC, "SQRT", 0.0);
		TokenTab[9] = new Token(Token.Token_Type.ORIGIN, "ORIGIN", 0.0);
		TokenTab[10] = new Token(Token.Token_Type.SCALE, "SCALE", 0.0);
		TokenTab[11] = new Token(Token.Token_Type.ROT, "ROT", 0.0);
		TokenTab[12] = new Token(Token.Token_Type.IS, "IS", 0.0);
		TokenTab[13] = new Token(Token.Token_Type.FOR, "FOR", 0.0);
		TokenTab[14] = new Token(Token.Token_Type.FROM, "FROM", 0.0);
		TokenTab[15] = new Token(Token.Token_Type.TO, "TO", 0.0);
		TokenTab[16] = new Token(Token.Token_Type.STEP, "STEP", 0.0);
		TokenTab[17] = new Token(Token.Token_Type.DRAW, "DRAW", 0.0);
	}

	static public Boolean InitScanner(String FileName) {
		file = new File(FileName);
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LineNo = 1;
		return true;
	}

	static public void CloseScanner() {
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static char GetChar() {
		char ch = 0;
		try {
			if (now_point == 0)
			{
				str = reader.readLine();
				if (str == null)
				{
					flag=true;
					return ch;
				}
				// create a new StringReader
				sr = new StringReader(str);
				// create a new PushBack reader based on our string reader
				pr = new PushbackReader(sr);
			}

			ch = (char) pr.read();
			now_point++;
			//System.out.println(""+now_point+":"+ch);
			if (now_point == str.length())
				now_point = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Character.toUpperCase(ch);
	}

	static void BackChar(char ch) {
		if (ch != 0)
			try {
				//System.out.println(ch);
				pr.unread(ch);
				now_point--;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	static void AddCharTokenString(char ch) {
		TokenBuffer = TokenBuffer + ch;
	}

	static Token JudgeKeyToken(String IDString) {
		for (int i = 0; i < 18; i++) {
			if (TokenTab[i].lexeme.equals(IDString))
				return TokenTab[i];
		}

		Token errorToken = new Token(Token.Token_Type.ERRTOKEN, "", 0.0);
		return errorToken;
	}

	static public Token GetToken() {
		String read_line;
		Token token = new Token(Token.Token_Type.ERRTOKEN, " ", 0.0); // ���ڷ��ؼǺ�
		TokenBuffer = "";
		char now_char = GetChar(); // ��Դ�ļ��ж�ȡһ���ַ�
		while (now_char == ' ' || now_char == '\t')
			// �ո�TAB���س����ַ��Ĺ���
			now_char = GetChar();
		while (now_char == 65535) {
			now_point=0;
			LineNo++;
			now_char = GetChar();
		}
		if (flag)
		{
			token.type = Token.Token_Type.NONTOKEN;
			return token;
		}
		AddCharTokenString(now_char); // ��������ַ��Ž�������TokenBuffer��
		if (Character.isLetter(now_char)) {
			while (1 == 1) {
				now_char = GetChar();
				if (Character.isLetter(now_char)||Character.isDigit(now_char))
					AddCharTokenString(now_char);
				else
					break;
			}
			BackChar(now_char);
			token = JudgeKeyToken(TokenBuffer);
			return token;
		} // ʶ��ID
		else if (Character.isDigit(now_char)) {
			token.type = Token.Token_Type.CONST_ID;
			while (1 == 1) {
				now_char = GetChar();
				if (Character.isDigit(now_char)) {
					AddCharTokenString(now_char);
				} else {
					break;
				}
			}
			if (now_char == '.') {
				AddCharTokenString(now_char);
				while (1 == 1) {
					now_char = GetChar();
					if (Character.isDigit(now_char)) {
						AddCharTokenString(now_char);
					} else {
						break;
					}
				}
			}
			BackChar(now_char);
			token.lexeme = TokenBuffer;
			token.value=Double.parseDouble(token.lexeme);
			return token;
		} // ʶ�����ֳ���
		else {
			switch (now_char) {
			case ';':
				token.lexeme = TokenBuffer;
				token.type = Token.Token_Type.SEMICO;
				return token;
			case '(':
				token.lexeme = TokenBuffer;
				token.type = Token.Token_Type.L_BRACKET;
				return token;
			case ')':
				token.lexeme = TokenBuffer;
				token.type = Token.Token_Type.R_BRACKET;
				return token;
			case ',':
				token.lexeme = TokenBuffer;
				token.type = Token.Token_Type.COMMA;
				return token;
			case '+':
				token.lexeme = TokenBuffer;
				token.type = Token.Token_Type.PLUS;
				return token;
				// ���⴦�������:-,/,*
			case '-':
				now_char = GetChar();
				if (now_char == '-') {
					now_point = 0;
					return GetToken();
				} else {
					BackChar(now_char);
					token.lexeme = TokenBuffer;
					token.type = Token.Token_Type.MINUS;
				}
				return token;
			case '*':
				now_char = GetChar();
				if (now_char == '*') {
					AddCharTokenString(now_char);
					token.lexeme = TokenBuffer;
					token.type = Token.Token_Type.POWER;
				} else {
					BackChar(now_char);
					token.lexeme = TokenBuffer;
					token.type = Token.Token_Type.MUL;
				}
				return token;
			case '/':
				now_char = GetChar();
				if (now_char == '/') {
					now_point = 0;
					return GetToken();
				} else {
					BackChar(now_char);
					token.lexeme = TokenBuffer;
					token.type = Token.Token_Type.DIV;
				}
				return token;
			}
		}
		return token;
	}
}
