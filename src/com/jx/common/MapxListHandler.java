package com.jx.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.AbstractListHandler;

/**
 * @author Administrator DButils用到的数据库结果处理类 本工程用的数据库操作库是DButils
 */
public class MapxListHandler extends AbstractListHandler<Mapx<String, Object>> {

	/**
	 * The RowProcessor implementation to use when converting rows into Maps.
	 */
	private final RowProcessor convert;

	/**
	 * Creates a new instance of MapListHandler using a
	 * <code>BasicRowProcessor</code> for conversion.
	 */
	public MapxListHandler() {
		convert = new BasicRowProcessor();
	}

	@Override
	protected Mapx<String, Object> handleRow(ResultSet rs) throws SQLException {
		Map<String, Object> map = this.convert.toMap(rs);
		return new Mapx<String, Object>(map);
	}

}
