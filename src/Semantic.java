/**
 * Created by kkpanda on 2014/12/6.
 */

import java.lang.*;

public class Semantic{

    static Double Parameter=0.0;		    // 为参数T分配的变量
    static Double Origin_x=0.0, Origin_y=0.0;// 用于记录平移距离
    static Double Rot_angle=0.0;		    // 用于记录旋转角度
    static Double Scale_x=1.0, Scale_y=1.0;	    // 用于记录比例因子

    Semantic()
    {
    }

    static class Point
    {
        double x_val;
        double y_val;

        Point()
        {
            x_val=0;
            y_val=0;
        }

        Point(double x,double y)
        {
            x_val = x;
            y_val = y;
        }
    }

    static void SemanticError (int case_of)
    {
        switch(case_of)
        {
            case 1:
                System.out.println("POWER EXPONENT ERROR~~");
                break;
            case 2:
                System.out.println("NO SUCH FUNCTION~~");
            default:
                System.out.println("SOMETHING WRONG!!");
                break;
        }
    }

    public static void SetOrigin(double x,double y)
    {
        Origin_x = x;
        Origin_y = y;
    }

    public static void SetRotAngle(double angle)
    {
        Rot_angle = angle;
    }

    public static void SetScale(double x,double y)
    {
        Scale_x = x;
        Scale_y = y;
    }


    //表达式值的计算
    public static double GetExprValue(ExprNode root)
    {
        if (root == null) return 0.0;
        switch (root.OpCode)
        {
            case PLUS:
                return GetExprValue(root.Left )
                        + GetExprValue(root.Right) ;
            case MINUS:
                return GetExprValue(root.Left )
                        - GetExprValue(root.Right) ;
            case MUL:
                return GetExprValue(root.Left )
                        * GetExprValue(root.Right) ;
            case DIV:
                return GetExprValue(root.Left )
                        / GetExprValue(root.Right) ;
            case POWER:
                double tmp=1.0;
                double bn=GetExprValue(root.Left );
                double ex=GetExprValue(root.Right);
                if ((int)ex-ex!=0)
                    SemanticError(1);
                for(int i=1;i<=(int)ex;i++)
                    tmp*=bn;
                return tmp;
            case FUNC:
                Double para = GetExprValue(root.Child);
                if (root.MathFuncPtr=="SIN")
                {
                    return Math.sin(para);
                }
                else if (root.MathFuncPtr=="COS")
                {
                    return Math.cos(para);
                }
                else if (root.MathFuncPtr=="TAN")
                {
                    return Math.tan(para);
                }
                else if (root.MathFuncPtr=="LN")
                {
                    return Math.log(para);
                }
                else if (root.MathFuncPtr=="EXP")
                {
                    return Math.exp(para);
                }
                else if (root.MathFuncPtr=="SQRT")
                {
                    return Math.sqrt(para);
                }
                else {
                    SemanticError(2);
                }
            case CONST_ID : return root.CaseConst ;
            case T:         return Parameter;
            default:        return 0.0 ;
        }
    }


    // 计算点的坐标值
    static void CalcCoord (ExprNode x_nptr,ExprNode y_nptr,Point now)
    {
        double local_x, local_y, temp;
        local_x=GetExprValue(x_nptr); 	// 计算点的原始坐标
        local_y=GetExprValue(y_nptr);
        local_x *= Scale_x; 			// 比例变换
        local_y *= Scale_y;
        temp=local_x*Math.cos(Rot_angle)+local_y*Math.sin(Rot_angle);
        local_y=local_y*Math.cos(Rot_angle)-local_x*Math.sin(Rot_angle);
        local_x = temp; 			// 旋转变换
        local_x += Origin_x;			// 平移变换
        local_y += Origin_y;
        now.x_val = local_x; 			// 返回变换后点的坐标
        now.y_val = local_y;
    }


    //点轨迹的循环绘制
    public static void DrawLoop(Double Start,Double End,Double Step,
                      ExprNode x_ptr, ExprNode y_ptr)
    {
        Point now = new Point();
        System.out.println("x,y: " + now.x_val + " " + now.y_val);
        for(Parameter=Start; Parameter<=End; Parameter+=Step)
        {
            CalcCoord(x_ptr, y_ptr, now);   // 计算实际坐标
            System.out.println("x,y: "+now.x_val+" "+now.y_val);
            DrawPixel(now.x_val, now.y_val);
            // 根据坐标绘制点
        }
    }

    static void DrawPixel(double x,double y)
    {
        Parser.ans_num++;
        Parser.ans[Parser.ans_num]= new Point(x,y);
    }
}
