import javax.swing.*;

public class test extends JFrame {

	public static void main(String[] args) {
		// TODO Auto-generated method stub


//		Token token = null;
//		Lexer lexer = new Lexer();
//		if (!lexer.InitScanner("test.txt"))            // 初始化词法分析器
//		{
//			System.out.println("Open Source File Error !");
//		}
//		System.out.print("  记号类别     字符串    常数值\n");
//		System.out.print("_______________________________\n");
//		while(1==1)
//		{
//			token = lexer.GetToken();		// 通过词法分析器获得一个记号
//			if(token.type != Token.Token_Type.NONTOKEN)// 打印记号的内容
//			{
//				System.out.printf("%10s  %8s  %8.2f\n",
//						token.type.toString(),token.lexeme,token.value);
//			}
//			else
//				break;			// 源程序结束，退出循环
//		};
//		System.out.print("_______________________________\n");
//		lexer.CloseScanner();// 关闭词法分析器


		 Parser parser = new Parser();
		 parser.init(test.class.getClassLoader().getResource("").getPath()+"\\test.txt");

	}

}
