package cn.gy4j.monitor.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-17
 */
public class MethodLimitCheck extends AbstractCheck {
    private static final int DEFAULT_MAX = 30;
    private int max = DEFAULT_MAX;

    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF};
    }

    public int[] getAcceptableTokens() {
        return new int[0];
    }

    public int[] getRequiredTokens() {
        return new int[0];
    }

    @Override
    public void visitToken(DetailAST ast) {
        // find the OBJBLOCK node below the CLASS_DEF/INTERFACE_DEF
        DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);

        // count the number of direct children of the OBJBLOCK
        // that are METHOD_DEFS
        int methodDefs = objBlock.getChildCount(TokenTypes.METHOD_DEF);

        // report error if limit is reached
        if (methodDefs > this.max) {
            String message = "too many methods, only " + this.max + " are allowed";
            log(ast.getLineNo(), message);
        }
    }

    public void setMax(int max) {
        this.max = max;
    }
}


