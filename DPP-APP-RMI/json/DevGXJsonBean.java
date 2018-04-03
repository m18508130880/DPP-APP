package json;

import java.util.ArrayList;
import java.util.List;

public class DevGXJsonBean {
	private String Id;
	private String Diameter;
	private String Length;	
	private String Start_Id;
	private String End_Id;
	private String Start_Height;
	private String End_Height;
	private String Material;
	private String Buried_Year;
	private String Data_Lev;
	private String Road;
	private String Flag;
	
	private String Project_Id;	
	private String Project_Name;	
	private String Equip_Id;	
	private String Equip_Name;
	private String Curr_Data;
	private String Subsys_Id;
	
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
	public String getDiameter() {
		return Diameter;
	}
	public void setDiameter(String diameter) {
		Diameter = diameter;
	}
	public String getLength() {
		return Length;
	}
	public void setLength(String length) {
		Length = length;
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
	public String getStart_Height() {
		return Start_Height;
	}
	public void setStart_Height(String start_Height) {
		Start_Height = start_Height;
	}
	public String getEnd_Height() {
		return End_Height;
	}
	public void setEnd_Height(String end_Height) {
		End_Height = end_Height;
	}
	public String getMaterial() {
		return Material;
	}
	public void setMaterial(String material) {
		Material = material;
	}
	public String getBuried_Year() {
		return Buried_Year;
	}
	public void setBuried_Year(String buried_Year) {
		Buried_Year = buried_Year;
	}
	public String getData_Lev() {
		return Data_Lev;
	}
	public void setData_Lev(String data_Lev) {
		Data_Lev = data_Lev;
	}
	public String getRoad() {
		return Road;
	}
	public void setRoad(String road) {
		Road = road;
	}
	public String getFlag() {
		return Flag;
	}
	public void setFlag(String flag) {
		Flag = flag;
	}
	public String getProject_Id() {
		return Project_Id;
	}
	public void setProject_Id(String project_Id) {
		Project_Id = project_Id;
	}
	public String getProject_Name() {
		return Project_Name;
	}
	public void setProject_Name(String project_Name) {
		Project_Name = project_Name;
	}
	public String getEquip_Id() {
		return Equip_Id;
	}
	public void setEquip_Id(String equip_Id) {
		Equip_Id = equip_Id;
	}
	public String getEquip_Name() {
		return Equip_Name;
	}
	public void setEquip_Name(String equip_Name) {
		Equip_Name = equip_Name;
	}
	public String getCurr_Data() {
		return Curr_Data;
	}
	public void setCurr_Data(String curr_Data) {
		Curr_Data = curr_Data;
	}
	public String getSubsys_Id() {
		return Subsys_Id;
	}
	public void setSubsys_Id(String subsys_Id) {
		Subsys_Id = subsys_Id;
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
