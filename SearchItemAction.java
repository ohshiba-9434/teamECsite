package com.internousdev.georgia.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.georgia.dao.MCategoryDAO;
import com.internousdev.georgia.dao.ProductInfoDAO;
import com.internousdev.georgia.dto.MCategoryDTO;
import com.internousdev.georgia.dto.ProductInfoDTO;
import com.internousdev.georgia.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class SearchItemAction extends ActionSupport implements SessionAware {
	
	private String categoryId;
	private String keywords;
	private List<String> keywordsErrorMessageList;
	private List<ProductInfoDTO> productInfoDTOList;
	private Map<String, Object> session;
	
	public String execute() {
		
		//カテゴリー未選択の場合は全てのカテゴリーを設定
		if (categoryId == null) {
			categoryId = "1";
		}
		
		if (StringUtils.isBlank(keywords)) {
			//検索ワードがスペース,nullなら空文字とみなす
			keywords = "";
		} else {
			//検索ワードを区切るときの余分なスペースを消す
			keywords = keywords.replaceAll("　", " ").replaceAll("\\s{2,}", " ").trim();
		}
		
		InputChecker inputChecker = new InputChecker();
		
		if (!keywords.equals("")) {
			//入力エラーチェック
			keywordsErrorMessageList = inputChecker.doCheck("検索ワード", keywords, 0, 50, true, true, true, true, true, true);
			
			if (keywordsErrorMessageList.size() > 0) {
				return SUCCESS;
			}
		}
		
		ProductInfoDAO productInfoDAO = new ProductInfoDAO();
		switch (categoryId) {
		case "1":
			//全てのカテゴリーで検索
			productInfoDTOList = productInfoDAO.getProductInfoListByKeyword(keywords.split(" "));
			break;
			
		default:
			//選択されたカテゴリーで検索
			productInfoDTOList = productInfoDAO.getProductInfoListByCategoryIdAndKeyword(categoryId, keywords.split(" "));
			break;
		}
		
		//カテゴリーが表示されていなければ選択できないので、表示する
		if (!session.containsKey("mCategoryDTOList")) {
			List<MCategoryDTO> mCategoryDTOList = new ArrayList<MCategoryDTO>();
			MCategoryDAO mCategoryDAO = new MCategoryDAO();
			mCategoryDTOList = mCategoryDAO.getMCategoryList();
			
			session.put("mCategoryDTOList", mCategoryDTOList);
		}
		return SUCCESS;
	}

	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public List<String> getKeywordsErrorMessageList() {
		return keywordsErrorMessageList;
	}
	public void setKeywordsErrorMessageList(List<String> keywordsErrorMessageList) {
		this.keywordsErrorMessageList = keywordsErrorMessageList;
	}
	
	public List<ProductInfoDTO> getProductInfoDTOList() {
		return productInfoDTOList;
	}
	public void setProductInfoDTOList(List<ProductInfoDTO> productInfoDTOList) {
		this.productInfoDTOList = productInfoDTOList;
	}
	
	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}