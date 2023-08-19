package models;

public class ListModel {

	private String id;
	private String name;
	private int mem_num;
	private String img;
	private String us;
	


	public ListModel(String id, String name, int mem_num, String img,String user) {
		super();
		this.id = id; 
		this.name = name;
		this.mem_num = mem_num;
		this.img = img;
		us= user;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMem_num() {
		return mem_num;
	}

	public void setMem_num(int mem_num) {
		this.mem_num = mem_num;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
