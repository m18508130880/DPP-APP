package bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import util.CommUtil;

public class GjGxUtil {
	
	private class DevGJData
	{
		int		sn				= 0;
		float	water			= 0;
		String	Top_Height		= "0";
		String	Equip_Height	= "0";

	}
	/**
	 * 自动填充出设备井以外井的水位数据
	 * 
	 * @param gjObj
	 * @param gxObj
	 * @param Id
	 * @return
	 */
	public ArrayList<Object> AnalogGJList(ArrayList<?> gjObj, ArrayList<?> gxObj, String Id)
	{
		/*** 2017.2.7 设置超过两小时没采集的设备的直为0 cj ***/
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date newTime;
		Date equip_time;
		long between = 0;
		long hour = 0;
		/*******/
		// gjObj ArrayList转Hash
		Hashtable<String, DevGJBean> objGJTable = null;
		objGJTable = new Hashtable<String, DevGJBean>();
		Iterator<?> iterGJ = gjObj.iterator();
		while (iterGJ.hasNext())
		{
			DevGJBean gjBean = (DevGJBean) iterGJ.next();
			String gjId = gjBean.getId();

			/*** 2017.2.7 设置超过两小时没采集的设备的值为0 cj ***/
			if (gjBean.getEquip_Time().length() > 1)
			{
				try
				{
					newTime = df.parse(df.format(new Date()));
					equip_time = df.parse(gjBean.getEquip_Time());
					between = (newTime.getTime() - equip_time.getTime()) / 1000;// 除以1000是为了转换成秒
					hour = between / 3600;
				}
				catch (ParseException e)
				{
					e.printStackTrace();
				}
				if (hour > 2)
				{
					gjBean.setCurr_Data("0.00");
				}
			}
			/*******/
			HashPut(objGJTable, gjId, gjBean);
		}
		// gxObj ArrayList转Hash
		Hashtable<String, DevGXBean> objGXTable = null;
		objGXTable = new Hashtable<String, DevGXBean>();
		Iterator<?> iterGX = gxObj.iterator();
		while (iterGX.hasNext())
		{
			DevGXBean gxBean = (DevGXBean) iterGX.next();
			String gxId = gxBean.getId();
			HashPut(objGXTable, gxId, gxBean);
		}

		// 遍历，梳理先后关系,转成ArrayList
		DevGJBean nextGJ = (DevGJBean) HashGet(objGJTable, Id);
		ArrayList<Object> gjList = new ArrayList<Object>(); // 管井ArrayList
		gjList.add(nextGJ); // 加入第一个管井
		ArrayList<Object> devList = new ArrayList<Object>(); // 设备管井ArrayList
		DevGXBean nextGX = new DevGXBean();

		int sn = 0;
		int option = 0;
		do
		{
			if (nextGJ.getFlag().equals("2"))
			{
				option = 1;
			}
			if (!nextGJ.getCurr_Data().equals("0.00"))
			{
				DevGJData devGJ = new DevGJData();
				devGJ.sn = sn;
				nextGJ.getBase_Height();
				devGJ.Top_Height = nextGJ.getTop_Height();
				devGJ.Equip_Height = nextGJ.getEquip_Height();
				devGJ.water = CommUtil.StrToFloat(nextGJ.getTop_Height()) - CommUtil.StrToFloat(nextGJ.getEquip_Height()) + CommUtil.StrToFloat(nextGJ.getCurr_Data());
				devList.add(devGJ);
			}
			String outGXId = nextGJ.getOut_Id();
			nextGX = (DevGXBean) HashGet(objGXTable, outGXId);
			if(null != nextGX)
			{
				String outGJId = nextGX.getEnd_Id();
				String startGJId = nextGX.getStart_Id();
				System.out.println("outGJId["+outGJId+"]startGJId["+startGJId+"]");
				if(outGJId.substring(2,5).equals(startGJId.substring(2,5)))
				{
					nextGJ = (DevGJBean) HashGet(objGJTable, outGJId);
					sn++;
					gjList.add(nextGJ);
				}
				else
				{
					option = 1;
				}
			}
		}
		while (option == 0);

		// 如果没有设备井，返回由选择管井到终点的管井列表
		System.out.println("devList.size()["+devList.size()+"]");
		if (0 == devList.size())
		{
			return gjList;
		}
		// 如果有设备井，更新管井列表中无设备管井的水位
		int count = 0;
		DevGJData devGJData1 = ((DevGJData) devList.get(0)); // 取到第一个有设备的管井
		DevGJData devGJDataN = ((DevGJData) devList.get(devList.size() - 1)); // 取到最后一个有设备的管井
		@SuppressWarnings("rawtypes")
		Iterator it = gjList.iterator();
		while (it.hasNext())
		{
			DevGJBean gjBean = (DevGJBean) it.next();
			int flag = 0;
			for (int i = 0; i < devList.size(); i++)
			{
				if (count == ((DevGJData) devList.get(i)).sn)
				{
					flag = 1;
					break;
				}
			}
			if (1 == flag) // 设备管井本身 下一个
			{
				gjBean.setCurr_Data(String.valueOf(CommUtil.StrToFloat(gjBean.getTop_Height())
						- CommUtil.StrToFloat(gjBean.getEquip_Height())
						+ CommUtil.StrToFloat(gjBean.getCurr_Data())));// 赋予当前管井的水位高度
				count++;
				continue;
			}
			if (count < devGJData1.sn) // 第一个设备之前的管井
			{
				float DevGJWater = devGJData1.water; // 第一个设备的水位
				gjBean.setCurr_Data(String.valueOf(DevGJWater));// 赋予当前管井的水位高度
			}
			else if (count > devGJDataN.sn) // 最后一个设备之后的管井
			{
				float DevGJWater = devGJDataN.water;
				gjBean.setCurr_Data(String.valueOf(DevGJWater));// 赋予当前管井的水位高度
			}
			else
			// 第一个设备和最后一个设备之间的管井
			{

				for (int i = 1; i < devList.size(); i++)
				{
					DevGJData devGJDataI_1 = (DevGJData) devList.get(i - 1);
					DevGJData devGJDataI = (DevGJData) devList.get(i);
					if (devGJDataI_1.sn < count && devGJDataI.sn > count)
					{
						// 之后的 + （之后的-之前的）/（中间差数）
						float i_1Lev = devGJDataI_1.water;
						float iLev = devGJDataI.water;
						float waterLev = i_1Lev + (iLev - i_1Lev) * (count - devGJDataI_1.sn) / (devGJDataI.sn - devGJDataI_1.sn);
						gjBean.setCurr_Data(String.valueOf(waterLev));
						break;
					}
				}
			}
			count++;
		}
		return gjList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void HashPut(Hashtable hashTable, String key, Object obj)
	{
		if (hashTable.containsKey(key))
		{
			hashTable.remove(key); // 在哈希表里移除客户端
		}
		hashTable.put(key, obj);
	}

	@SuppressWarnings({ "rawtypes" })
	public Object HashGet(Hashtable hashTable, String key)
	{
		if (!hashTable.isEmpty() && hashTable.containsKey(key))
		{
			return hashTable.get(key);
		}
		return null;
	}
}
