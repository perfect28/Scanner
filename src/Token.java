import java.io.File;
public class Token {
	public enum Token_Type // 记号的类别，共22个
	{
		ORIGIN("ORIGIN",0), SCALE("SCALE",1), ROT("ROT",2), IS("IS",3), // 保留字（一字一码）
		TO("TO",4), STEP("STEP",5), DRAW("DRAW",6), FOR("FOR",7), FROM("FROM",8), // 保留字
		T("T",9), // 参数
		SEMICO("SEMICO",10), L_BRACKET("L_BRACKET",11), R_BRACKET("R_BRACKET",12), COMMA("COMMA",13), // 分隔符
		PLUS("PLUS",14), MINUS("MINUS",15), MUL("MUL",16), DIV("DIV",17), POWER("POWER",18), // 运算符
		FUNC("FUNC",19), // 函数（调用）
		CONST_ID("CONST_ID",20), // 常数
		NONTOKEN("NONTOKEN",21), // 空记号（源程序结束）
		ERRTOKEN("ERRTOKEN",22);// 出错记号（非法输入）

		// 成员变量
		private String name;
		private int index;

		// 构造方法
		private Token_Type(String name, int index) {
			this.name = name;
			this.index = index;
		}

		// 覆盖方法
		@Override
		public String toString() {
			return this.name;
		}
	};

	public Token_Type type;
	public String lexeme;
	public double value;

	Token(){}

	Token(Token_Type type,String lexeme,double value)
	{
		this.type=type;
		this.lexeme=lexeme;
		this.value=value;
	}
}
