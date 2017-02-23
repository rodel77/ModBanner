package mx.com.rodel.modbanner;

public interface ModData {
	public String getName();
	public String getVersion();
	
	default String getCompleteData(){
		return getName()+" "+getVersion();
	}
}
