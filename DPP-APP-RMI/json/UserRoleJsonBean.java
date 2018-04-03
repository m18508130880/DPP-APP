package json;

import java.util.ArrayList;
import java.util.List;

/** 管理权限和功能权限
 * @author cui
 *  与表 role关联
 */
public class UserRoleJsonBean
{
	private String Id;
	private String CName;
	private String Point;
	private String ZoomX;
	private String ZoomY;
	private String ZoomLev;
	
	private String RoleList;
	private String Sid;
	
	private String Url;
	private String Rst;
	private String Token;
	List<Object> CData = new ArrayList<Object>();

	public List<Object> getCData()
	{
		return CData;
	}

	public void setCData(List<Object> cData)
	{
		CData = cData;
	}

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

	public String getZoomX()
	{
		return ZoomX;
	}

	public void setZoomX(String zoomX)
	{
		ZoomX = zoomX;
	}

	public String getZoomY()
	{
		return ZoomY;
	}

	public void setZoomY(String zoomY)
	{
		ZoomY = zoomY;
	}

	public String getZoomLev()
	{
		return ZoomLev;
	}

	public void setZoomLev(String zoomLev)
	{
		ZoomLev = zoomLev;
	}

	public String getRoleList() {
		return RoleList;
	}
	public void setRoleList(String roleList) {
		RoleList = roleList;
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
	public String getPoint() {
		return Point;
	}
	public void setPoint(String point) {
		Point = point;
	}

	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}
}
