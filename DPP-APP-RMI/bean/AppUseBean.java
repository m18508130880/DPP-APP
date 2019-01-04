package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.AppUseJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

public class AppUseBean extends RmiBean {
	public final static long serialVersionUID = RmiBean.RMI_APP_USE;

	public long getClassId() {
		return serialVersionUID;
	}

	public AppUseBean() {
		super.className = "AppUseBean";
	}

	public void ExecCmd(HttpServletRequest request, HttpServletResponse response,
			Rmi pRmi, boolean pFromZone, String Url,
			HashMap<String, String> TokenList) throws ServletException,
			IOException {
		PrintWriter output = null;
		try {
			getHtmlData(request);

			AppUseJsonBean json = new AppUseJsonBean();
			json.setUrl(Url);
			json.setRst(CommUtil.IntToStringLeftFillZero(
					MsgBean.STA_FAILED, 4));
			// 首页无需鉴权
			//if (TokenList.containsValue(Token)) {
				List<Object> CData = new ArrayList<Object>();
				msgBean = pRmi.RmiExec(Cmd, this, 0, 25);
				switch (Cmd) {
				case 0:
					// 获取信息
					if (msgBean.getStatus() == MsgBean.STA_SUCCESS) {
						ArrayList<?> newsList = (ArrayList<?>) msgBean
								.getMsg();
						CData = objToJson(newsList, CData);
						json.setCData(CData);
						json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_SUCCESS, 4));
					}
					break;
				case 1:
					// 获取信息
					if (msgBean.getStatus() == MsgBean.STA_SUCCESS) {
						ArrayList<?> newsList = (ArrayList<?>) msgBean
								.getMsg();
						CData = objToJson_hanContent(newsList, CData);
						json.setCData(CData);
						json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_SUCCESS, 4));
					}
					break;
				}
//				
//			} else {
//				// 鉴权失败
//				json.setRst(CommUtil.IntToStringLeftFillZero(
//						MsgBean.STA_ACCOUNT_NOT_LOGIN, 4));
//			}

			JSONObject jsonObj = (JSONObject) JSONObject.toJSON(json);
			response.setCharacterEncoding("UTF-8");
			output = response.getWriter();
			output.write(jsonObj.toString());
			output.flush();

			//System.out.println("AppGisJson:" + jsonObj.toString() + ";");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(output != null) {
				output.close();
			}
		}
	}
	
	// 不转换content
	public List<Object> objToJson(ArrayList<?> objList, List<Object> CData){
		Iterator<?> objIterator = objList.iterator();
		while (objIterator.hasNext()) {
			AppUseBean RealJson = (AppUseBean) objIterator
					.next();
			AppUseBean json = new AppUseBean();
			json.setSN(RealJson.getSN());
			json.setProject_Id(RealJson.getProject_Id());
			json.setProject_Name(RealJson.getProject_Name());
			json.setImgPath(RealJson.getImgPath());
			//json.setContent(RealJson.getContent());
			json.setCTime(RealJson.getCTime().substring(0, 10));
			json.setDes(RealJson.getDes());
			CData.add(json);
		}
		return CData;
		
	}

	// 转换content
	public List<Object> objToJson_hanContent(ArrayList<?> objList, List<Object> CData){
		Iterator<?> objIterator = objList.iterator();
		while (objIterator.hasNext()) {
			AppUseBean RealJson = (AppUseBean) objIterator
					.next();
			AppUseJsonBean json = new AppUseJsonBean();
			json.setSN(RealJson.getSN());
			json.setProject_Id(RealJson.getProject_Id());
			json.setProject_Name(RealJson.getProject_Name());
			json.setImgPath(RealJson.getImgPath());
			json.setContent(RealJson.getContent());
			json.setCTime(RealJson.getCTime().substring(0, 10));
			json.setDes(RealJson.getDes());
			CData.add(json);
		}
		return CData;
		
	}
	/**
	 * 获取相应sql语句
	 * 
	 */
	public String getSql(int pCmd) {
		String Sql = "";
		switch (pCmd) {
		case 0:// 查询全部
			Sql = " select t.sn, t.project_id, t.project_name, imgpath, t.content, t.ctime, t.des"
					+ " from app_use t order by t.ctime desc";
			break;
		case 1:// 查询单个
			Sql = " select t.sn, t.project_id, t.project_name, imgpath, t.content, t.ctime, t.des"
					+ " from app_use t "
					+ " where t.sn = '" + SN + "'"
					+ " order by t.ctime desc";
			break;
		}
		return Sql;
	}

	/**
	 * 将数据库中 结果集的数据 封装到DevGjBean中
	 * 
	 */
	public boolean getData(ResultSet pRs) {
		boolean IsOK = true;
		try {
			setSN(pRs.getString(1));
			setProject_Id(pRs.getString(2));
			setProject_Name(pRs.getString(3));
			setImgPath(pRs.getString(4));
			setContent(pRs.getString(5));
			setCTime(pRs.getString(6));
			setDes(pRs.getString(7));
		} catch (SQLException sqlExp) {
			sqlExp.printStackTrace();
		}
		return IsOK;
	}

	/**
	 * 得到页面数据
	 * 
	 * @param request
	 * @return
	 */
	public boolean getHtmlData(HttpServletRequest request) {
		boolean IsOK = true;
		try {
			setSN(CommUtil.StrToGB2312(request.getParameter("SN")));
			setProject_Id(CommUtil.StrToGB2312(request.getParameter("Project_Id")));
			setProject_Name(CommUtil.StrToGB2312(request.getParameter("Project_Name")));
			setImgPath(CommUtil.StrToGB2312(request.getParameter("ImgPath")));
			setContent(CommUtil.StrToGB2312(request.getParameter("Content")));
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));
			setDes(CommUtil.StrToGB2312(request.getParameter("Des")));
			
			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));
			setCmd(CommUtil.StrToInt(request.getParameter("Cmd")));
		} catch (Exception Exp) {
			Exp.printStackTrace();
		}
		return IsOK;
	}

	private String SN;
	private String Project_Id;
	private String Project_Name;
	private String ImgPath;
	private String Content;
	private String CTime;
	private String Des;
	
	private String Token;
	private int Cmd;

	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
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

	public String getImgPath() {
		return ImgPath;
	}

	public void setImgPath(String imgPath) {
		ImgPath = imgPath;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getCTime() {
		return CTime;
	}

	public void setCTime(String cTime) {
		CTime = cTime;
	}

	public String getDes() {
		return Des;
	}

	public void setDes(String des) {
		Des = des;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public int getCmd() {
		return Cmd;
	}

	public void setCmd(int cmd) {
		Cmd = cmd;
	}

}