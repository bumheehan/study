package pattern.java.chap1.polymorphism;

public class Polymorphism {
	
	public static void main(String[] args) {
		A[] a = {new B(),new C(),new D()}; 
		a[0].print();
		//((C)a[0]).print(); //에러, 형제 클래스끼리는 형변환 안됨
		a[2].print();
		//a[2].print2(); // 에러, A에는 print만 있기때문에 D의 print2를 사용할 수 없다.
		((D)a[2]).print2();
		
	}
	

}
