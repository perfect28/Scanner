import javax.swing.*;

/**
 * Created by kkpanda on 2014/12/6.
 */


public class Parser {
    static double Parameter;
    static Token token;
    static Lexer lexer;
    public static Draw panel;

    public static final int DEFAULT_WIDTH=800;
    public static final int DEFAULT_HEIGHT=600;

    static int ans_num;
    static Semantic.Point[] ans;

    Parser()
    {
        token = new Token();
        lexer = new Lexer();
    }

    ExprNode MakeExprNode(Token.Token_Type opcode,Object... args)
    {
        ExprNode ExprPtr = new ExprNode();
        ExprPtr.OpCode = opcode;
        switch(opcode)
        {
            case CONST_ID:	// 常数节点
                ExprPtr.CaseConst = (Double)args[0];
                break;
            case T:		// 参数节点
                ExprPtr.CaseParmPtr = Parameter;
                break;
            case FUNC:	       // 函数调用节点
                ExprPtr.MathFuncPtr = (String)args[0];
                ExprPtr.Child =(ExprNode)args[1];
                break;
            default:	       // 二元运算节点
                ExprPtr.Left =(ExprNode)args[0];
                ExprPtr.Right=(ExprNode)args[1];
                break;
        }
        return ExprPtr;
    }

    /*
    从root开始，对语法树进行深度优先的先序遍历，
    并且根据缩进值indent将当前被遍历的节点打印在适当的位置上。
    */
    void PrintSyntaxTree(ExprNode root, int indent)
    {
        if (root!=null)
        {
            for(int i=0;i<indent;i++)
                System.out.print(" ");
            System.out.println(root.OpCode.toString());
            switch(root.OpCode)
            {
                case CONST_ID:
                    for(int i=0;i<indent+4;i++)
                        System.out.print(" ");
                    System.out.printf("%f\n", root.CaseConst);
                    break;
                case T:
                    for(int i=0;i<indent+4;i++)
                        System.out.print(" ");
                    System.out.println("T");
                    break;
                case FUNC:
                    for(int i=0;i<indent+4;i++)
                        System.out.print(" ");
                    System.out.println(root.MathFuncPtr);
                    PrintSyntaxTree(root.Child, indent + 4);
                    break;
                default:
                    PrintSyntaxTree(root.Left,indent+4);
                    PrintSyntaxTree(root.Right,indent+4);
                    break;
            }
        }
    }

    static void SyntaxError (int case_of)
    {
        switch(case_of)
        {
            case 1:
                System.out.println("ERRTOKEN~~");
                break;
            case 2:
                System.out.println("MATCHTOKEN ERROR~~");
                break;
            default:
                System.out.println("SOMETHING WRONG!!");
                break;
        }
    }

    static void FetchToken()
    {
        token = Lexer.GetToken();
        if (token.type == Token.Token_Type.ERRTOKEN)
            SyntaxError(1);
    }

    void MatchToken (Token.Token_Type AToken)
    {
        if (AToken != token.type)
            SyntaxError(2);

        if (AToken == Token.Token_Type.FUNC)
            System.out.println("MATCH TOKEN: "+token.lexeme);
        else
            System.out.println("MATCH TOKEN: "+AToken.toString());
        FetchToken();
    }

    /*****
     最终表达式的产生式:
     Expression 	→ Term  { ( PLUS | MINUS) Term }
     Term       	→ Factor { ( MUL | DIV ) Factor }
     Factor  	→ PLUS Factor | MINUS Factor | Component
     Component 	→ Atom [POWER Component]
     Atom → CONST_ID
     | T
     | FUNC L_BRACKET Expression R_BRACKET
     | L_BRACKET Expression R_BRACKET
     *****/

