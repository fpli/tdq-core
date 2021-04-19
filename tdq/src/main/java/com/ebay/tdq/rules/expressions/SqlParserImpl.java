package com.ebay.tdq.rules.expressions;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;

/**
 * @author juntzhang
 */
public class SqlParserImpl {
    private static final FrameworkConfig config = Frameworks.newConfigBuilder()
            .parserConfig(SqlParser.configBuilder()
                    .setCaseSensitive(false)
                    .setQuoting(Quoting.BACK_TICK)
                    .setQuotedCasing(Casing.UNCHANGED)
                    .setUnquotedCasing(Casing.UNCHANGED)
                    .build()).operatorTable(SqlStdOperatorTable.instance())
            .build();

    private static SqlSelect getSql(String sql) throws SqlParseException {
        SqlParser parser = SqlParser.create(sql, config.getParserConfig());
        return (SqlSelect) parser.parseStmt();
    }

    public static SqlNode getExpr(String str) throws SqlParseException {
        SqlParser parser = SqlParser.create("SELECT " + str, config.getParserConfig());
        return ((SqlSelect) parser.parseStmt()).getSelectList().get(0);
    }

    public static void main(String[] args) {

    }

}
