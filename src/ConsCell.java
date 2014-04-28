
public class ConsCell implements Cloneable {
	public String value;
	public ConsCell car;
	public ConsCell cdr;
	
	public ConsCell(){
		
	}
	public ConsCell(String value){
		this.value = value;
	}
	
	public ConsCell(ConsCell car, ConsCell cdr){
		this.car = car;
		this.cdr = cdr;
	}
	
	public Object clone(){
		ConsCell myself = this;
		ConsCell clone = new ConsCell();
		ConsCell firstClone = new ConsCell();
		firstClone = clone;
		while(myself.value.equals("Nil") != true){
			if(myself.value.equals("car")){
				clone.value = myself.value;
				ConsCell car = new ConsCell();
				car = (ConsCell)myself.car.clone();
				clone.car = car;
				ConsCell cdr = new ConsCell();
				clone.cdr = cdr;
				clone = clone.cdr;
			}else{
				clone.value = myself.value;
				ConsCell cdr = new ConsCell();
				cdr.value = myself.cdr.value;
				clone.cdr = cdr;
				clone = clone.cdr;
			}
			myself = myself.cdr;
		}
		clone.value = myself.value;
		return firstClone;
		
	}

}
