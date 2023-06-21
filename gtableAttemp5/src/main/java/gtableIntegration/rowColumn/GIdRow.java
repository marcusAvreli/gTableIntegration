package gtableIntegration.rowColumn;
//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Common/src/dynagent/common/utils/GIdRow.java
public class GIdRow {

	private int ido;
	private int idto;
	private String rdn;

	public GIdRow(int ido, int idto, String rdn) {
		super();
		this.ido = ido;
		this.idto = idto;
		this.rdn = rdn;
	}
	
	public int getIdo() {
		return ido;
	}

	public void setIdo(int ido) {
		this.ido = ido;
	}

	public int getIdto() {
		return idto;
	}

	public void setIdto(int idto) {
		this.idto = idto;
	}

	public String getRdn() {
		return rdn;
	}

	public void setRdn(String rdn) {
		this.rdn = rdn;
	}

	@Override
	public String toString() {
		return "GIdRow ido:"+ido+" idto:"+idto+" rdn:"+rdn;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GIdRow other = (GIdRow) obj;
		if (ido != other.ido)
			return false;
		if (idto != other.idto)
			return false;
		if (rdn == null) {
			if (other.rdn != null)
				return false;
		} else if (!rdn.equals(other.rdn))
			return false;
		return true;
	}
	
	
	
}