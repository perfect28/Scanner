/**
 * Created by kkpanda on 2014/12/6.
 */
public class ExprNode {
    Token.Token_Type OpCode;	// 记号种类

    ExprNode Left, Right;
            // 二元运算

    ExprNode Child;
    String MathFuncPtr;
            // 函数调用

    Double CaseConst; 	// 常数，绑定右值
    Double CaseParmPtr; 	// 参数T，绑定左值

    ExprNode()
    {

    }
}
