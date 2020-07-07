package xyz.bumbing.param.vo;

public class TotalVo {

    private String name;
    private String age;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getAge() {
	return age;
    }

    public void setAge(String age) {
	this.age = age;
    }

    private int no;
    private String type;

    public int getNo() {
	return no;
    }

    public void setNo(int no) {
	this.no = no;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    @Override
    public String toString() {
	return "TotalVo [name=" + name + ", age=" + age + ", no=" + no + ", type=" + type + "]";
    }

}
