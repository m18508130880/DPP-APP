package json;

import java.util.ArrayList;
import java.util.List;

public class TopoGXJsonBean {
	private String Id;
	private String Start_Id;
	private String End_Id;
	private String Flag;
	private String Road;
	private String Length;
	
	private String Url;
	private String Rst;
	private String Token;
	List<Object> CData = new ArrayList<Object>();
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getStart_Id() {
		return Start_Id;
	}
	public void setStart_Id(String start_Id) {
		Start_Id = start_Id;
	}
	public String getEnd_Id() {
		return End_Id;
	}
	public void setEnd_Id(String end_Id) {
		End_Id = end_Id;
	}
	public String getFlag() {
		return Flag;
	}
	public void setFlag(String flag) {
		Flag = flag;
	}
	public String getRoad() {
		return Road;
	}
	public void setRoad(String road) {
		Road = road;
	}
	public String getLength() {
		return Length;
	}
	public void setLength(String length) {
		Length = length;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getRst() {
		return Rst;
	}
	public void setRst(String rst) {
		Rst = rst;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public List<Object> getCData() {
		return CData;
	}
	public void setCData(List<Object> cData) {
		CData = cData;
	}
	
}
