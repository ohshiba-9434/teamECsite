package com.internousdev.georgia.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.georgia.dao.MCategoryDAO;
import com.internousdev.georgia.dto.MCategoryDTO;
import com.internousdev.georgia.util.CommonUtility;
import com.opensymphony.xwork2.ActionSupport;

public class HomeAction extends ActionSupport implements SessionAware {
	
	private Map<String, Object> session;
	
	public String execute() {
		
		//ログイン済みでなければ未ログインとする
		if (!session.containsKey("logined")) {
			session.put("logined", 0);
		}
		
		//仮ユーザーIDの付与
		if (Integer.parseInt(String.valueOf(session.get("logined"))) == 0 && !session.containsKey("tempUserId")) {
			CommonUtility commonUtility = new CommonUtility();
			session.put("tempUserId", commonUtility.getRamdomValue());
		}
		
		//カテゴリーを表示する
		if (!session.containsKey("mCategoryDTOList")) {
			List<MCategoryDTO> mCategoryDTOList = new ArrayList<MCategoryDTO>();
			MCategoryDAO mCategoryDAO = new MCategoryDAO();
			mCategoryDTOList = mCategoryDAO.getMCategoryList();
			
			session.put("mCategoryDTOList", mCategoryDTOList);
		}
		return SUCCESS;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}