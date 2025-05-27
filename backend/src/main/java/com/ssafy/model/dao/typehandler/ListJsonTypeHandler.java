package com.ssafy.model.dao.typehandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.model.dto.domain.Document;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.Collections;
import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class ListJsonTypeHandler extends BaseTypeHandler<List<Document.Item>> {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final TypeReference<List<Document.Item>> TYPE_REF = new TypeReference<>() {};

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
																	List<Document.Item> parameter, JdbcType jdbcType)
		throws SQLException {
		try {
			ps.setString(i, mapper.writeValueAsString(parameter));
		} catch (Exception e) {
			throw new SQLException("Failed to serialize schedules to JSON", e);
		}
	}

	@Override
	public List<Document.Item> getNullableResult(ResultSet rs, String columnName)
		throws SQLException {
		String json = rs.getString(columnName);
		if (json == null) return Collections.emptyList();
		try {
			return mapper.readValue(json, TYPE_REF);
		} catch (Exception e) {
			throw new SQLException("Failed to deserialize JSON to schedules", e);
		}
	}

	@Override
	public List<Document.Item> getNullableResult(ResultSet rs, int columnIndex)
		throws SQLException {
		return getNullableResult(rs, rs.findColumn(rs.getMetaData().getColumnName(columnIndex)));
	}

	@Override
	public List<Document.Item> getNullableResult(CallableStatement cs, int columnIndex)
		throws SQLException {
		String json = cs.getString(columnIndex);
		if (json == null) return Collections.emptyList();
		try {
			return mapper.readValue(json, TYPE_REF);
		} catch (Exception e) {
			throw new SQLException("Failed to deserialize JSON to schedules", e);
		}
	}
}