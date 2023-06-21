package gtableIntegration.table.rowColumn;

public class ColumnDefinition {

	private int ordering;
	private String id;
	private String fieldName;
	private String headerName;
	
	public ColumnDefinition(int ordering, String fieldName, String headerName) {
		super();
		this.ordering = ordering;
		this.id=fieldName;
		this.fieldName = fieldName;
		this.headerName = headerName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getOrdering() {
		return ordering;
	}
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	
	
	
}
