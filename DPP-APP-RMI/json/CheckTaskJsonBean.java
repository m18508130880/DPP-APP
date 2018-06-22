package json;

import java.util.ArrayList;
import java.util.List;

public class CheckTaskJsonBean
{
	private String SN;
	private String Leader;
	private String Staff;
	private String CTime;
	private String End_Time;
	private String Project_Id;
	private String Status;
	private String GJ_List;
	private String GX_List;
	private String Des;
	
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
	public String getLeader() {
		return Leader;
	}
	public void setLeader(String leader) {
		Leader = leader;
	}
	public String getStaff() {
		return Staff;
	}
	public void setStaff(String staff) {
		Staff = staff;
	}
	public String getCTime() {
		return CTime;
	}
	public void setCTime(String cTime) {
		CTime = cTime;
	}
	public String getEnd_Time() {
		return End_Time;
	}
	public void setEnd_Time(String end_Time) {
		End_Time = end_Time;
	}
	public String getProject_Id() {
		return Project_Id;
	}
	public void setProject_Id(String project_Id) {
		Project_Id = project_Id;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getGJ_List() {
		return GJ_List;
	}
	public void setGJ_List(String gJ_List) {
		GJ_List = gJ_List;
	}
	public String getGX_List() {
		return GX_List;
	}
	public void setGX_List(String gX_List) {
		GX_List = gX_List;
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