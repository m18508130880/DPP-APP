package json;

import java.util.ArrayList;
import java.util.List;

public class CheckTaskGJJsonBean
{
	private String	SN;
	private String	Id;
	private String	Project_Id;
	private String	CT_SN;
	private String	Status;
	private String	Images;
	private String	Check_Time;
	private String	Longitude;
	private String	Latitude;
	private String	WX_Lng;
	private String	WX_Lat;
	private String	In_Id;
	private String	Out_Id;
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
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getWX_Lng() {
		return WX_Lng;
	}
	public void setWX_Lng(String wX_Lng) {
		WX_Lng = wX_Lng;
	}
	public String getWX_Lat() {
		return WX_Lat;
	}
	public void setWX_Lat(String wX_Lat) {
		WX_Lat = wX_Lat;
	}
	public String getIn_Id() {
		return In_Id;
	}
	public void setIn_Id(String in_Id) {
		In_Id = in_Id;
	}
	public String getOut_Id() {
		return Out_Id;
	}
	public void setOut_Id(String out_Id) {
		Out_Id = out_Id;
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