package com.blazebit.persistence.impl.dialect;

public class DB2DbmsDialect extends DefaultDbmsDialect {

	@Override
	public String getWithClause(boolean recursive) {
		return "with";
	}

	@Override
	public boolean supportsQueryReturning() {
		return true;
	}

	@Override
	public void applyQueryReturning(StringBuilder sqlSb, String[] returningColumns) {
		StringBuilder sb = new StringBuilder(100);
		sb.append("SELECT ");
		for (int i = 0; i < returningColumns.length; i++) {
			if (i != 0) {
				sqlSb.append(',');
			}
			sqlSb.append(returningColumns[i]);
		}
		sb.append(" FROM FINAL TABLE (");
		sqlSb.insert(0, sb);
		sqlSb.append(')');
	}

}
