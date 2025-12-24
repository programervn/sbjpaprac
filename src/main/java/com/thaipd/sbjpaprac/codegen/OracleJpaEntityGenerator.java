package com.thaipd.sbjpaprac.codegen;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class OracleJpaEntityGenerator {

	public static void main(String[] args) throws Exception {
		Map<String, String> p = parseArgs(args);

		String url = requiredWithEnv(p, "url", "DB_URL");
		String username = requiredWithEnv(p, "username", "DB_USERNAME");
		String password = requiredWithEnv(p, "password", "DB_PASSWORD");
		String schema = optionalWithEnv(p, "schema", "DB_SCHEMA");
		if (schema == null || schema.isBlank()) {
			schema = username;
		}
		schema = schema.toUpperCase(Locale.ROOT);

		String basePackage = p.getOrDefault("basePackage", "com.thaipd.sbjpaprac.entity.db");
		Path outDir = Paths.get(p.getOrDefault("out", "src/main/java"));
		Path packageDir = outDir.resolve(basePackage.replace('.', '/'));
		Files.createDirectories(packageDir);

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			DatabaseMetaData meta = connection.getMetaData();

			List<String> tables = listTables(meta, schema);
			for (String table : tables) {
				TableModel tableModel = readTable(meta, schema, table);
				GeneratedFiles generated = generateJava(basePackage, schema, tableModel);
				writeFile(packageDir.resolve(generated.entityName + ".java"), generated.entitySource);
				if (generated.idName != null && generated.idSource != null) {
					writeFile(packageDir.resolve(generated.idName + ".java"), generated.idSource);
				}
			}
		}
	}

	private static List<String> listTables(DatabaseMetaData meta, String schema) throws SQLException {
		List<String> tables = new ArrayList<>();
		try (ResultSet rs = meta.getTables(null, schema, "%", new String[]{"TABLE"})) {
			while (rs.next()) {
				tables.add(rs.getString("TABLE_NAME"));
			}
		}
		tables.sort(String::compareTo);
		return tables;
	}

	private static TableModel readTable(DatabaseMetaData meta, String schema, String table) throws SQLException {
		Map<String, ColumnModel> columns = new LinkedHashMap<>();
		try (ResultSet rs = meta.getColumns(null, schema, table, "%")) {
			while (rs.next()) {
				ColumnModel c = new ColumnModel();
				c.name = rs.getString("COLUMN_NAME");
				c.jdbcType = rs.getInt("DATA_TYPE");
				c.typeName = rs.getString("TYPE_NAME");
				c.size = rs.getInt("COLUMN_SIZE");
				c.decimalDigits = rs.getInt("DECIMAL_DIGITS");
				c.nullable = rs.getInt("NULLABLE") != DatabaseMetaData.columnNoNulls;
				c.isAutoIncrement = "YES".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT"));
				columns.put(c.name, c);
			}
		}

		Set<String> pk = new TreeSet<>();
		try (ResultSet rs = meta.getPrimaryKeys(null, schema, table)) {
			while (rs.next()) {
				pk.add(rs.getString("COLUMN_NAME"));
			}
		}

		TableModel t = new TableModel();
		t.schema = schema;
		t.name = table;
		t.columns = new ArrayList<>(columns.values());
		t.primaryKeyColumns = pk;
		return t;
	}

	private static GeneratedFiles generateJava(String basePackage, String schema, TableModel table) {
		String entityName = toClassName(table.name);

		Set<String> auditColumns = table.columns.stream()
				.map(c -> c.name.toLowerCase(Locale.ROOT))
				.collect(Collectors.toSet());

		boolean canExtendBaseEntity = auditColumns.contains("created_at") && auditColumns.contains("updated_at");

		List<ColumnModel> columns = new ArrayList<>(table.columns);
		columns.sort(Comparator.comparing(c -> c.name));

		List<ColumnModel> effectiveColumns = new ArrayList<>();
		for (ColumnModel c : columns) {
			String lc = c.name.toLowerCase(Locale.ROOT);
			if (canExtendBaseEntity && (lc.equals("created_at") || lc.equals("updated_at"))) {
				continue;
			}
			effectiveColumns.add(c);
		}

		Set<String> pk = table.primaryKeyColumns;
		List<ColumnModel> pkColumns = effectiveColumns.stream().filter(c -> pk.contains(c.name)).toList();

		if (pk.isEmpty()) {
			pkColumns = List.of();
		}

		Set<String> imports = new HashSet<>();
		imports.add(Entity.class.getName());
		imports.add(Table.class.getName());
		imports.add(Column.class.getName());

		String idType = null;
		String idName = null;
		String idSource = null;

		StringBuilder fieldsAndAccessors = new StringBuilder();
		Set<String> usedFieldNames = new HashSet<>();

		if (pkColumns.size() == 1) {
			ColumnModel idColumn = pkColumns.getFirst();
			String javaType = mapJavaType(imports, idColumn);
			String fieldName = uniqueFieldName(usedFieldNames, toFieldName(idColumn.name));

			imports.add(Id.class.getName());
			fieldsAndAccessors.append("\n\t@Id\n");
			if (idColumn.isAutoIncrement) {
				imports.add(GeneratedValue.class.getName());
				imports.add(GenerationType.class.getName());
				fieldsAndAccessors.append("\t@GeneratedValue(strategy = GenerationType.IDENTITY)\n");
			}
			fieldsAndAccessors.append(columnAnnotation(idColumn, true));
			fieldsAndAccessors.append("\tprivate " + javaType + " " + fieldName + ";\n");
			fieldsAndAccessors.append(getterSetter(javaType, fieldName));
		} else if (pkColumns.size() > 1) {
			imports.add(EmbeddedId.class.getName());
			String pkClassName = entityName + "Id";
			idName = pkClassName;

			String pkFieldName = "id";
			fieldsAndAccessors.append("\n\t@EmbeddedId\n");
			fieldsAndAccessors.append("\tprivate " + pkClassName + " " + pkFieldName + ";\n");
			fieldsAndAccessors.append(getterSetter(pkClassName, pkFieldName));

			idSource = generateEmbeddableId(basePackage, pkClassName, pkColumns, imports);
		} else {
			// no primary key detected
		}

		for (ColumnModel c : effectiveColumns) {
			if (pk.contains(c.name) && pkColumns.size() == 1) {
				continue;
			}
			if (pk.contains(c.name) && pkColumns.size() > 1) {
				continue;
			}

			String javaType = mapJavaType(imports, c);
			String fieldName = uniqueFieldName(usedFieldNames, toFieldName(c.name));
			fieldsAndAccessors.append("\n");
			fieldsAndAccessors.append(columnAnnotation(c, false));
			fieldsAndAccessors.append("\tprivate " + javaType + " " + fieldName + ";\n");
			fieldsAndAccessors.append(getterSetter(javaType, fieldName));
		}

		String tableAnnotation = "@Table(name = \"" + table.name + "\", schema = \"" + schema + "\")";

		String extendsClause = canExtendBaseEntity ? " extends com.thaipd.sbjpaprac.entity.BaseEntity" : "";
		if (canExtendBaseEntity) {
			imports.add("com.thaipd.sbjpaprac.entity.BaseEntity");
		}

		String source = "package " + basePackage + ";\n\n"
				+ renderImports(basePackage, imports)
				+ "\n@Entity\n"
				+ tableAnnotation + "\n"
				+ "public class " + entityName + extendsClause + " {\n"
				+ fieldsAndAccessors
				+ "}\n";

		GeneratedFiles f = new GeneratedFiles();
		f.entityName = entityName;
		f.entitySource = source;
		f.idName = idName;
		f.idSource = idSource;
		f.idType = idType;
		return f;
	}

	private static String generateEmbeddableId(String basePackage, String pkClassName, List<ColumnModel> pkColumns, Set<String> entityImports) {
		Set<String> imports = new HashSet<>();
		imports.add(Embeddable.class.getName());
		imports.add(Column.class.getName());
		imports.add("java.io.Serializable");
		imports.add(Objects.class.getName());

		Set<String> usedFieldNames = new HashSet<>();
		StringBuilder fields = new StringBuilder();
		for (ColumnModel c : pkColumns) {
			String javaType = mapJavaType(imports, c);
			String fieldName = uniqueFieldName(usedFieldNames, toFieldName(c.name));
			fields.append("\n");
			fields.append(columnAnnotation(c, true));
			fields.append("\tprivate " + javaType + " " + fieldName + ";\n");
			fields.append(getterSetter(javaType, fieldName));
		}

		String equalsArgs = pkColumns.stream()
				.map(c -> toFieldName(c.name))
				.map(OracleJpaEntityGenerator::sanitizeIdentifier)
				.collect(Collectors.joining(", "));

		String objectsEquals = pkColumns.stream()
				.map(c -> "Objects.equals(" + sanitizeIdentifier(toFieldName(c.name)) + ", that." + sanitizeIdentifier(toFieldName(c.name)) + ")")
				.collect(Collectors.joining(" && "));

		String objectsHash = pkColumns.stream()
				.map(c -> sanitizeIdentifier(toFieldName(c.name)))
				.collect(Collectors.joining(", "));

		return "package " + basePackage + ";\n\n"
				+ renderImports(basePackage, imports)
				+ "\n@Embeddable\n"
				+ "public class " + pkClassName + " implements Serializable {\n"
				+ fields
				+ "\n\t@Override\n"
				+ "\tpublic boolean equals(Object o) {\n"
				+ "\t\tif (this == o) return true;\n"
				+ "\t\tif (o == null || getClass() != o.getClass()) return false;\n"
				+ "\t\t" + pkClassName + " that = (" + pkClassName + ") o;\n"
				+ "\t\treturn " + (objectsEquals.isBlank() ? "true" : objectsEquals) + ";\n"
				+ "\t}\n"
				+ "\n\t@Override\n"
				+ "\tpublic int hashCode() {\n"
				+ "\t\treturn Objects.hash(" + objectsHash + ");\n"
				+ "\t}\n"
				+ "}\n";
	}

	private static String renderImports(String basePackage, Set<String> imports) {
		List<String> sorted = imports.stream()
				.filter(i -> !i.startsWith("java.lang."))
				.sorted()
				.toList();
		if (sorted.isEmpty()) {
			return "";
		}
		StringBuilder b = new StringBuilder();
		for (String i : sorted) {
			if (i.startsWith(basePackage + ".")) {
				continue;
			}
			b.append("import ").append(i).append(";\n");
		}
		return b.toString();
	}

	private static String columnAnnotation(ColumnModel c, boolean partOfPk) {
		StringBuilder b = new StringBuilder();
		b.append("\t@Column(name = \"").append(c.name).append("\"");

		if (!c.nullable && !partOfPk) {
			b.append(", nullable = false");
		}

		if (isStringType(c.jdbcType) && c.size > 0) {
			b.append(", length = ").append(c.size);
		}

		if (isNumericType(c.jdbcType) && c.size > 0) {
			b.append(", precision = ").append(c.size);
			if (c.decimalDigits > 0) {
				b.append(", scale = ").append(c.decimalDigits);
			}
		}

		b.append(")\n");
		return b.toString();
	}

	private static boolean isStringType(int jdbcType) {
		return jdbcType == Types.CHAR
				|| jdbcType == Types.NCHAR
				|| jdbcType == Types.VARCHAR
				|| jdbcType == Types.NVARCHAR
				|| jdbcType == Types.LONGVARCHAR
				|| jdbcType == Types.LONGNVARCHAR;
	}

	private static boolean isNumericType(int jdbcType) {
		return jdbcType == Types.NUMERIC || jdbcType == Types.DECIMAL;
	}

	private static String mapJavaType(Set<String> imports, ColumnModel c) {
		return switch (c.jdbcType) {
			case Types.CHAR, Types.NCHAR, Types.VARCHAR, Types.NVARCHAR, Types.LONGVARCHAR, Types.LONGNVARCHAR -> "String";
			case Types.CLOB, Types.NCLOB -> "String";
			case Types.BOOLEAN, Types.BIT -> "Boolean";
			case Types.TINYINT, Types.SMALLINT, Types.INTEGER -> "Integer";
			case Types.BIGINT -> "Long";
			case Types.FLOAT, Types.REAL, Types.DOUBLE -> "Double";
			case Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY, Types.BLOB -> "byte[]";
			case Types.DATE -> {
				imports.add(LocalDate.class.getName());
				yield "LocalDate";
			}
			case Types.TIME -> {
				imports.add(LocalTime.class.getName());
				yield "LocalTime";
			}
			case Types.TIMESTAMP -> {
				imports.add(LocalDateTime.class.getName());
				yield "LocalDateTime";
			}
			case Types.TIMESTAMP_WITH_TIMEZONE -> {
				imports.add(OffsetDateTime.class.getName());
				yield "OffsetDateTime";
			}
			case Types.NUMERIC, Types.DECIMAL -> mapNumber(imports, c);
			default -> {
				imports.add(Object.class.getName());
				yield "Object";
			}
		};
	}

	private static String mapNumber(Set<String> imports, ColumnModel c) {
		int precision = c.size;
		int scale = c.decimalDigits;
		if (scale == 0) {
			if (precision > 0 && precision <= 9) {
				return "Integer";
			}
			if (precision > 0 && precision <= 18) {
				return "Long";
			}
			imports.add(BigInteger.class.getName());
			return "BigInteger";
		}
		imports.add(BigDecimal.class.getName());
		return "BigDecimal";
	}

	private static void writeFile(Path path, String content) {
		try {
			Files.createDirectories(path.getParent());
			Files.writeString(path, content, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static Map<String, String> parseArgs(String[] args) {
		Map<String, String> p = new HashMap<>();
		for (int i = 0; i < args.length; i++) {
			String a = args[i];
			if (a.startsWith("--")) {
				String key = a.substring(2);
				String value = (i + 1 < args.length) ? args[i + 1] : null;
				if (value != null && !value.startsWith("--")) {
					p.put(key, value);
					i++;
				} else {
					p.put(key, "true");
				}
			}
		}
		return p;
	}

	private static String required(Map<String, String> p, String key) {
		String v = p.get(key);
		if (v == null || v.isBlank()) {
			throw new IllegalArgumentException("Missing --" + key);
		}
		return v;
	}

	private static String requiredWithEnv(Map<String, String> p, String key, String envKey) {
		String v = p.get(key);
		if (v == null || v.isBlank()) {
			v = System.getenv(envKey);
		}
		if (v == null || v.isBlank()) {
			throw new IllegalArgumentException("Missing --" + key + " (or env " + envKey + ")");
		}
		return v;
	}

	private static String optionalWithEnv(Map<String, String> p, String key, String envKey) {
		String v = p.get(key);
		if (v == null || v.isBlank()) {
			v = System.getenv(envKey);
		}
		return v;
	}

	private static String toClassName(String tableName) {
		String n = tableName.toLowerCase(Locale.ROOT);
		String[] parts = n.split("_");
		StringBuilder b = new StringBuilder();
		for (String part : parts) {
			if (part.isBlank()) continue;
			b.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
		}
		String r = b.toString();
		if (r.endsWith("s") && r.length() > 1) {
			r = r.substring(0, r.length() - 1);
		}
		return sanitizeIdentifier(r);
	}

	private static String toFieldName(String columnName) {
		String n = columnName.toLowerCase(Locale.ROOT);
		String[] parts = n.split("_");
		if (parts.length == 0) {
			return sanitizeIdentifier(n);
		}
		StringBuilder b = new StringBuilder(parts[0]);
		for (int i = 1; i < parts.length; i++) {
			String part = parts[i];
			if (part.isBlank()) continue;
			b.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
		}
		return sanitizeIdentifier(b.toString());
	}

	private static String uniqueFieldName(Set<String> used, String base) {
		String n = base;
		int i = 2;
		while (used.contains(n)) {
			n = base + i;
			i++;
		}
		used.add(n);
		return n;
	}

	private static String sanitizeIdentifier(String name) {
		String n = name;
		if (!n.isEmpty() && Character.isDigit(n.charAt(0))) {
			n = "v" + n;
		}
		Set<String> reserved = Set.of(
				"class", "public", "private", "protected", "static", "final", "void", "int", "long", "double",
				"float", "short", "byte", "boolean", "char", "package", "import", "return", "new"
		);
		if (reserved.contains(n)) {
			n = n + "Value";
		}
		return n;
	}

	private static String getterSetter(String type, String fieldName) {
		String m = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		return "\n\tpublic " + type + " get" + m + "() {\n"
				+ "\t\treturn " + fieldName + ";\n"
				+ "\t}\n"
				+ "\n\tpublic void set" + m + "(" + type + " " + fieldName + ") {\n"
				+ "\t\tthis." + fieldName + " = " + fieldName + ";\n"
				+ "\t}\n";
	}

	private static class GeneratedFiles {
		private String entityName;
		private String entitySource;
		private String idName;
		private String idSource;
		private String idType;
	}

	private static class TableModel {
		private String schema;
		private String name;
		private List<ColumnModel> columns;
		private Set<String> primaryKeyColumns;
	}

	private static class ColumnModel {
		private String name;
		private int jdbcType;
		private String typeName;
		private int size;
		private int decimalDigits;
		private boolean nullable;
		private boolean isAutoIncrement;
	}
}
