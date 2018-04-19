package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.ProjectInfoJsonBean;
import json.UserRoleJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

/**
 * 管理权限和功能权限
 */
public class UserRoleBean extends RmiBean {
	public final static long serialVersionUID = RmiBean.RMI_USER_ROLE;

	public long getClassId() {
		return serialVersionUID;
	}

	public UserRoleBean() {
		super.className = "UserRoleBean";
	}

	public void ExecCmd(HttpServletRequest request,
			HttpServletResponse response, Rmi pRmi, boolean pFromZone,
			String Url, HashMap<String, Date> TokenList)
					throws ServletException, IOException {
		PrintWriter output = null;
		try {
			getHtmlData(request);
			UserRoleJsonBean json = new UserRoleJsonBean();
			json.setUrl(Url);
			json.setRst(CommUtil.IntToStringLeftFillZero(
					MsgBean.STA_FAILED, 4));
			if (TokenList.containsKey(Token)) {
				TokenList.put(Token, new Date());
				switch (Cmd) {
				case 0:
					// 获取管理权限
					if (msgBean.getStatus() == MsgBean.STA_SUCCESS) {
						List<Object> CData = new ArrayList<Object>();
						
						msgBean = pRmi.RmiExec(Cmd, this, 0, 25);
						UserRoleBean Manage_Role = (UserRoleBean) ((ArrayList<?>)msgBean.getMsg()).get(0);
						
						String[] points = Manage_Role.getPoint().split(",");
						
						ProjectInfoBean projectInfoBean = new ProjectInfoBean();
						msgBean = pRmi.RmiExec(0, projectInfoBean, 0, 25);
						ArrayList<?> projectList = (ArrayList<?>) msgBean.getMsg();
						Iterator<?> projectIterator = projectList.iterator();
						while (projectIterator.hasNext()) {
							ProjectInfoBean RealJson = (ProjectInfoBean) projectIterator
									.next();
							ProjectInfoJsonBean projectJson = new ProjectInfoJsonBean();
							if (Arrays.asList(points)
									.contains(RealJson.getId())) {
								projectJson.setId(RealJson.getId());
								projectJson.setLatitude(RealJson.getLatitude());
								projectJson.setLongitude(RealJson.getLongitude());
								projectJson.setWX_Lng(RealJson.getWX_Lng());
								projectJson.setWX_Lat(RealJson.getWX_Lat());
								projectJson.setCName(RealJson.getCName());
								projectJson.setMapLev(RealJson.getMapLev());
								projectJson.setMapAngle(RealJson.getMapAngle());
								CData.add(projectJson);
							}
						}
						json.setCData(CData);
						json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_SUCCESS, 4));
					}
					break;
				}
			} else {
				// 鉴权失败
				json.setRst(CommUtil.IntToStringLeftFillZero(
						MsgBean.STA_ACCOUNT_NOT_LOGIN, 4));
			}

			JSONObject jsonObj = (JSONObject) JSONObject.toJSON(json);
			response.setCharacterEncoding("UTF-8");
			output = response.getWriter();
			output.write(jsonObj.toString());
			output.flush();

			//System.out.println("AppGisJson:" + jsonObj.toString() + ";");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			output.close();
		}

	}

	public String getSql(int pCmd) {
		String Sql = "";
		switch (pCmd) {
		case 0:// 管理权限/单个
			Sql = " select t.id, t.cname, t.point, t.zoomx, t.zoomy, t.zoomlev from role t where t.id = " + Id;
			break;
		case 1:// 功能权限
			Sql = " select t.id, t.cname, t.point, t.zoomx, t.zoomy, t.zoomlev from role t where length(t.id) = 3 order by t.id";
			break;
		case 2:// 功能点
			Sql = " select t.id, t.cname, '' as point from fp_info t where t.status = '0' order by t.id";
			break;
		case 3:// 管理权限
			Sql = " select t.id, t.cname, t.point, t.zoomx, t.zoomy, t.zoomlev from role t where substr(t.id,1,2) = '50' and (length(t.id) = 4 or length(t.id) = 6 or length(t.id) = 8) order by t.id";
			break;
		}
		return Sql;
	}

	public boolean getData(ResultSet pRs) {
		boolean IsOK = true;
		try {
			setId(pRs.getString(1));
			setCName(pRs.getString(2));
			setPoint(pRs.getString(3));
			setZoomX(pRs.getString(4));
			setZoomY(pRs.getString(5));
			setZoomLev(pRs.getString(6));
		} catch (SQLException sqlExp) {
			sqlExp.printStackTrace();
		}
		return IsOK;
	}

	public boolean getHtmlData(HttpServletRequest request) {
		boolean IsOK = true;
		try {
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setCName(CommUtil.StrToGB2312(request.getParameter("CName")));
			setPoint(CommUtil.StrToGB2312(request.getParameter("Point")));
			setZoomX(CommUtil.StrToGB2312(request.getParameter("ZoomX")));
			setZoomY(CommUtil.StrToGB2312(request.getParameter("ZoomY")));
			setZoomLev(CommUtil.StrToGB2312(request.getParameter("ZoomLev")));
			
			setCmd(CommUtil.StrToInt(request.getParameter("Cmd")));
			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));
		} catch (Exception Exp) {
			Exp.printStackTrace();
		}
		return IsOK;
	}

	private String Id;
	private String CName;
	private String Point;
	private String ZoomX;
	private String ZoomY;
	private String ZoomLev;

	private int Cmd;
	private String Token;

	public int getCmd() {
		return Cmd;
	}

	public void setCmd(int cmd) {
		Cmd = cmd;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public String getZoomX() {
		return ZoomX;
	}

	public void setZoomX(String zoomX) {
		ZoomX = zoomX;
	}

	public String getZoomY() {
		return ZoomY;
	}

	public void setZoomY(String zoomY) {
		ZoomY = zoomY;
	}

	public String getZoomLev() {
		return ZoomLev;
	}

	public void setZoomLev(String zoomLev) {
		ZoomLev = zoomLev;
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
}
