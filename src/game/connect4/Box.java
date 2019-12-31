package game.connect4;

public class Box {

	private int x, y, value;

	public Box(int x, int y, int value) {
		super();
		this.x = x;
		this.y = y;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return this.value+"";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		result = prime * result + x;
		result = prime * result + y;
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
		Box other = (Box) obj;
		if (value != other.value)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	@Override
	protected Box clone()  {
		return new Box(x, y, value);
	}
	
	
	
	
}
