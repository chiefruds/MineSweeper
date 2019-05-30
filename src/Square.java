
public class Square {
	
	/*
	 * the type of a square can be normal or marked
	 * bombCount holds the count of the bomb around the square
	 * marked is a boolean because it is not a type
	 */
	private String type;
	private int bombCount;
	private boolean marked;

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	public int getBombCount() {
		return bombCount;
	}

	public void setBombCount(int bombCount) {
		this.bombCount = bombCount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
