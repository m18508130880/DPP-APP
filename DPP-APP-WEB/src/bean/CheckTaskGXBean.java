package bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import json.CheckTaskGXJsonBean;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

public class CheckTaskGXBean extends RmiBean
{
	public final static long	serialVersionUID	= RmiBean.RMI_CHECK_GX;

	public long getClassId()
	{
		return serialVersionUID;
	}

	public CheckTaskGXBean()
	{
		super.className = "CheckTaskGXBean";
	}
	
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response,
			Rmi pRmi, boolean pFromZone, String Url,
			HashMap<String, String> TokenList) throws ServletException,
			IOException {
		PrintWriter output = null;
		try {
			getHtmlData(request);

			CheckTaskGXJsonBean json = new CheckTaskGXJsonBean();
			json.setUrl(Url);
			json.setRst(CommUtil.IntToStringLeftFillZero(
					MsgBean.STA_FAILED, 4));
			if (TokenList.containsValue(Token)) {
				List<Object> CData = new ArrayList<Object>();
				msgBean = pRmi.RmiExec(Cmd, this, 0, 25);
				switch (Cmd) {
				case 0:
				case 1:
					// 获取管井信息
					if (msgBean.getStatus() == MsgBean.STA_SUCCESS) {
						ArrayList<?> checkTaskGXList = (ArrayList<?>) msgBean
								.getMsg();
						CData = objToJson(checkTaskGXList, CData);
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

	public void uploadPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取文件需要上传到的路径
        String path = request.getRealPath("/upload") + "/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        //logger.debug("path=" + path);

        request.setCharacterEncoding("utf-8");  //设置编码
        //获得磁盘文件条目工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();

        //如果没以下两行设置的话,上传大的文件会占用很多内存，
        //设置暂时存放的存储室,这个存储室可以和最终存储文件的目录不同
        /**
         * 原理: 它是先存到暂时存储室，然后再真正写到对应目录的硬盘上，
         * 按理来说当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的
         * 然后再将其真正写到对应目录的硬盘上
         */
        factory.setRepository(dir);
        //设置缓存的大小，当上传文件的容量超过该缓存时，直接放到暂时存储室
        factory.setSizeThreshold(1024 * 1024);
        //高水平的API文件上传处理
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> list = upload.parseRequest(request);
            FileItem picture = null;
            for (FileItem item : list) {
                //获取表单的属性名字
                String name = item.getFieldName();
                //如果获取的表单信息是普通的 文本 信息
                if (item.isFormField()) {
                    //获取用户具体输入的字符串
                    String value = item.getString();
                    request.setAttribute(name, value);
                    //logger.debug("name=" + name + ",value=" + value);
                } else {
                    picture = item;
                }
            }

            //自定义上传图片的名字为userId.jpg
            String fileName = request.getAttribute("userId") + ".jpg";
            String destPath = path + fileName;
            //logger.debug("destPath=" + destPath);

            //真正写到磁盘上
            File file = new File(destPath);
            OutputStream out = new FileOutputStream(file);
            InputStream in = picture.getInputStream();
            int length = 0;
            byte[] buf = new byte[1024];
            // in.read(buf) 每次读到的数据存放在buf 数组中
            while ((length = in.read(buf)) != -1) {
                //在buf数组中取出数据写到（输出流）磁盘上
                out.write(buf, 0, length);
            }
            in.close();
            out.close();
        } catch (FileUploadException e1) {
            //logger.error("", e1);
        } catch (Exception e) {
            //logger.error("", e);
        }


        PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("success", true);
        printWriter.write(JSONObject.toJSONString(res));
        printWriter.flush();
    }
	public List<Object> objToJson(ArrayList<?> list, List<Object> CData){
		Iterator<?> iterator = list.iterator();
		while (iterator.hasNext()) {
			CheckTaskGXBean RealJson = (CheckTaskGXBean) iterator.next();
			CheckTaskGXJsonBean json = new CheckTaskGXJsonBean();
			json.setSN(RealJson.getSN());
			json.setId(RealJson.getId());
			json.setProject_Id(RealJson.getProject_Id());
			json.setCT_SN(RealJson.getCT_SN());
			json.setStatus(RealJson.getStatus());
			json.setImages(RealJson.getImages());
			json.setCheck_Time(RealJson.getCheck_Time());
			json.setStart_Id(RealJson.getStart_Id());
			json.setEnd_Id(RealJson.getEnd_Id());
			json.setRoad(RealJson.getRoad());
			json.setDes(RealJson.getDes());

			CData.add(json);
		}
		return CData;
		
	}

	/**
	 * 获取相应sql语句
	 * 
	 */
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
			case 0:// 查询（类型&项目）
				Sql = " select t.sn, t.id, t.project_id, t.ct_sn, t.status, t.images, t.check_time, t.start_id, t.end_id, t.road, t.des " +
					  " from view_check_gx t " + 
					  " where t.project_id = '" + Project_Id + "'" + 
					  " and t.id like '" + Id + "%'" + 
					  " order by t.sn ";
				break;
			case 1:// 查询根据sn查询
				Sql = " select t.sn, t.id, t.project_id, t.ct_sn, t.status, t.images, t.check_time, t.start_id, t.end_id, t.road, t.des " +
					  " from view_check_gx t " + 
					  " where t.ct_sn = '" + CT_SN + "'" +
					  " order by t.sn ";
				break;
			case 12:// 删除
				Sql = " delete from check_task_gx where id = '" + Id + "' and project_id = '" + Project_Id + "'";
				break;
		}
		return Sql;
	}

	/**
	 * 将数据库中 结果集的数据 封装到DevGjBean中
	 * 
	 */
	public boolean getData(ResultSet pRs)
	{
		boolean IsOK = true;
		try
		{
			setSN(pRs.getString(1));
			setId(pRs.getString(2));
			setProject_Id(pRs.getString(3));
			setCT_SN(pRs.getString(4));
			setStatus(pRs.getString(5));
			setImages(pRs.getString(6));
			setCheck_Time(pRs.getString(7));
			setStart_Id(pRs.getString(8));
			setEnd_Id(pRs.getString(9));
			setRoad(pRs.getString(10));
			setDes(pRs.getString(11));
		}
		catch (SQLException sqlExp)
		{
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
	public boolean getHtmlData(HttpServletRequest request)
	{
		boolean IsOK = true;
		try
		{
			setSN(CommUtil.StrToGB2312(request.getParameter("SN")));
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setProject_Id(CommUtil.StrToGB2312(request.getParameter("Project_Id")));
			setCT_SN(CommUtil.StrToGB2312(request.getParameter("CT_SN")));
			setStatus(CommUtil.StrToGB2312(request.getParameter("Status")));
			setImages(CommUtil.StrToGB2312(request.getParameter("Images")));
			setCheck_Time(CommUtil.StrToGB2312(request.getParameter("Check_Time")));
			setStart_Id(CommUtil.StrToGB2312(request.getParameter("Start_Id")));
			setEnd_Id(CommUtil.StrToGB2312(request.getParameter("End_Id")));
			setRoad(CommUtil.StrToGB2312(request.getParameter("Road")));
			setDes(CommUtil.StrToGB2312(request.getParameter("Des")));

			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));

			setCmd(CommUtil.StrToInt(request.getParameter("Cmd")));
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}

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

	private String Token;
	
	private int Cmd;

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