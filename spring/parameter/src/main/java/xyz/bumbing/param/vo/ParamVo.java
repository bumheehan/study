package xyz.bumbing.param.vo;

public class ParamVo {

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
	return "ParamVo [no=" + no + ", type=" + type + "]";
    }

}