    // Expression → Term  { ( PLUS | MINUS) Term }
    ExprNode Expression()
    {
        ExprNode left, right;
        //left = new ExprNode();
        left = Term();
        while (token.type== Token.Token_Type.PLUS || token.type== Token.Token_Type.MINUS)
        {
            Token.Token_Type now_Token=token.type;
            MatchToken(token.type);
            right = Term();
            left = MakeExprNode(now_Token,left,right);
        }
        System.out.println();
        PrintSyntaxTree(left,0);
        System.out.println();
        return left;
    };

    //Term → Factor { ( MUL | DIV ) Factor }
    ExprNode Term()
    {
        ExprNode left, right;
        left = Factor();
        while(token.type== Token.Token_Type.MUL || token.type== Token.Token_Type.DIV)
        {
            Token.Token_Type now_Token=token.type;
            MatchToken(token.type);
            right = Factor();
            left = MakeExprNode(now_Token,left,right);
        }
        return left;
    };

    //Factor → PLUS Factor | MINUS Factor | Component
    ExprNode Factor()
    {
        ExprNode left,right;
        if (token.type== Token.Token_Type.PLUS)
        {
            MatchToken(token.type);
            left = Factor();
        }
        else if (token.type== Token.Token_Type.MINUS)
        {
            MatchToken(token.type);

            left = new ExprNode();
            left.OpCode=Token.Token_Type.CONST_ID;
            left.CaseConst=0.0;
            right = Factor();
            left = MakeExprNode(Token.Token_Type.MINUS,left,right);

        }
        else{
            left = Component();
        }
        return left;
    };

    //Component → Atom [POWER Component]
    ExprNode Component()
    {
        ExprNode left,right;
        left = Atom();
        if (token.type==Token.Token_Type.POWER)
        {
            MatchToken(Token.Token_Type.POWER);
            right = Component();
            left = MakeExprNode(Token.Token_Type.POWER,left,right);
        }
        return left;
    };

    /*
    Atom → CONST_ID
          | T
          | FUNC L_BRACKET Expression R_BRACKET
          | L_BRACKET Expression R_BRACKET
          */
    ExprNode Atom()
    {
        ExprNode left = new ExprNode();
        switch(token.type)
        {
            case CONST_ID:
                left = MakeExprNode(token.type,token.value);
                MatchToken(Token.Token_Type.CONST_ID);
                break;
            case T:
                left = MakeExprNode(token.type);
                MatchToken(Token.Token_Type.T);
                break;
            case FUNC:
                String func = token.lexeme;
                MatchToken(Token.Token_Type.FUNC);
                MatchToken(Token.Token_Type.L_BRACKET);
                left = Expression();
                left = MakeExprNode(Token.Token_Type.FUNC,func,left);
                MatchToken(Token.Token_Type.R_BRACKET);
                break;
            case L_BRACKET:
                MatchToken(Token.Token_Type.L_BRACKET);
                left = Expression();
                MatchToken(Token.Token_Type.R_BRACKET);
                break;
        }
        return left;
    };

    //Program  → { Statement SEMICO }
    void Program()
    {
        while (token.type != Token.Token_Type.NONTOKEN)
        {
            Statement();
            MatchToken(Token.Token_Type.SEMICO);
            System.out.println();
        }
    }

    //Statement →  OriginStatment | ScaleStatment | RotStatment | ForStatment
    void Statement()
    {
        switch(token.type)
        {
            case ORIGIN:
                OriginStatement();
                break;
            case ROT:
                RotStatement();
                break;
            case SCALE:
                ScaleStatement();
                break;
            case FOR:
                ForStatement();
                break;
        }
    }


