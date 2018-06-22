package json;

import java.util.ArrayList;
import java.util.List;

public class CheckTaskGXJsonBean
{
	private String	SN;
	private String	Id;
	private String	Project_Id;
	private String	CT_SN;
	private String	Status;
	private String	Images;
	private String	Check_Time;
	private String	Start_Id;
	private String	End_Id;
	private String	Road;
	private String	Des;

	private String Url;
	private String Rst;
	private String Token;
	List<Object> CData = new ArrayList<Object>();
	public String getSN() {
		return SN;
	}
	public void setSN(String sN) {
		SN = sN;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getProject_Id() {
		return Project_Id;
	}
	public void setProject_Id(String project_Id) {
		Project_Id = project_Id;
	}
	public String getCT_SN() {
		return CT_SN;
	}
	public void setCT_SN(String cT_SN) {
		CT_SN = cT_SN;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getImages() {
		return Images;
	}
	public void setImages(String images) {
		Images = images;
	}
	public String getCheck_Time() {
		return Check_Time;
	}
	public void setCheck_Time(String check_Time) {
		Check_Time = check_Time;
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
	public String getRoad() {
		return Road;
	}
	public void setRoad(String road) {
		Road = road;
	}
	public String getDes() {
		return Des;
	}
	public void setDes(String des) {
		Des = des;
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