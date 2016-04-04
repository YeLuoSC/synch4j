package com.synch4j.execute.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.synch4j.execute.service.ISynch2TestService;

@Controller
@RequestMapping(value="/synch2/test")
public class Synch2TestController {
	
	@Resource
	private ISynch2TestService synch2TestServiceImpl;
	
	@RequestMapping("")
	public String test(HttpServletRequest request,
			HttpServletResponse response) {
		return "synch2/test";
	}
	
	/**
     * 获取表头
     * @return grid
     * @throws Exception
     */
    @RequestMapping(value="getDefine")
    @ResponseBody
    public Object getDefine()throws Exception{
//        Table grid = synch2TestServiceImpl.setTableDefine();
//        return grid;
    	return null;
    }
    
    /**
     * 获取表格数据
     * @return 
     * @throws Exception
     */
    @RequestMapping(value="getData")
    @ResponseBody
    public Object getTableData(String grid)throws Exception{
       /* CommonGrid commonGrid = (CommonGrid)(new ObjectMapper()).readValue(grid,CommonGrid.class);
        return synch2TestServiceImpl.getData(commonGrid);*/
    	return null;
    }
    
    @RequestMapping(value="reset")
    @ResponseBody
    public Object reset(String fileGuidArr)throws Exception{
//    	ObjectMapper mapper = new ObjectMapper();
//    	List<String> list = mapper.readValue(fileGuidArr, ArrayList.class);
//        synch2TestServiceImpl.reset(list);
        return "1";
    }
}