    //OriginStatment → ORIGIN IS L_BRACKET Expression COMMA Expression R_BRACKET
    //origin is (350, 200);
    void OriginStatement()
    {
        double x,y;
        ExprNode x_prt,y_prt;
        MatchToken(Token.Token_Type.ORIGIN);
        MatchToken(Token.Token_Type.IS);
        MatchToken(Token.Token_Type.L_BRACKET);
        x_prt = Expression();
        x = Semantic.GetExprValue(x_prt);// 获取横坐标的平移值
        MatchToken(Token.Token_Type.COMMA);
        y_prt = Expression();
        y = Semantic.GetExprValue(y_prt);// 获取纵坐标的平移值
        MatchToken(Token.Token_Type.R_BRACKET);
        Semantic.SetOrigin(x,y);
        System.out.println("origin x:"+x+" y: "+y);
    }

    //RotStatment → ROT IS Expression
//rot is pi/6;
    void RotStatement()
    {
        double angle;
        ExprNode rot_prt;
        MatchToken(Token.Token_Type.ROT);
        MatchToken(Token.Token_Type.IS);
        rot_prt = Expression();

        angle = Semantic.GetExprValue(rot_prt);
        Semantic.SetRotAngle(angle);
        System.out.println("rot angle:"+angle);
    }


    //ScaleStatment  → SCALE IS L_BRACKET Expression COMMA Expression R_BRACKET
//scale is (2, 1);
    void ScaleStatement()
    {
        double x,y;
        ExprNode x_prt,y_prt;
        MatchToken(Token.Token_Type.SCALE);
        MatchToken(Token.Token_Type.IS);
        MatchToken(Token.Token_Type.L_BRACKET);
        x_prt = Expression();
        x = Semantic.GetExprValue(x_prt);
        MatchToken(Token.Token_Type.COMMA);
        y_prt = Expression();
        y = Semantic.GetExprValue(y_prt);
        MatchToken(Token.Token_Type.R_BRACKET);
        Semantic.SetScale(x,y);
        System.out.println("scale x:" + x + " y: " + y);
    }


    /*
    ForStatment → FOR T
       FROM Expression
       TO   Expression
       STEP Expression
       DRAW L_BRACKET Expression COMMA Expression R_BRACKET
       */
//for t from -100 to 100 step 1 draw (t, 0);
    void ForStatement()
    {
        double Start,End,Step;
        ExprNode start_ptr, end_ptr, step_ptr,
        x_ptr, y_ptr;
        MatchToken (Token.Token_Type.FOR);
        MatchToken(Token.Token_Type.T);
        MatchToken (Token.Token_Type.FROM);
        start_ptr = Expression();
        Start = Semantic.GetExprValue(start_ptr);
        MatchToken (Token.Token_Type.TO);
        end_ptr   = Expression();
        End = Semantic.GetExprValue(end_ptr);
        MatchToken (Token.Token_Type.STEP);
        step_ptr  = Expression();
        Step = Semantic.GetExprValue(step_ptr);
        MatchToken (Token.Token_Type.DRAW);
        MatchToken (Token.Token_Type.L_BRACKET);
        x_ptr = Expression();
        MatchToken (Token.Token_Type.COMMA);
        y_ptr = Expression();
        MatchToken (Token.Token_Type.R_BRACKET);
        Semantic.DrawLoop(Start, End, Step, x_ptr, y_ptr);
    }


//糊涂1：应该是先构造语法树，后MatchToken
//糊涂2：忘记加break;
//糊涂3: 滥用全局变量
//糊涂4: 换行符
//糊涂5: 忘记把函数加入树中
    //困难:画图……

    void init(String SrcFilePtr)
    {
        if(!Lexer.InitScanner(SrcFilePtr))// 初始化词法分析器
        {
            System.out.println("Open Source File Error !");
            return;
        }


        ans_num = 0;
        panel = new Draw();
        ans = new Semantic.Point[500000];

        System.out.println("ENTER!");
        FetchToken();	// 获取第一个记号
        Program();		// 递归下降分析
        System.out.println("EXIT!");

        JFrame drawFrame = new JFrame("My Draw");
        drawFrame.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        drawFrame.add(panel);
        drawFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        drawFrame.setVisible(true);

        //Lexer.CloseScanner();	// 关闭词法分析器
    }
}
