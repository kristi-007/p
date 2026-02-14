package domain;

public abstract class Body {
	private final String designation; // The designation in Council records
	private String name;

	public Body(String designation, String name) {
		super();
		this.designation = designation;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesignation() {
		return designation;
	}

	@Override
	public int hashCode() {
		final int prime = 997;
		int result = 1;
		result = prime * result + ((this.getDesignation() == null) ? 0 : this.getDesignation().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Body other = (Body) obj;
		if (this.getDesignation() == null) {
			if (other.getDesignation() != null)
				return false;
		} else if (!this.getDesignation().equals(other.getDesignation()))
			return false;
		return true;
	}
	
	public double calculateDistance(Body other) {
		return this.getCoordinate().calculateDistance(other.getCoordinate());
	}
	
	public abstract Coordinate getCoordinate();

}
