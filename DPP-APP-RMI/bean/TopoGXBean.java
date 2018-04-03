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

import json.TopoGXJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

public class TopoGXBean extends RmiBean {
	public final static long serialVersionUID = RmiBean.RMI_TOPOGX;

	public long getClassId() {
		return serialVersionUID;
	}

	public TopoGXBean() {
		super.className = "TopoGXBean";
	}

	public void ExecCmd(HttpServletRequest request, HttpServletResponse response,
			Rmi pRmi, boolean pFromZone, String Url,
			HashMap<String, String> TokenList) throws ServletException,
			IOException {
		PrintWriter output = null;
		try {
			getHtmlData(request);

			TopoGXJsonBean json = new TopoGXJsonBean();
			json.setUrl(Url);
			json.setRst(CommUtil.IntToStringLeftFillZero(
					MsgBean.STA_FAILED, 4));
			if (TokenList.containsKey(Token)) {
				msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0, 0);
				switch (currStatus.getCmd()) {
				case 0:
					// 获取管线信息
					if (msgBean.getStatus() == MsgBean.STA_SUCCESS) {
						List<Object> CData = new ArrayList<Object>();
						json.setRst("0000");
						ArrayList<?> topoGXList = (ArrayList<?>) msgBean
								.getMsg();
						Iterator<?> topoGJIterator = topoGXList.iterator();
						while (topoGJIterator.hasNext()) {
							TopoGXBean RealJson = (TopoGXBean) topoGJIterator
									.next();
							TopoGXJsonBean topoGXJson = new TopoGXJsonBean();
							topoGXJson.setId(RealJson.getId());
							topoGXJson.setStart_Id(RealJson.getStart_Id());
							topoGXJson.setEnd_Id(RealJson.getEnd_Id());
							topoGXJson.setFlag(RealJson.getFlag());
							topoGXJson.setRoad(RealJson.getRoad());
							topoGXJson.setLength(RealJson.getLength());
							
							CData.add(topoGXJson);
						}
						json.setCData(CData);;
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

			System.out.println("AppGisJson{" + jsonObj.toString() + "}");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			output.close();
		}
	}

	/**
	 * 获取相应sql语句
	 * 
	 */
	public String getSql(int pCmd) {
		String Sql = "";
		switch (pCmd) {
		case 0:// 查询dev_gx，用于地图显示
			Sql = " select t.id, t.start_id, t.end_id, t.road, t.flag, t.length"
					+ " from dev_gx t "
					+ " where instr('" + Project_Id + "', project_id) > 0 "
					+ " and instr('" + Road + "', road) > 0 "
					+ " order by id";
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
			setId(pRs.getString(1));
			setStart_Id(pRs.getString(2));
			setEnd_Id(pRs.getString(3));
			setRoad(pRs.getString(4));
			setFlag(pRs.getString(5));
			setLength(pRs.getString(6));
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
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setStart_Id(CommUtil
					.StrToGB2312(request.getParameter("Start_Id")));
			setEnd_Id(CommUtil.StrToGB2312(request.getParameter("End_Id")));
			setRoad(CommUtil.StrToGB2312(request.getParameter("Road")));
			setFlag(CommUtil.StrToGB2312(request.getParameter("Flag")));
			setLength(CommUtil.StrToGB2312(request.getParameter("Length")));
			
			setProject_Id(CommUtil.StrToGB2312(request.getParameter("Project_Id")));
			
			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));
		} catch (Exception Exp) {
			Exp.printStackTrace();
		}
		return IsOK;
	}

	private String Id;
	private String Start_Id;
	private String End_Id;
	private String Flag;
	private String Road;
	private String Length;
	
	private String Project_Id;
	
	private String Token;

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

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public String getProject_Id() {
		return Project_Id;
	}

	public void setProject_Id(String project_Id) {
		Project_Id = project_Id;
	}

}