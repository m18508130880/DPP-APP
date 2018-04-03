package json;

import java.util.ArrayList;
import java.util.List;

public class ProjectInfoJsonBean
{
	private String Id;
	private String CName;
	private String Longitude;
	private String Latitude;
	private String MapLev;
	private String MapAngle;
	private String Demo;
	private String Sid;
	
	private String Url;
	private String Rst;
	private String Token;
	List<Object> CData = new ArrayList<Object>();
	
	public String getUrl()
	{
		return Url;
	}

	public void setUrl(String url)
	{
		Url = url;
	}

	public String getRst()
	{
		return Rst;
	}

	public void setRst(String rst)
	{
		Rst = rst;
	}

	public String getToken()
	{
		return Token;
	}

	public void setToken(String token)
	{
		Token = token;
	}

	public List<Object> getCData()
	{
		return CData;
	}

	public void setCData(List<Object> cData)
	{
		CData = cData;
	}

	public String getMapAngle() {
		return MapAngle;
	}

	public void setMapAngle(String mapAngle) {
		MapAngle = mapAngle;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getCName() {
		return CName;
	}

	public void setCName(String cName) {
		CName = cName;
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

	public String getMapLev() {
		return MapLev;
	}

	public void setMapLev(String mapLev) {
		MapLev = mapLev;
	}

	public String getDemo() {
		return Demo;
	}

	public void setDemo(String demo) {
		Demo = demo;
	}

	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}
	
	
}